package com.mk.pasajeqr.driver;

import com.mk.pasajeqr.common.exception.DuplicateResourceException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.driver.request.DriverRegisterRequest;
import com.mk.pasajeqr.driver.request.DriverUpdateRequest;
import com.mk.pasajeqr.driver.response.BulkDeleteResponseDTO;
import com.mk.pasajeqr.driver.response.DriverDetailDTO;
import com.mk.pasajeqr.driver.response.DriverUserListDTO;
import com.mk.pasajeqr.driver.response.DriversResponseDTO;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import com.mk.pasajeqr.user.dto.UserStatusResponse;
import com.mk.pasajeqr.utils.DriverStatus;
import com.mk.pasajeqr.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService{

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DriverDetailDTO createDriver(DriverRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese email");
        }

        if (userRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese DNI");
        }

        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("Ya existe un conductor con esa licencia");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dni(request.getDni())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.DRIVER)
                .status(true)
                .build();

        User savedUser = userRepository.save(user);

        Driver driver = Driver.builder()
                .user(savedUser)
                .licenseNumber(request.getLicenseNumber())
                .status(DriverStatus.AVAILABLE)
                .build();

        Driver savedDriver = driverRepository.save(driver);

        return new DriverDetailDTO(savedDriver);
    }

    @Override
    public DriverDetailDTO getById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver no encontrado con id: " + id));
        return new DriverDetailDTO(driver);
    }

    @Override
    public DriversResponseDTO listDrivers(Pageable pageable) {
        Page<Driver> driversPage = driverRepository.findAll(pageable);
        List<DriverUserListDTO> driverDTOs = driversPage.getContent().stream()
                .map(DriverUserListDTO::new)
                .toList();

        return new DriversResponseDTO(
                driverDTOs,
                driversPage.getNumber(),
                driversPage.getTotalPages(),
                driversPage.getTotalElements()
        );
    }

    @Override
    @Transactional
    public DriverDetailDTO updateDriver(Long id, DriverUpdateRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver no encontrado con id: " + id));

        User user = driver.getUser();

        // Verificaciones de duplicados (excepto si es el mismo usuario)
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese email");
        }

        if (!user.getDni().equals(request.getDni()) && userRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese DNI");
        }

        if (!driver.getLicenseNumber().equals(request.getLicenseNumber()) &&
                driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("Ya existe un conductor con ese número de licencia");
        }

        // Actualizar datos del usuario (excepto contraseña)
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDni(request.getDni());
        user.setStatus(request.getUserStatus());

        userRepository.save(user); // Opcional si tienes cascade en Driver

        // Actualizar datos del driver
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setStatus(request.getStatus());

        Driver updatedDriver = driverRepository.save(driver);

        return new DriverDetailDTO(updatedDriver);
    }

    @Override
    @Transactional
    public UserStatusResponse setUserStatus(Long id, boolean active) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver no encontrado con ID: " + id));

        User user = driver.getUser();
        if (user.getStatus() == active) {
            String status = active ? "activada" : "desactivada";
            throw new IllegalStateException("La cuenta ya está " + status + ".");
        }

        user.setStatus(active);
        userRepository.save(user);

        return new UserStatusResponse(user.getId(), active);
    }

    @Override
    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver no encontrado con id: " + id));
        driverRepository.delete(driver);
    }

    @Override
    @Transactional
    public BulkDeleteResponseDTO deleteDrivers(List<Long> ids) {
        /*for (Long id : ids) {
            Driver driver = driverRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Driver no encontrado con id: " + id));
            driverRepository.delete(driver);
        }*/
        List<Driver> foundDrivers = driverRepository.findAllById(ids);
        // Extraemos los IDs encontrados
        List<Long> foundIds = foundDrivers.stream()
                .map(Driver::getUserId)
                .toList();
        // Comparamos los IDs solicitados con los encontrados
        List<Long> notFoundIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        for (Driver driver : foundDrivers) {
            driverRepository.delete(driver);
        }

        return new BulkDeleteResponseDTO(foundIds, notFoundIds);
    }
}
