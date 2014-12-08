package org.whut.adapters;

import java.util.HashMap;
import java.util.List;

import org.whut.qrcodemanagement.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyDeviceAdapter extends BaseAdapter{

	private List<HashMap<String,String>> list;
	private LayoutInflater inflater;
	private Context context;
	
	public MyDeviceAdapter(Context context,List<HashMap<String,String>> list){
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView = inflater.inflate(R.layout.listitem_device, null);
			ViewHolder holder = new ViewHolder();
			holder.tv_deviceName = (TextView) convertView.findViewById(R.id.device_name);
			holder.tv_deviceType = (TextView) convertView.findViewById(R.id.deviceType);
			holder.tv_batchName = (TextView) convertView.findViewById(R.id.batchName);
			holder.tv_deviceId = (TextView) convertView.findViewById(R.id.deviceId);
			holder.tv_supplierName = (TextView) convertView.findViewById(R.id.supplierName);
			holder.tv_storeHouseName = (TextView) convertView.findViewById(R.id.storeHouseName);
			holder.printTag = (ImageView) convertView.findViewById(R.id.printTag);	
			convertView.setTag(holder);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		holder.tv_deviceName.setText(list.get(position).get("name"));
		holder.tv_deviceType.setText(list.get(position).get("deviceType"));
		holder.tv_batchName.setText(list.get(position).get("batchNumber"));
		holder.tv_deviceId.setText("设备编号："+list.get(position).get("number"));
		holder.tv_supplierName.setText("供应商名："+list.get(position).get("supplierName"));
		holder.tv_storeHouseName.setText("存储地点："+list.get(position).get("storehouseName"));
		
		switch(Integer.parseInt(list.get(position).get("havePrint"))){
			case 0:
				holder.printTag.setVisibility(View.GONE);
				break;
			case 1:
				holder.printTag.setVisibility(View.VISIBLE);
				break;
		}
		
		
		return convertView;
	}

	class ViewHolder{
		TextView tv_deviceName;
		TextView tv_deviceType;
		TextView tv_batchName;
		TextView tv_deviceId;
		TextView tv_supplierName;
		TextView tv_storeHouseName;
		ImageView printTag;
	}
	
}
