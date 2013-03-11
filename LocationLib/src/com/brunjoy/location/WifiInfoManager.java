package com.brunjoy.location;

import java.util.ArrayList;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
public class WifiInfoManager {
	
	WifiManager wm;
	
	public WifiInfoManager(){}
	
	public ArrayList<WifiInfo> getWifiInfo(Context context){
		ArrayList<WifiInfo> wifi = new ArrayList<WifiInfo>();
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = new WifiInfo();
		
		info.mac = wm.getConnectionInfo().getBSSID();
		wifi.add(info);
		Log.d("location", " WifiManager=wifi:"+wifi);
		return wifi;
	}
}