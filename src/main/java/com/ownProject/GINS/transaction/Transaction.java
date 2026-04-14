package com.ownProject.GINS.transaction;

import java.time.LocalDateTime;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ownProject.GINS.inventory.Inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "inventory_id")
	private Inventory inventory;
	
	public enum Type {INBOUND, OUTBOUND, ADJUSTMENT, TRANSFER}
	
	@Enumerated(EnumType.STRING)
	public Type type;
	
	@NotNull
	private Integer qtyChange;
	
	private String reason;
	
	@NotNull
	@JsonFormat(pattern = "dd-MMMM-yyyy HH:mm:ss")
	@TimeZoneStorage(TimeZoneStorageType.AUTO)
	private LocalDateTime createdAt;
	
	public Transaction() {
		super();
	}

	public Transaction(Integer id, Inventory inventory, Type type, @NotNull Integer qtyChange, String reason,
			@NotNull LocalDateTime createdAt) {
		super();
		this.id = id;
		this.inventory = inventory;
		this.type = type;
		this.qtyChange = qtyChange;
		this.reason = reason;
		this.createdAt = createdAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getQtyChange() {
		return qtyChange;
	}

	public void setQtyChange(Integer qtyChange) {
		this.qtyChange = qtyChange;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", inventory=" + inventory + ", type=" + type + ", qtyChange=" + qtyChange
				+ ", reason=" + reason + ", createdAt=" + createdAt + "]";
	}

	
}
