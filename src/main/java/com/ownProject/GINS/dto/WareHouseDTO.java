package com.ownProject.GINS.dto;

public class WareHouseDTO {

	public String name;
	public String locationCity;
	public String countryCode;
	public Boolean is_active;
	
	public WareHouseDTO(String name, String locationCity, String countryCode, Boolean is_active) {
		super();
		this.name = name;
		this.locationCity = locationCity;
		this.countryCode = countryCode;
		this.is_active = is_active;
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

	@Override
	public String toString() {
		return "WareHouseDTO [name=" + name + ", locationCity=" + locationCity + ", countryCode=" + countryCode
				+ ", is_active=" + is_active + "]";
	}

}
