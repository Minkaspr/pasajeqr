package com.mk.pasajeqr.bus;

import com.mk.pasajeqr.bus.request.BusCreateRQ;
import com.mk.pasajeqr.bus.request.BusUpdateRQ;
import com.mk.pasajeqr.bus.response.BusDetailRS;
import com.mk.pasajeqr.bus.response.BusItemRS;
import com.mk.pasajeqr.bus.response.BusesRS;
import com.mk.pasajeqr.common.exception.DuplicateResourceException;
import com.mk.pasajeqr.common.exception.ResourceNotFoundException;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public BusesRS listPaged(Pageable pageable) {
        Page<Bus> page = busRepository.findAll(pageable);
        List<BusItemRS> busList = page.getContent().stream().map(BusItemRS::new).toList();

        return new BusesRS(busList, page.getNumber(), page.getTotalPages(), page.getTotalElements());
    }

    @Override
    @Transactional
    public BusDetailRS create(BusCreateRQ request) {
        if (busRepository.existsByPlate(request.getPlate())) {
            throw new DuplicateResourceException("Ya existe un bus con esa placa.");
        }

        Bus bus = Bus.builder()
                .plate(request.getPlate())
                .model(request.getModel())
                .capacity(request.getCapacity())
                .status(request.getStatus())
                .build();

        return new BusDetailRS(busRepository.save(bus));
    }

    @Override
    public BusDetailRS getById(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus no encontrado con ID: " + id));
        return new BusDetailRS(bus);
    }

    @Override
    @Transactional
    public BusDetailRS update(Long id, BusUpdateRQ request) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus no encontrado con ID: " + id));

        if (!bus.getPlate().equals(request.getPlate()) && busRepository.existsByPlate(request.getPlate())) {
            throw new DuplicateResourceException("Ya existe un bus con esa placa.");
        }

        bus.setPlate(request.getPlate());
        bus.setModel(request.getModel());
        bus.setCapacity(request.getCapacity());
        bus.setStatus(request.getStatus());

        return new BusDetailRS(busRepository.save(bus));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus no encontrado con ID: " + id));
        busRepository.delete(bus);
    }

    @Override
    @Transactional
    public BulkDeleteRS deleteBulk(List<Long> ids) {
        List<Bus> found = busRepository.findAllById(ids);
        List<Long> foundIds = found.stream().map(Bus::getId).toList();
        List<Long> notFound = ids.stream().filter(id -> !foundIds.contains(id)).toList();
        found.forEach(busRepository::delete);
        return new BulkDeleteRS(foundIds, notFound);
    }
}
