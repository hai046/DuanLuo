package com.brunjoy.location;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class CellIDInfoManager {
	private TelephonyManager manager;
	private PhoneStateListener listener;
	private GsmCellLocation gsm;
	private CdmaCellLocation cdma;
	int lac;
	String current_ci, mcc, mnc;

	public CellIDInfoManager() {
	}

	public ArrayList<CellIDInfo> getCellIDInfo(Context context) {
		manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		listener = new PhoneStateListener();
		manager.listen(listener, 0);
		ArrayList<CellIDInfo> CellID = new ArrayList<CellIDInfo>();
		CellIDInfo currentCell = new CellIDInfo();

		int type = manager.getNetworkType();
		Log.d("location", "type:"+type);
		
		if (type == TelephonyManager.NETWORK_TYPE_GPRS||type ==TelephonyManager.NETWORK_TYPE_UMTS
				|| type == TelephonyManager.NETWORK_TYPE_EDGE
				|| type == TelephonyManager.NETWORK_TYPE_HSDPA) {
			gsm = ((GsmCellLocation) manager.getCellLocation());
			Log.d("location", "gsm:"+gsm);
			if (gsm == null)
				return null;
			
			lac = gsm.getLac();
			mcc = manager.getNetworkOperator().substring(0, 3);
			mnc = manager.getNetworkOperator().substring(3, 5);
			if(mnc.startsWith("0"))
			{
				mnc=mnc.substring(1);
			}

			currentCell.cellId = gsm.getCid();
			currentCell.mobileCountryCode = mcc;
			currentCell.mobileNetworkCode = mnc;
			currentCell.locationAreaCode = lac;
			currentCell.radioType = "gsm";
			CellID.add(currentCell);

			List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				CellIDInfo info = new CellIDInfo();
				
				info.cellId = list.get(i).getCid();
				info.mobileCountryCode = mcc;
				info.mobileCountryCode = mnc;
				info.locationAreaCode = lac;
				CellID.add(info);
			}
			Log.d("location", "CellID:"+CellID);
			return CellID;

		} else if (type == TelephonyManager.NETWORK_TYPE_CDMA
				|| type == TelephonyManager.NETWORK_TYPE_1xRTT) {
			cdma = ((CdmaCellLocation) manager.getCellLocation());
			Log.d("location", "cdma:"+cdma);
			if (cdma == null)
				return null;
			Log.d("location", "cdma:"+cdma);
			if ("460".equals(manager.getSimOperator().substring(0, 3)))
				return null;
		}else
		{
			 manager.getCellLocation();
			Log.d("location", "cdma:"+cdma);
		}
		return null;
	}
}