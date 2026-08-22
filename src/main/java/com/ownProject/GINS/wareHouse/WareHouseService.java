package com.ownProject.GINS.wareHouse;

import com.ownProject.GINS.dto.WareHouseDTO;
import com.ownProject.GINS.jpa.WareHouseRepository;
import com.ownProject.GINS.exception.customExpClasses.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WareHouseService {

    private final WareHouseRepository wareHouseRepository;

    public WareHouseService(WareHouseRepository wareHouseRepository) {
        this.wareHouseRepository = wareHouseRepository;
    }

    public List<WareHouse> getAllWh() {
        return wareHouseRepository.findAll();
    }

    @Cacheable(value = "warehouses", key = "#id")
    public WareHouse getWhFromId(int id) {
        return wareHouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WareHouse doesn't exist with id: " + id));
    }

    public WareHouse builtNewWh(WareHouseDTO warehouseDto) {
        WareHouse wh = new WareHouse();
        wh.setName(warehouseDto.getName());
        wh.setLocationCity(warehouseDto.getLocationCity());
        wh.setCountryCode(warehouseDto.getCountryCode());
        wh.setIs_active(warehouseDto.getIs_active());
        wh.setContactEmail(warehouseDto.getContactEmail());

        return wareHouseRepository.save(wh);
    }

    @CachePut(value = "warehouses", key = "#id")
    public WareHouse updateWh(int id, WareHouseDTO warehouseDto) {
        return wareHouseRepository.findById(id).map(existingWh -> {
            existingWh.setName(warehouseDto.getName());
            existingWh.setLocationCity(warehouseDto.getLocationCity());
            existingWh.setCountryCode(warehouseDto.getCountryCode());
            existingWh.setIs_active(warehouseDto.getIs_active());
            existingWh.setContactEmail(warehouseDto.getContactEmail());

            return wareHouseRepository.save(existingWh);
        }).orElseThrow(() -> new ResourceNotFoundException("WareHouse not found with id " + id));
    }
}
