package com.ownProject.GINS.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WareHouseDTO {

	@NotBlank(message = "Warehouse name is required")
	public String name;

	@NotBlank(message = "Location city is required")
	public String locationCity;

	@NotBlank(message = "Country code is required")
	@Size(min = 2, max = 3, message = "Country code must be 2 or 3 characters")
	public String countryCode;

	@NotNull(message = "is_active status is required")
	public Boolean is_active;

	@NotBlank(message = "Contact email is required")
	@Email(message = "Please provide a valid email address")
	public String contactEmail;
	
	public WareHouseDTO() {
		super();
	}

	public WareHouseDTO(String name, String locationCity, String countryCode, Boolean is_active, String contactEmail) {
		super();
		this.name = name;
		this.locationCity = locationCity;
		this.countryCode = countryCode;
		this.is_active = is_active;
		this.contactEmail = contactEmail;
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

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Override
	public String toString() {
		return "WareHouseDTO [name=" + name + ", locationCity=" + locationCity + ", countryCode=" + countryCode
				+ ", is_active=" + is_active + ", contactEmail=" + contactEmail + "]";
	}

}
