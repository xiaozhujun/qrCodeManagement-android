package org.whut.activities;

import java.util.HashMap;
import java.util.List;

import org.whut.adapters.MyDeviceAdapter;
import org.whut.client.CasClient;
import org.whut.qrcodemanagement.R;
import org.whut.utils.CommonUtils;
import org.whut.utils.JsonUtils;
import org.whut.utils.UrlStrings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class DeviceListActivity extends Activity{

	private String deviceId_value;
	private String deviceType_value;
	private String batchId_value;
	
	private ListView listView;
	private MyDeviceAdapter adapter;
	
	private TextView tv_no_device;
	
	private Handler handler;
	
	private ImageView arr_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_devicelist);
	
		deviceId_value = getIntent().getStringExtra("deviceId_value");
		deviceType_value = getIntent().getStringExtra("deviceType_value");
		batchId_value = getIntent().getStringExtra("batchId_value");
		
		listView = (ListView) findViewById(R.id.listView);
		tv_no_device = (TextView) findViewById(R.id.text_no_device);
		arr_back = (ImageView) findViewById(R.id.iv_topbar_left_back);
		
		arr_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					DeviceListActivity.this.finish();
		
			}
		});
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 0:
					Toast.makeText(getApplicationContext(), "对不起，操作出错，请稍后再试！", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					@SuppressWarnings("unchecked")
					final List<HashMap<String,String>> list = (List<HashMap<String, String>>) msg.obj;
					if(list.size()==0){
						listView.setVisibility(View.GONE);
						tv_no_device.setVisibility(View.VISIBLE);
					}else{
						listView.setVisibility(View.VISIBLE);
						tv_no_device.setVisibility(View.GONE);
						adapter = new MyDeviceAdapter(getApplicationContext(), list);
						listView.setAdapter(adapter);
						
						
						listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
							
								Intent it = new Intent(DeviceListActivity.this,CreateCodeActivity.class);
							
								it.putExtra("deviceId", list.get(arg2).get("id"));
								
								String qrCodeString = CommonUtils.createQRCodeString(list.get(arg2));
								
								Log.i("msg", qrCodeString);
								
								it.putExtra("qrCodeString", qrCodeString);
								
								startActivity(it);
								finish();
								
							}
						});
					}
					break;
				}
			}
		};
		
		
		
		
		new Thread(new GetDeviceThread()).start();
		
		
		
	}
	
	class GetDeviceThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			HashMap<String,Object> params = new HashMap<String, Object>();
			/***
			 * 设备编号：number
			 * 设备类型：typeId
			 * 设备批次：batchId
			 */
			if(deviceId_value!=null&&deviceId_value!=""){
				params.put("number", deviceId_value);
			}

			if(deviceType_value!=null&&deviceType_value!=""){
				params.put("typeId", deviceType_value);
			}

			if(batchId_value!=null&&batchId_value!=""){
				params.put("batchId", batchId_value);
			}

			String jsonString = CommonUtils.HashToJson(params);

			Log.i("msg", jsonString);

			HashMap<String,String> params2 = new HashMap<String, String>();

			params2.put("jsonString", jsonString);

			String result = CasClient.getInstance().doPost(UrlStrings.URL_GETMESSAGE, params2);

			Log.i("msg", result);

			try{

				msg.obj = JsonUtils.GetDeviceFromResult(result);

				msg.what = 1;

				handler.sendMessage(msg);

			}catch(Exception e){
				e.printStackTrace();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}
	}
	
}
