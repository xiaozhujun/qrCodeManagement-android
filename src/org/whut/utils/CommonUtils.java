package org.whut.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.util.Log;

public class CommonUtils {

	//判断输入值是否为数字
	public static boolean isNumeric(String str){
		for (int i = str.length();--i>=0;){   
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	//HashMap转化为jsonString
	public static String HashToJson(HashMap<String,Object> params){
		String string = "{";  
		for (Iterator<Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext();) {  
			Entry<String,Object> e = (Entry<String,Object>) it.next();  
			string += "\"" + e.getKey() + "\":";  
			string += "\"" + e.getValue() + "\",";  
		}  
		string = string.substring(0, string.lastIndexOf(","));  
		string += "}";  
		return string;
	}

	public static String createQRCodeString(HashMap<String, String> hashMap) {
		// TODO Auto-generated method stub
		
		
		
		return "rent"+","+hashMap.get("id")+","+hashMap.get("number")+","+hashMap.get("batchId")+","+hashMap.get("batchNumber")+","
				+hashMap.get("typeId")+","+hashMap.get("deviceType")+","+hashMap.get("isMainDevice");
	}

	public static String GetDeviceIdFromCode(String code) {
		// TODO Auto-generated method stub
		
		Log.i("msg", code.substring(0, 4));
		
		if(!code.substring(0, 4).equals("rent")){
			return null;
		}
		return code.split(",")[1];
	}



	public static String GetBatchNumberFromCode(String qrCodeString) {
		// TODO Auto-generated method stub
		return qrCodeString.split(",")[4];
	}

	public static String GetNumberFromCode(String qrCodeString) {
		// TODO Auto-generated method stub
		return qrCodeString.split(",")[2];
	}
}
