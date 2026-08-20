package com.ownProject.GINS.wareHouse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Ware_House APIs")
public class WareHouseController {

	private final WareHouseService wareHouseService;

	public WareHouseController(WareHouseService wareHouseService) {
		this.wareHouseService = wareHouseService;
	}

	@GetMapping("/warehouses")
	@Operation(summary = "get all warehouses")
	public List<WareHouse> knowAllWareHouses() {
		return wareHouseService.getAllWh();
	}
	
	@GetMapping("/warehouses/{id}")
	@Operation(summary = "get warehouse by its ID")
	public EntityModel<WareHouse> getWh(@PathVariable int id) {
		WareHouse wh = wareHouseService.getWhFromId(id);
		
		EntityModel<WareHouse> entityModel = EntityModel.of(wh);
		entityModel.add(linkTo(methodOn(WareHouseController.class).knowAllWareHouses()).withRel("all-wareHouses"));
		entityModel.add(linkTo(methodOn(WareHouseController.class).getWh(id)).withSelfRel());
		entityModel.add(linkTo(methodOn(WareHouseController.class).builtNewWareHouse(null)).withRel("create-wareHouse"));
		entityModel.add(linkTo(methodOn(WareHouseController.class).updateWareHouse(id, null)).withRel("update-wareHouse"));

		return entityModel;
	}
	
	@PostMapping("/warehouses")
	@Operation(summary = "add new warehouse")
	public ResponseEntity<Object> builtNewWareHouse(@RequestBody WareHouseDTO warehouseDto) {
		WareHouse builtWh = wareHouseService.builtNewWh(warehouseDto);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(builtWh.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/warehouses/{id}")
	@Operation(summary = "change details about warehouse")
	public ResponseEntity<WareHouse> updateWareHouse(@PathVariable int id, @RequestBody WareHouseDTO warehouseDto) {
		WareHouse updatedWrh = wareHouseService.updateWh(id, warehouseDto);
		return ResponseEntity.ok(updatedWrh);
	}
}
