package com.brunjoy.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.brunjoy.location.MyLocationManager.LocaltionListener;

public class GPSManager {
	private double latitude = 0;
	private double longitude = 0;
	private static GPSManager mGPSManager;
	private LocationManager locationManager;

	private GPSManager(Context mContext) {
		// this.mContext = mContext;
		locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public synchronized static GPSManager getInstance(Context mContext) {
		if (mGPSManager == null) {
			mGPSManager = new GPSManager(mContext);
		}

		return mGPSManager;
	}

	public interface CallBackGpsLoactionLener {
		void myLocation(double latitude, double longitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	private LocationListener locationListener = null;
	private CallBackGpsLoactionLener mCallBackGpsLoactionLener;

	public void findLocation(final Context mContext,
			final CallBackGpsLoactionLener mCallBackGpsLoactionLener) {

		if (this.locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		this.mCallBackGpsLoactionLener = mCallBackGpsLoactionLener;
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				if (mCallBackGpsLoactionLener != null) {
					mCallBackGpsLoactionLener.myLocation(latitude, longitude);
				}
				return;
			}
		} else {
			if (mCallBackGpsLoactionLener != null) {
				new MyLocationManager().getLocationInfo(mContext,
						new LocaltionListener() {

							@Override
							public void result(LocationInfo mLocationInfo) {
								if (mCallBackGpsLoactionLener != null)
									if(mLocationInfo==null)
									{
										mCallBackGpsLoactionLener.myLocation(0, 0);
										return;
									}
									mCallBackGpsLoactionLener.myLocation(
											mLocationInfo.getLoc()
													.getLatitude(),
											mLocationInfo.getLoc()
													.getLongitude());

							}
						});

			}
			return;
		}
		startLocation();
	}

	private void startLocation() {
		locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					if (mCallBackGpsLoactionLener != null) {
						mCallBackGpsLoactionLener.myLocation(latitude,
								longitude);
					}
				}
			}
		};
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			latitude = location.getLatitude(); 
			longitude = location.getLongitude(); 
			if (mCallBackGpsLoactionLener != null) {
				mCallBackGpsLoactionLener.myLocation(latitude, longitude);
			}
			// MyLog.E("Map",
			// "Locati222222on changed : Lat: " + location.getLatitude()
			// + " Lng: " + location.getLongitude());
		}
	}

	public void cancleLocation() {
		if (locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
		locationListener = null;
		mCallBackGpsLoactionLener = null;

	}

}
