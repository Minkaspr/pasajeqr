package com.mk.pasajeqr.trip;

import com.mk.pasajeqr.bus.Bus;
import com.mk.pasajeqr.bus.BusRepository;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.trip.request.TripCreateRQ;
import com.mk.pasajeqr.trip.request.TripUpdateRQ;
import com.mk.pasajeqr.trip.response.TripDetailRS;
import com.mk.pasajeqr.trip.response.TripEditRS;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private BusRepository busRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private TripQrJwtService tripQrJwtService;
    @Autowired
    private TripQrSimpleTokenService tripQrSimpleTokenService;

    @Override
    public TripRS listPaged(Pageable pageable, String codeFilter) {
        Page<Trip> page;

        if (codeFilter != null && !codeFilter.trim().isEmpty()) {
            page = tripRepository.findByCodeContainingIgnoreCase(codeFilter.trim(), pageable);
        } else {
            page = tripRepository.findAll(pageable);
        }
        List<TripItemRS> content = page.getContent().stream()
                .map(TripItemRS::new).toList();
        return new TripRS(content, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    @Transactional
    public TripDetailRS create(TripCreateRQ request) {
        String generatedCode = generateTripCode();

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
                .code(generatedCode)
                .bus(bus)
                .driver(driver)
                .originStop(origin)
                .destinationStop(destination)
                .departureDate(departureDate)
                .departureTime(departureTime)
                .arrivalDate(arrivalDate)
                .arrivalTime(arrivalTime)
                .status(request.getStatus())
                .build();

        return new TripDetailRS(tripRepository.save(trip));
    }

    @Override
    public TripEditRS getById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        return new TripEditRS(
                trip.getId(),
                trip.getBus().getId(),
                trip.getDriver().getId(),
                trip.getOriginStop().getId(),
                trip.getDestinationStop().getId(),
                trip.getDepartureDate(),
                trip.getDepartureTime(),
                trip.getArrivalDate(),
                trip.getArrivalTime(),
                trip.getStatus(),
                trip.getCode()
        );
    }

    @Override
    @Transactional
    public TripDetailRS update(Long id, TripUpdateRQ request) {
        Trip service = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

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

        service.setBus(bus);
        service.setDriver(driver);
        service.setOriginStop(origin);
        service.setDestinationStop(destination);
        service.setDepartureDate(departureDate);
        service.setDepartureTime(departureTime);
        service.setArrivalDate(arrivalDate);
        service.setArrivalTime(arrivalTime);
        service.setStatus(request.getStatus());

        return new TripDetailRS(tripRepository.save(service));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Trip service = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        tripRepository.delete(service);
    }

    @Override
    @Transactional
    public BulkDeleteRS deleteBulk(List<Long> ids) {
        List<Trip> found = tripRepository.findAllById(ids);
        List<Long> foundIds = found.stream().map(Trip::getId).toList();
        List<Long> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();
        found.forEach(tripRepository::delete);
        return new BulkDeleteRS(foundIds, notFoundIds);
    }

    @Override
    public String generateQrToken(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        if (trip.getStatus() == ServiceStatus.CANCELED) {
            throw new IllegalStateException("No se puede generar un QR para un servicio cancelado");
        }

        if(trip.getStatus() == ServiceStatus.COMPLETED) {
            throw new IllegalStateException("No se puede generar un QR para un servicio completado");
        }

        //return tripQrJwtService.generateToken(trip);
        return tripQrSimpleTokenService.encryptTripId(tripId);
    }

    @Override
    public Map<String, Object> validateQrToken(String token) {
        Long tripId;
        try {
            // tripId = tripQrJwtService.validateTokenAndGetTripId(token);
            tripId = tripQrSimpleTokenService.decryptToken(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("El código QR no es válido o ha expirado");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("El viaje asociado al QR no existe"));

        if (trip.getStatus() == ServiceStatus.CANCELED) {
            throw new IllegalStateException("Este servicio ha sido cancelado");
        }

        if (trip.getStatus() == ServiceStatus.COMPLETED) {
            throw new IllegalStateException("Este servicio ya fue completado");
        }

        // Si todo va bien, devolver un map con la información
        Map<String, Object> response = new HashMap<>();
        response.put("tripId", trip.getId());
        response.put("tripCode", trip.getCode());
        response.put("status", trip.getStatus());

        return response;
    }

    public String generateTripCode() {
        String uuidPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "TRIP-" + uuidPart;
    }
}
