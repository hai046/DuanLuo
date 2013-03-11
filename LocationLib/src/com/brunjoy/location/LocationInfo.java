package com.brunjoy.location;

import android.location.Location;

public class LocationInfo {
	private Location loc;
	private AddressInfo addressInfo;

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}
	public Location getLoc() {
		return loc;
	}
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	@Override
	public String toString() {
		return "LocationInfo [loc=" + loc + ", addressInfo=" + addressInfo + "]";
	}
	
	
}
