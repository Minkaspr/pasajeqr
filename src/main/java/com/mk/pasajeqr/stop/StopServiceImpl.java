package com.mk.pasajeqr.stop;

import com.mk.pasajeqr.common.exception.DuplicateResourceException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.stop.request.StopCreateRQ;
import com.mk.pasajeqr.stop.request.StopUpdateRQ;
import com.mk.pasajeqr.stop.response.StopDetailRS;
import com.mk.pasajeqr.stop.response.StopItemRS;
import com.mk.pasajeqr.stop.response.StopsRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopServiceImpl implements StopService {

    @Autowired
    private StopRepository stopRepository;

    @Override
    public StopsRS listPaged(String search, Pageable pageable) {
        Page<Stop> page;

        if (search == null || search.isBlank()) {
            page = stopRepository.findAll(pageable);
        } else {
            page = stopRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        }

        List<StopItemRS> stops = page.getContent().stream()
                .map(StopItemRS::new)
                .toList();

        return new StopsRS(stops, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    public StopsRS listTerminalStops(Pageable pageable) {
        Page<Stop> page = stopRepository.findByTerminalTrue(pageable);
        List<StopItemRS> stops = page.getContent().stream()
                .map(StopItemRS::new)
                .toList();

        return new StopsRS(
                stops,
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Override
    public StopDetailRS create(StopCreateRQ request) {
        if (stopRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Ya existe un paradero con ese nombre");
        }

        Stop stop = Stop.builder()
                .name(request.getName())
                .terminal(request.isTerminal())
                .build();

        Stop saved = stopRepository.save(stop);

        return new StopDetailRS(saved);
    }

    @Override
    public StopDetailRS getById(Long id) {
        Stop stop = stopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paradero no encontrado con ID: " + id));
        return new StopDetailRS(stop);
    }

    @Override
    public StopDetailRS update(Long id, StopUpdateRQ request) {
        Stop stop = stopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paradero no encontrado con ID: " + id));

        if (!stop.getName().equals(request.getName()) && stopRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Ya existe un paradero con ese nombre");
        }

        stop.setName(request.getName());
        stop.setTerminal(request.isTerminal());

        return new StopDetailRS(stopRepository.save(stop));
    }

    @Override
    public void delete(Long id) {
        Stop stop = stopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paradero no encontrado con ID: " + id));
        stopRepository.delete(stop);
    }

    @Override
    public BulkDeleteRS deleteBulk(List<Long> ids) {
        List<Stop> foundStops = stopRepository.findAllById(ids);

        List<Long> foundIds = foundStops.stream()
                .map(Stop::getId)
                .toList();

        List<Long> notFoundIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        foundStops.forEach(stopRepository::delete);

        return new BulkDeleteRS(foundIds, notFoundIds);
    }
}

