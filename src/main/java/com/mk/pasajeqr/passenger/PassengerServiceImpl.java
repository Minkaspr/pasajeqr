package com.mk.pasajeqr.passenger;

import com.mk.pasajeqr.common.exception.DuplicateResourceException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.passenger.request.PassengerCreateRQ;
import com.mk.pasajeqr.passenger.request.PassengerUpdateRQ;
import com.mk.pasajeqr.passenger.response.PassengerDetailRS;
import com.mk.pasajeqr.passenger.response.PassengerUserItemRS;
import com.mk.pasajeqr.passenger.response.PassengersRS;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import com.mk.pasajeqr.utils.ChangePasswordRQ;
import com.mk.pasajeqr.utils.Role;
import com.mk.pasajeqr.utils.UserStatusRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService{

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PassengersRS getAllPaged(Pageable pageable) {
        Page<Passenger> page = passengerRepository.findAll(pageable);

        List<PassengerUserItemRS> passengerList = page.getContent().stream()
                .map(PassengerUserItemRS::new)
                .toList();

        return new PassengersRS(
                passengerList,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    @Transactional
    public PassengerDetailRS create(PassengerCreateRQ request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese email");
        }

        if (userRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese DNI");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dni(request.getDni())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PASSENGER)
                .status(true)
                .build();

        User savedUser = userRepository.save(user);

        Passenger passenger = Passenger.builder()
                .user(savedUser)
                .balance(request.getBalance())
                .build();

        Passenger savedPassenger = passengerRepository.save(passenger);

        return new PassengerDetailRS(savedPassenger);
    }

    @Override
    public PassengerDetailRS getById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + id));
        return new PassengerDetailRS(passenger);
    }

    @Override
    @Transactional
    public PassengerDetailRS update(Long id, PassengerUpdateRQ request) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + id));

        User user = passenger.getUser();

        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese email");
        }

        if (!user.getDni().equals(request.getDni()) &&
                userRepository.existsByDni(request.getDni())) {
            throw new DuplicateResourceException("Ya existe un usuario con ese DNI");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDni(request.getDni());
        user.setEmail(request.getEmail());
        user.setStatus(request.getUserStatus());

        passenger.setBalance(request.getBalance());

        Passenger updatedPassenger = passengerRepository.save(passenger);

        return new PassengerDetailRS(updatedPassenger);
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRQ request) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + id));

        User user = passenger.getUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual no es correcta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserStatusRS setUserStatus(Long id, boolean active) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + id));

        User user = passenger.getUser();

        if (user.getStatus() == active) {
            String estado = active ? "activada" : "desactivada";
            throw new IllegalStateException("La cuenta ya está " + estado + ".");
        }

        user.setStatus(active);
        userRepository.save(user);

        return new UserStatusRS(user.getId(), active);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado con ID: " + id));
        passengerRepository.delete(passenger);
    }

    @Override
    @Transactional
    public BulkDeleteRS deleteBulk(List<Long> ids) {
        List<Passenger> foundPassengers = passengerRepository.findAllById(ids);

        List<Long> foundIds = foundPassengers.stream()
                .map(Passenger::getUserId)
                .toList();

        List<Long> notFoundIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        for (Passenger passenger : foundPassengers) {
            passengerRepository.delete(passenger);
        }

        return new BulkDeleteRS(foundIds, notFoundIds);
    }
}
