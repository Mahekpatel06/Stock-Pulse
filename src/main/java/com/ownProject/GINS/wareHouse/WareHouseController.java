package com.ownProject.GINS.wareHouse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ownProject.GINS.jpa.WareHouseRepository;

@RestController
public class WareHouseController {

	private WareHouseRepository wareHouseRepository;

	public WareHouseController(WareHouseRepository wareHouseRepository) {
		super();
		this.wareHouseRepository = wareHouseRepository;
	}

	@GetMapping("/warehouses")
	public List<WareHouse> knowAllWareHouses() {
//		return "Here are all the Ware Houses..!!";
		return wareHouseRepository.findAll();
	}
	
	@GetMapping("/warehouses/{id}")
	public EntityModel<WareHouse> getWh(@PathVariable int id) {
		
		Optional<WareHouse> wh = wareHouseRepository.findById(id);
		
		if(wh.isEmpty()) {
			throw new NoSuchElementException("WareHouse doesn't exist with id : " + id);
		}
		
		EntityModel<WareHouse> entityModel = EntityModel.of(wh.get());
		
		entityModel.add(linkTo(methodOn(this.getClass()).knowAllWareHouses()).withRel("all-wareHouses"));
		entityModel.add(linkTo(methodOn(this.getClass()).getWh(id)).withRel("get-wareHouse"));
		entityModel.add(linkTo(methodOn(this.getClass()).builtNewWareHouse(null)).withRel("built-wareHouse"));
		entityModel.add(linkTo(methodOn(this.getClass()).updateWh(id, null)).withRel("built-wareHouse"));
		
		return entityModel;
		
	}
	
	@PostMapping("/warehouses")
	public ResponseEntity<Object> builtNewWareHouse(@RequestBody WareHouse warehouse) {
		WareHouse builtWh = wareHouseRepository.save(warehouse);
		
		URI Location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(builtWh.getId()).toUri();
		return ResponseEntity.created(Location).build();
		
	}
	
	@PutMapping("/warehouses/{id}")
	public ResponseEntity<WareHouse> updateWh(@PathVariable int id, @RequestBody WareHouse warehouse) {
		
		WareHouse updatedWh = wareHouseRepository.findById(id).map(existingWh -> {

			existingWh.setName(warehouse.getName());
			existingWh.setLocationCity(warehouse.getLocationCity());
			existingWh.setCountryCode(warehouse.getCountryCode());
			existingWh.setIs_active(warehouse.getIs_active());

			return wareHouseRepository.save(existingWh);
		}).orElseThrow(() -> new NoSuchElementException("WareHouse not found with id " + id));
		
		return ResponseEntity.ok(updatedWh); // Wrap in ResponseEntity
	}
}