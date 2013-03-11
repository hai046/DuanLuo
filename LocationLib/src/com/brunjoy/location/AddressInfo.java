package com.brunjoy.location;

public class AddressInfo {

	private String country;
	private String country_code;
	private String region;
	private String city;
	private String street;
	private String street_number;
	public String getCountry() {
		return country;
	}
	public String getCountry_code() {
		return country_code;
	}
	public String getRegion() {
		return region;
	}
	public String getCity() {
		return city;
	}
	public String getStreet() {
		return street;
	}
	public String getStreet_number() {
		return street_number;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}
	@Override
	public String toString() {
		return "AddressInfo [country=" + country + ", country_code="
				+ country_code + ", region=" + region + ", city=" + city
				+ ", street=" + street + ", street_number=" + street_number
				+ "]";
	}
	public String toShortString()
	{
		return country+" "+region;
	}
	public String toDetailAddress()
	{
		return country+" "+region+" "+city+" "+street+" "+street_number;
	}
	
}
