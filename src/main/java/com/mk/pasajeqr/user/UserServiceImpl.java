package com.mk.pasajeqr.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.pasajeqr.admin.Admin;
import com.mk.pasajeqr.admin.AdminRepository;
import com.mk.pasajeqr.common.exception.BadRequestException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.common.exception.RoleDataConstraintViolationException;
import com.mk.pasajeqr.driver.Driver;
import com.mk.pasajeqr.driver.DriverRepository;
import com.mk.pasajeqr.passenger.Passenger;
import com.mk.pasajeqr.passenger.PassengerRepository;
import com.mk.pasajeqr.passenger.response.PassengerLookupRS;
import com.mk.pasajeqr.user.dto.RegisterDTO;
import com.mk.pasajeqr.user.request.UserRegisterRequest;
import com.mk.pasajeqr.utils.RoleType;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public User createUser(UserRegisterRequest request) {
        validarRoleData(request);
        RegisterDTO userData = request.getUserData();
        String encryptedPassword = passwordEncoder.encode(userData.getPassword());
        User user = User.builder()
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .dni(userData.getDni())
                .email(userData.getEmail())
                .password(encryptedPassword)
                .role(userData.getRole())
                .status(true)
                .build();

        User newUser = userRepository.save(user);

        try {
            switch (user.getRole()) {
                case PASSENGER -> {
                    Passenger passenger = objectMapper.treeToValue(request.getRoleData(), Passenger.class);
                    passenger.setUser(newUser);
                    passengerRepository.save(passenger);
                }
                case DRIVER -> {
                    Driver driver = objectMapper.treeToValue(request.getRoleData(), Driver.class);
                    driver.setUser(newUser);
                    driverRepository.save(driver);
                }
                case ADMIN -> {
                    Admin admin = objectMapper.treeToValue(request.getRoleData(), Admin.class);
                    admin.setUser(newUser);
                    adminRepository.save(admin);
                }
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error al convertir roleData al tipo correspondiente", e);
        }
        return newUser;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public PassengerLookupRS findPassengerByDni(String dni) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con DNI: " + dni));

        if (user.getRole() != RoleType.PASSENGER) {
            throw new BadRequestException("El usuario no es un pasajero");
        }

        Passenger passenger = passengerRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró información del pasajero"));

        return new PassengerLookupRS(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getDni(),
                user.getEmail(),
                passenger.getBalance(),
                user.getStatus()
        );
    }

    private void validarRoleData(UserRegisterRequest request) {
        RoleType role = request.getUserData().getRole();
        JsonNode roleData = request.getRoleData();

        // Validar que si hay un rol, debe haber roleData no nulo y no vacío
        if (role != null && roleData == null) {
            throw new IllegalArgumentException("roleData no puede ser nulo cuando se especifica un rol");
        }

        if (role == null) {
            // Si no hay rol, no se valida nada más
            return;
        }

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<Object>> violations = switch (role) {
                case PASSENGER -> {
                    try {
                        Passenger passenger = objectMapper.treeToValue(roleData, Passenger.class);
                        yield validator.validate(passenger);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("roleData no tiene el formato esperado para PASSENGER", e);
                    }
                }
                case DRIVER -> {
                    try {
                        Driver driver = objectMapper.treeToValue(roleData, Driver.class);
                        yield validator.validate(driver);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("roleData no tiene el formato esperado para DRIVER", e);
                    }
                }
                case ADMIN -> {
                    try {
                        Admin admin = objectMapper.treeToValue(roleData, Admin.class);
                        yield validator.validate(admin);
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException("roleData no tiene el formato esperado para ADMIN", e);
                    }
                }
            };

            if (!violations.isEmpty()) {
                throw new RoleDataConstraintViolationException("Errores en datos del rol", (Set<ConstraintViolation<?>>)(Set<?>) violations);
            }
        }
    }
}
