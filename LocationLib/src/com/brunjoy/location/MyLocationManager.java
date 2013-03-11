package com.brunjoy.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * @author Haizhu
 * 粗略定位
 */
public class MyLocationManager {

	
	public void getLocationInfo(Context mContext,LocaltionListener mLocaltionListener) {
		new LocaltionAsynTask(mContext,mLocaltionListener).execute();
	
	}
	public interface LocaltionListener
	{
		
		public void result(LocationInfo mLocationInfo);
	}
	class LocaltionAsynTask extends AsyncTask<Void, Void, LocationInfo>
	{
		private LocaltionListener mLocaltionListener;
		private ArrayList<WifiInfo> wifiList;
		private ArrayList<CellIDInfo> cellList;
		public LocaltionAsynTask(Context mContext,LocaltionListener mLocaltionListener) {
			this.mLocaltionListener=mLocaltionListener;
			wifiList=new WifiInfoManager().getWifiInfo(mContext);
			cellList=new CellIDInfoManager().getCellIDInfo(mContext);
		}

		@Override
		protected LocationInfo doInBackground(Void... params) {
			LocationInfo lofo=callGear(wifiList,cellList);
			return lofo;
			
		}

		@Override
		protected void onPostExecute(LocationInfo result) {
			
			if(mLocaltionListener!=null)
			{
				mLocaltionListener.result(result);
			}
		}
		
		
		
	}
	private LocationInfo callGear(ArrayList<WifiInfo> wifi,
			ArrayList<CellIDInfo> cellID) {
		if (cellID == null)
			return null;
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.google.com/loc/json");
		JSONObject holder = new JSONObject();
		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("home_mobile_country_code",
					cellID.get(0).mobileCountryCode);
			holder.put("home_mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			holder.put("radio_type", cellID.get(0).radioType);
			holder.put("request_address", true);
			if ("460".equals(cellID.get(0).mobileCountryCode))
				holder.put("address_language", "zh_CN");
			else
				holder.put("address_language", "en_US");

			JSONObject data, current_data;
			JSONArray array = new JSONArray();

			current_data = new JSONObject();
			current_data.put("cell_id", cellID.get(0).cellId);
			current_data.put("mobile_country_code",
					cellID.get(0).mobileCountryCode);
			current_data.put("mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			current_data.put("age", 0);
			array.put(current_data);

			if (cellID.size() > 2) {
				for (int i = 1; i < cellID.size(); i++) {
					data = new JSONObject();
					data.put("cell_id", cellID.get(i).cellId);
					data.put("location_area_code",
							cellID.get(0).locationAreaCode);
					data.put("mobile_country_code",
							cellID.get(0).mobileCountryCode);
					data.put("mobile_network_code",
							cellID.get(0).mobileNetworkCode);
					data.put("age", 0);
					array.put(data);
				}
			}
			holder.put("cell_towers", array);
			if (wifi.get(0).mac != null) {
				data = new JSONObject();
				data.put("mac_address", wifi.get(0).mac);
				data.put("signal_strength", 8);
				data.put("age", 0);
				array = new JSONArray();
				array.put(data);
				holder.put("wifi_towers", array);
			}
			//
			// StringEntity se = new StringEntity(
			// "{\"address_language\":\"zh_CN\",\"host\":\"http://maps.google.com\",\"radio_type\":\"cdma\",\"home_mobile_country_code\":\"460″,\"home_mobile_network_code\":\"13965″,\"cell_towers\":[{\"mobile_network_code\":\"13965\",\"location_area_code\":11,\"cell_id\":1985,\"age\":0,\"mobile_country_code\":\"460\"}],\"request_address\":true,\"version\":\"1.1.0\"}");
			StringEntity se = new StringEntity(holder.toString());
			post.setEntity(se);
			HttpResponse resp = client.execute(post);
			HttpEntity entity = resp.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			StringBuffer sb = new StringBuffer();
			String result = br.readLine();
			while (result != null) {
				sb.append(result);
				result = br.readLine();
			}
			br.close();
			Log.d("location", sb.toString());
			data = new JSONObject(sb.toString());
			data = (JSONObject) data.get("location");
			Location loc = new Location(LocationManager.NETWORK_PROVIDER);
			loc.setLatitude((Double) data.get("latitude"));
			loc.setLongitude((Double) data.get("longitude"));
			loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
			loc.setTime(System.currentTimeMillis());

			JSONObject obj = data.getJSONObject("address");

			AddressInfo addrList = analysisAddress(obj);

			LocationInfo li = new LocationInfo();
			li.setAddressInfo(addrList);
			li.setLoc(loc);
			return li;
		} catch (JSONException e) {
			Log.d("location", "JSONException:" + e);
			return null;
		} catch (UnsupportedEncodingException e) {
			Log.d("location", "UnsupportedEncodingException:" + e);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.d("location", "ClientProtocolException:" + e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("location", "IOException:" + e);
			e.printStackTrace();
		}
		return null;
	}

	private AddressInfo analysisAddress(JSONObject item) {
		
		if (item == null || item.toString().length() == 0) {
			return null;
		}		
		AddressInfo info = new AddressInfo();
		try {
			info.setCountry(item.getString("country"));
			info.setCountry_code(item.getString("country_code"));
			info.setRegion(item.getString("region"));
			info.setCity(item.getString("city"));
			info.setStreet(item.getString("street"));
			info.setStreet_number(item.getString("street_number"));
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return info;

	}
}
