package com.brunjoy.location;

public class CellIDInfo {
	
	public int cellId;
	public String mobileCountryCode;
	public String mobileNetworkCode;
	public int locationAreaCode;
	public String radioType;
	
	@Override
	public String toString() {
		return "CellIDInfo [cellId=" + cellId + ", mobileCountryCode="
				+ mobileCountryCode + ", mobileNetworkCode="
				+ mobileNetworkCode + ", locationAreaCode=" + locationAreaCode
				+ ", radioType=" + radioType + "]";
	}

	public CellIDInfo(){}
}