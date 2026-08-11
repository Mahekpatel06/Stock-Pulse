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

import com.ownProject.GINS.dto.WareHouseDTO;
import com.ownProject.GINS.jpa.WareHouseRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Ware_House APIs")
public class WareHouseController {

	private WareHouseService wareHouseService;

	public WareHouseController(WareHouseService wareHouseService) {
		super();
		this.wareHouseService = wareHouseService;
	}

	@GetMapping("/warehouses")
	@Operation(summary = "get all warehouses")
	public List<WareHouse> knowAllWareHouses() {

//		return "Here are all the Ware Houses..!!";
		return wareHouseService.getAllWh();
	}
	
	@GetMapping("/warehouses/{id}")
	@Operation(summary = "get warehouse by its ID")
	public EntityModel<WareHouse> getWh(@PathVariable int id) {

		return wareHouseService.getWhFromId(id);
	}
	
	@PostMapping("/warehouses")
	@Operation(summary = "add new warehouse")
	public ResponseEntity<Object> builtNewWareHouse(@RequestBody WareHouseDTO warehouseDto) {

		return wareHouseService.builtNewWh(warehouseDto);
	}
	
	@PutMapping("/warehouses/{id}")
	@Operation(summary = "change details about warehouse")
	public ResponseEntity<WareHouse> updateWareHouse(@PathVariable int id, @RequestBody WareHouseDTO warehouseDto) {

		return wareHouseService.updateWh(id, warehouseDto);
	}
}
