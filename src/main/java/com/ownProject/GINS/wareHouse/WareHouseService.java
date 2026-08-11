package com.ownProject.GINS.wareHouse;

import com.ownProject.GINS.dto.WareHouseDTO;
import com.ownProject.GINS.jpa.WareHouseRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class WareHouseService {

    private WareHouseRepository wareHouseRepository;

    public List<WareHouse> getAllWh() {
        return wareHouseRepository.findAll();
    }

    public EntityModel<WareHouse> getWhFromId(int id) {
        Optional<WareHouse> wh = wareHouseRepository.findById(id);

        if(wh.isEmpty()) {
            throw new NoSuchElementException("WareHouse doesn't exist with id : " + id);
        }

        EntityModel<WareHouse> entityModel = EntityModel.of(wh.get());

        entityModel.add(linkTo(methodOn(this.getClass()).getAllWh()).withRel("all-wareHouses"));
        entityModel.add(linkTo(methodOn(this.getClass()).getWhFromId(id)).withRel("get-wareHouse"));
        entityModel.add(linkTo(methodOn(this.getClass()).builtNewWh(null)).withRel("built-wareHouse"));
        entityModel.add(linkTo(methodOn(this.getClass()).updateWh(id, null)).withRel("built-wareHouse"));

        return entityModel;
    }

    public ResponseEntity<Object> builtNewWh(WareHouseDTO warehouseDto) {
        WareHouse wh = new WareHouse();
        wh.setName(warehouseDto.getName());
        wh.setLocationCity(warehouseDto.getLocationCity());
        wh.setCountryCode(warehouseDto.getCountryCode());
        wh.setIs_active(warehouseDto.getIs_active());

        WareHouse builtWh = wareHouseRepository.save(wh);

        URI Location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(builtWh.getId()).toUri();
        return ResponseEntity.created(Location).build();
    }

    public ResponseEntity<WareHouse> updateWh(int id, WareHouseDTO warehouseDto) {
        WareHouse updatedWrh = wareHouseRepository.findById(id).map(existingWh -> {

            existingWh.setName(warehouseDto.getName());
            existingWh.setLocationCity(warehouseDto.getLocationCity());
            existingWh.setCountryCode(warehouseDto.getCountryCode());
            existingWh.setIs_active(warehouseDto.getIs_active());

            return wareHouseRepository.save(existingWh);
        }).orElseThrow(() -> new NoSuchElementException("WareHouse not found with id " + id));

        return ResponseEntity.ok(updatedWrh); // Wrap in ResponseEntity
    }
}
