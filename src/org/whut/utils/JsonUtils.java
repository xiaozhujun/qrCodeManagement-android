package org.whut.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {

	
	/***
	 * {"message":"操作成功！",
	 * "data":[{"customerName":"中建一局","createTime":"2014-11-09",
	 * "supplierName":"三一重工","supplierId":1,"status":"使用",
	 * "batchId":1,"number":"sjs001","priceUnit":"元",
	 * "produceTime":"2013-11-01","id":1,"price":"800000",
	 * "batchNumber":"sdfsd","havePrint":0,"deviceType":"司机室",
	 * "name":"塔吊001","storehouseId":1,"storehouseName":"第一仓库",
	 * "contractId":1,"typeId":1,"contractName":"合同1","optionType":4}],"code":200}

	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static List<HashMap<String,String>> GetDeviceFromResult(String result) throws Exception{
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		JSONObject jsonObject = new JSONObject(result);
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		if(jsonArray.length()==0){
			return list;
		}else{
			for(int i=0;i<jsonArray.length();i++){
				HashMap<String,String> params = new HashMap<String,String>();
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				
				params.put("customerName", jsonItem.getString("customerName"));
				params.put("createTime", jsonItem.getString("createTime"));
				params.put("supplierName", jsonItem.getString("supplierName"));
				params.put("supplierId", jsonItem.getInt("supplierId")+"");
			//	params.put("status", jsonItem.getString("status"));
				params.put("batchId", jsonItem.getInt("batchId")+"");
				params.put("number", jsonItem.getString("number"));
				params.put("priceUnit", jsonItem.getString("priceUnit"));
				params.put("produceTime", jsonItem.getString("produceTime"));
				params.put("isMainDevice", jsonItem.getInt("isMainDevice")+"");
				params.put("id", jsonItem.getInt("id")+"");
				params.put("price", jsonItem.getString("price"));
				params.put("batchNumber", jsonItem.getString("batchNumber"));
				params.put("havePrint", jsonItem.getInt("havePrint")+"");
				params.put("deviceType", jsonItem.getString("deviceType"));
				params.put("name", jsonItem.getString("name"));
				params.put("storehouseId", jsonItem.getInt("storehouseId")+"");
				params.put("storehouseName", jsonItem.getString("storehouseName"));
				params.put("contractId", jsonItem.getInt("contractId")+"");
				params.put("typeId", jsonItem.getInt("typeId")+"");
				params.put("contractName", jsonItem.getString("contractName"));
				params.put("optionType", jsonItem.getInt("optionType")+"");
			
				list.add(params);
			
			}
		}
		
		return list;
	}
}
