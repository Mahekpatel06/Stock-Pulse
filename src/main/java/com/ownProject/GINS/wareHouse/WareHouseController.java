package com.ownProject.GINS.wareHouse;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/warehouses")
	public ResponseEntity<Object> builtNewWareHouse(@RequestBody WareHouse warehouse) {
		WareHouse builtWh = wareHouseRepository.save(warehouse);
		
		URI Location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(builtWh.getId()).toUri();
		return ResponseEntity.created(Location).build();
		
	}
}