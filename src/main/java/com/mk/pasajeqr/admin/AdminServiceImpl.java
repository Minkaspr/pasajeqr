package com.mk.pasajeqr.admin;

import com.mk.pasajeqr.admin.request.AdminCreateRQ;
import com.mk.pasajeqr.admin.request.AdminUpdateRQ;
import com.mk.pasajeqr.admin.response.AdminDetailRS;
import com.mk.pasajeqr.admin.response.AdminUserItemRS;
import com.mk.pasajeqr.admin.response.AdminsRS;
import com.mk.pasajeqr.common.exception.DuplicateResourceException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import com.mk.pasajeqr.utils.ChangePasswordRQ;
import com.mk.pasajeqr.utils.UserStatusRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import com.mk.pasajeqr.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminsRS getAllPaged(Pageable pageable) {
        // Obtener página de entidades Admin desde la base de datos
        Page<Admin> page = adminRepository.findAll(pageable);

        // Mapear cada entidad Admin a su DTO de listado
        List<AdminUserItemRS> adminList = page.getContent().stream()
                .map(AdminUserItemRS::new)
                .toList();

        // Construir y devolver la respuesta paginada
        return new AdminsRS(
                adminList,
                page.getNumber(),       // Número de página actual
                page.getTotalPages(),   // Total de páginas disponibles
                page.getTotalElements() // Total de registros encontrados
        );
    }

    @Override
    @Transactional
    public AdminDetailRS create(AdminCreateRQ request) {
        // Verificar email duplicado
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese email");
        }

        // Verificar DNI duplicado
        if (userRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese DNI");
        }

        // Crear objeto User
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dni(request.getDni())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .status(true)
                .build();

        User savedUser = userRepository.save(user);

        // Crear objeto Admin
        Admin admin = Admin.builder()
                .user(savedUser)
                .birthDate(request.getBirthDate())
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        // Retornar DTO detallado
        return new AdminDetailRS(savedAdmin);
    }

    @Override
    public AdminDetailRS getById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin no encontrado con ID:" + id));
        return new AdminDetailRS(admin);
    }

    @Override
    @Transactional
    public AdminDetailRS update(Long id, AdminUpdateRQ request) {
        // Buscar al Admin por ID (relacionado con User)
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin no encontrado con ID: " + id));

        User user = admin.getUser();

        // Validar email duplicado si se modifica
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese email");
        }

        // Validar DNI duplicado si se modifica
        if (!user.getDni().equals(request.getDni()) &&
                userRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese DNI");
        }

        // Actualizar campos del User
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDni(request.getDni());
        user.setEmail(request.getEmail());
        user.setStatus(request.getUserStatus());

        // Actualizar campo del Admin
        admin.setBirthDate(request.getBirthDate());

        // Guardar cambios en Admin (y cascada se encarga del User)
        Admin updatedAdmin = adminRepository.save(admin);

        // Retornar respuesta detallada
        return new AdminDetailRS(updatedAdmin);
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRQ request) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin no encontrado con ID: " + id));

        User user = admin.getUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual no es correcta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserStatusRS setUserStatus(Long id, boolean active) {
        // Buscar el admin por su ID
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin no encontrado con ID: " + id));

        User user = admin.getUser();

        // Verificar si ya tiene el estado que se quiere aplicar
        if (user.getStatus() == active) {
            String status = active ? "activada" : "desactivada";
            throw new IllegalStateException("La cuenta ya está " + status + ".");
        }

        // Actualizar estado
        user.setStatus(active);
        userRepository.save(user);

        // Retornar respuesta con el nuevo estado
        return new UserStatusRS(user.getId(), active);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Buscar el Admin por su ID
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin no encontrado con id: " + id));

        // Eliminar el Admin (y por cascada también se eliminará el User)
        adminRepository.delete(admin);
    }


    @Override
    @Transactional
    public BulkDeleteRS deleteBulk(List<Long> ids) {
        // Buscar todos los admins existentes por los IDs solicitados
        List<Admin> foundAdmins = adminRepository.findAllById(ids);

        // Obtener solo los IDs encontrados
        List<Long> foundIds = foundAdmins.stream()
                .map(Admin::getUserId)
                .toList();

        // Determinar cuáles IDs no fueron encontrados
        List<Long> notFoundIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        // Eliminar los admins encontrados
        for (Admin admin : foundAdmins) {
            adminRepository.delete(admin);
        }

        // Retornar respuesta con IDs eliminados y no encontrados
        return new BulkDeleteRS(foundIds, notFoundIds);
    }
}
