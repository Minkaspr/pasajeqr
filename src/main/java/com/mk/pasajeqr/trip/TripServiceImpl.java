package com.mk.pasajeqr.trip;

import com.mk.pasajeqr.bus.Bus;
import com.mk.pasajeqr.bus.BusRepository;
import com.mk.pasajeqr.common.exception.DuplicateResourceException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.trip.request.TripCreateRQ;
import com.mk.pasajeqr.trip.request.TripUpdateRQ;
import com.mk.pasajeqr.trip.response.TripDetailRS;
import com.mk.pasajeqr.trip.response.TripItemRS;
import com.mk.pasajeqr.trip.response.TripRS;
import com.mk.pasajeqr.stop.Stop;
import com.mk.pasajeqr.stop.StopRepository;
import com.mk.pasajeqr.user.User;
import com.mk.pasajeqr.user.UserRepository;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import com.mk.pasajeqr.utils.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository serviceRepository;
    @Autowired
    private BusRepository busRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StopRepository stopRepository;

    @Override
    public TripRS listPaged(Pageable pageable) {
        Page<Trip> page = serviceRepository.findAll(pageable);
        List<TripItemRS> content = page.getContent().stream()
                .map(TripItemRS::new).toList();
        return new TripRS(content, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    @Transactional
    public TripDetailRS create(TripCreateRQ request) {
        if (serviceRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Ya existe un servicio con ese código");
        }

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus no encontrado"));
        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
        Stop origin = stopRepository.findById(request.getOriginStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de origen no encontrado"));
        Stop destination = stopRepository.findById(request.getDestinationStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de destino no encontrado"));

        LocalDate departureDate = request.getDepartureDate();
        LocalTime departureTime = request.getDepartureTime();
        LocalDate arrivalDate = request.getArrivalDate();
        LocalTime arrivalTime = request.getArrivalTime();

        if (arrivalDate != null && arrivalTime != null) {
            LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);
            LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate, arrivalTime);

            if (!arrivalDateTime.isAfter(departureDateTime)) {
                throw new IllegalArgumentException("La fecha y hora de llegada deben ser posteriores a la de salida.");
            }
        }

        Trip trip = Trip.builder()
                .code(request.getCode())
                .bus(bus)
                .driver(driver)
                .originStop(origin)
                .destinationStop(destination)
                .departureDate(departureDate)
                .departureTime(departureTime)
                .arrivalDate(arrivalDate)
                .arrivalTime(arrivalTime)
                .status(ServiceStatus.SCHEDULED)
                .build();

        return new TripDetailRS(serviceRepository.save(trip));
    }

    @Override
    public TripDetailRS getById(Long id) {
        Trip service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        return new TripDetailRS(service);
    }

    @Override
    @Transactional
    public TripDetailRS update(Long id, TripUpdateRQ request) {
        Trip service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        if (!service.getCode().equals(request.getCode()) && serviceRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Ya existe un servicio con ese código");
        }

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus no encontrado"));
        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
        Stop origin = stopRepository.findById(request.getOriginStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de origen no encontrado"));
        Stop destination = stopRepository.findById(request.getDestinationStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de destino no encontrado"));

        LocalDate departureDate = request.getDepartureDate();
        LocalTime departureTime = request.getDepartureTime();
        LocalDate arrivalDate = request.getArrivalDate();
        LocalTime arrivalTime = request.getArrivalTime();

        if (arrivalDate != null && arrivalTime != null) {
            LocalDateTime departureDT = LocalDateTime.of(departureDate, departureTime);
            LocalDateTime arrivalDT = LocalDateTime.of(arrivalDate, arrivalTime);

            if (!arrivalDT.isAfter(departureDT)) {
                throw new IllegalArgumentException("La fecha y hora de llegada deben ser posteriores a la de salida.");
            }
        }

        service.setCode(request.getCode());
        service.setBus(bus);
        service.setDriver(driver);
        service.setOriginStop(origin);
        service.setDestinationStop(destination);
        service.setDepartureDate(departureDate);
        service.setDepartureTime(departureTime);
        service.setArrivalDate(arrivalDate);
        service.setArrivalTime(arrivalTime);
        service.setStatus(request.getStatus());

        return new TripDetailRS(serviceRepository.save(service));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Trip service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        serviceRepository.delete(service);
    }

    @Override
    @Transactional
    public BulkDeleteRS deleteBulk(List<Long> ids) {
        List<Trip> found = serviceRepository.findAllById(ids);
        List<Long> foundIds = found.stream().map(Trip::getId).toList();
        List<Long> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();
        found.forEach(serviceRepository::delete);
        return new BulkDeleteRS(foundIds, notFoundIds);
    }
}
