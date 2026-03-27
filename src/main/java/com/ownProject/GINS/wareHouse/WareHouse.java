package com.ownProject.GINS.wareHouse;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class WareHouse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
//	@JsonProperty("locationCity")
	private String locationCity;
	private String countryCode;
	
	@CreationTimestamp
	@TimeZoneStorage(TimeZoneStorageType.AUTO)
	private LocalDateTime timeZone;
	
	private Boolean is_active;
	
	public WareHouse() {
		
	}
 	
	public WareHouse(Integer id, String name, String locationCity, String countryCode, LocalDateTime timeZone,
			Boolean is_active) {
		super();
		this.id = id;
		this.name = name;
		this.locationCity = locationCity;
		this.countryCode = countryCode;
		this.timeZone = timeZone;
		this.is_active = is_active;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocationCity() {
		return locationCity;
	}
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Boolean getIs_active() {
		return is_active;
	}
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}
	public LocalDateTime getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(LocalDateTime timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public String toString() {
		return "WareHouse [id=" + id + ", name=" + name + ", locationCity=" + locationCity + ", countryCode="
				+ countryCode + ", timeZone=" + timeZone + ", is_active=" + is_active + "]";
	}
	
	
}
