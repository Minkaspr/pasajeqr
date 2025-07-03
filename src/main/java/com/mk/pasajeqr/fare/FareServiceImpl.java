package com.mk.pasajeqr.fare;

import com.mk.pasajeqr.common.exception.DuplicateResourceException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.fare.request.FareCreateRQ;
import com.mk.pasajeqr.fare.request.FareUpdateRQ;
import com.mk.pasajeqr.fare.response.FareDetailRS;
import com.mk.pasajeqr.fare.response.FareItemRS;
import com.mk.pasajeqr.fare.response.FaresRS;
import com.mk.pasajeqr.stop.Stop;
import com.mk.pasajeqr.stop.StopRepository;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FareServiceImpl implements FareService {

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private StopRepository stopRepository;

    @Override
    public FaresRS listPaged(String search, Pageable pageable) {
        Page<Fare> page;
        if (search != null && !search.trim().isEmpty()) {
            page = fareRepository.findByCodeContainingIgnoreCase(search.trim(), pageable);
        } else {
            page = fareRepository.findAll(pageable);
        }
        List<FareItemRS> fares = page.getContent().stream()
                .map(FareItemRS::new)
                .toList();

        return new FaresRS(fares, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    @Transactional
    public FareDetailRS create(FareCreateRQ request) {
        if (fareRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Ya existe una tarifa con ese código");
        }

        Stop origin = stopRepository.findById(request.getOriginStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de origen no encontrado"));

        Stop destination = stopRepository.findById(request.getDestinationStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de destino no encontrado"));

        Fare fare = Fare.builder()
                .code(request.getCode())
                .originStop(origin)
                .destinationStop(destination)
                .price(request.getPrice())
                .build();

        return new FareDetailRS(fareRepository.save(fare));
    }

    @Override
    public FareDetailRS getById(Long id) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa no encontrada"));
        return new FareDetailRS(fare);
    }

    @Override
    @Transactional
    public FareDetailRS update(Long id, FareUpdateRQ request) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa no encontrada"));

        if (!fare.getCode().equals(request.getCode()) && fareRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Ya existe una tarifa con ese código");
        }

        Stop origin = stopRepository.findById(request.getOriginStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de origen no encontrado"));

        Stop destination = stopRepository.findById(request.getDestinationStopId())
                .orElseThrow(() -> new ResourceNotFoundException("Paradero de destino no encontrado"));

        fare.setCode(request.getCode());
        fare.setOriginStop(origin);
        fare.setDestinationStop(destination);
        fare.setPrice(request.getPrice());

        return new FareDetailRS(fareRepository.save(fare));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa no encontrada con ID: " + id));
        fareRepository.delete(fare);
    }

    @Override
    @Transactional
    public BulkDeleteRS deleteBulk(List<Long> ids) {
        List<Fare> foundFares = fareRepository.findAllById(ids);
        List<Long> foundIds = foundFares.stream()
                .map(Fare::getId)
                .toList();

        List<Long> notFoundIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        for (Fare fare : foundFares) {
            fareRepository.delete(fare);
        }

        return new BulkDeleteRS(foundIds, notFoundIds);
    }
}

