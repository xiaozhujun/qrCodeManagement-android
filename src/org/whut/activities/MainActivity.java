package org.whut.activities;


import java.util.Timer;
import java.util.TimerTask;

import org.whut.qrcodemanagement.R;
import org.whut.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



@SuppressLint("HandlerLeak")
public class MainActivity extends Activity{

	private RelativeLayout rl_deviceId;
	private RelativeLayout rl_deviceType;
	private RelativeLayout rl_batchId;

	private TextView tv_deviceId;
	private TextView tv_deviceType;
	private TextView tv_batchId;

	private TextView tv_btn;


	private String[] deviceType = {"司机室","标准节","空"};
	private String[] batchId = {"sdfsd","pc20141125-01","bzj20141129","空"};

	private String deviceId_value;
	private String deviceType_value;
	private String batchId_value;
	
	private ImageView arr_back;
	
	private RelativeLayout scan;
	

	/** 
	 * 菜单、返回键响应 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode == KeyEvent.KEYCODE_BACK)  {   
			exitBy2Click();
		}  
		return false;  
	}  
	/** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  

	private void exitBy2Click() {  
		Timer tExit = null;  
		if (isExit == false) {  
			isExit = true; // 准备退出  
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
			tExit = new Timer();  
			tExit.schedule(new TimerTask() {  
				@Override  
				public void run() {  
					isExit = false; // 取消退出  
				}  
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  

		} else {  
			System.exit(0);
		}  
	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		rl_deviceId = (RelativeLayout) findViewById(R.id.deviceId_panel);
		rl_deviceType = (RelativeLayout) findViewById(R.id.deviceType_panel);
		rl_batchId = (RelativeLayout) findViewById(R.id.batchId_panel);

		tv_deviceId = (TextView) findViewById(R.id.deviceId_value);
		tv_deviceType = (TextView) findViewById(R.id.deviceType_value);
		tv_batchId = (TextView) findViewById(R.id.batchId_value);

		
		
		tv_btn = (TextView) findViewById(R.id.btn_start);

		arr_back = (ImageView) findViewById(R.id.iv_topbar_left_back);
		
		arr_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("提示").setMessage("是否退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
							System.exit(0);
					}
				}).setNegativeButton("取消", null).show();
			}
		});
		
		scan = (RelativeLayout) findViewById(R.id.tv_topbar_right_map_layout);
		
		scan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
					Intent it = new Intent(MainActivity.this,ScanCodeActivity.class);
					startActivityForResult(it, 0);
			}
		});
		
		rl_deviceId.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder builder = new AlertDialog.Builder(MainActivity.this);
				final EditText editText = new EditText(MainActivity.this);
				builder.setTitle("请输入设备编号").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String value = editText.getText().toString().trim();
						tv_deviceId.setText(value);
						deviceId_value = value;
					
					}
				}).setNegativeButton("取消", null).show();
			}
		});

		rl_deviceType.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("请选择设备类型").setItems(deviceType, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
							deviceType_value = "1";
							tv_deviceType.setText(deviceType[0]);
							break;
						case 1:
							deviceType_value = "2";
							tv_deviceType.setText(deviceType[1]);
							break;
						case 2:
							deviceType_value=null;
							tv_deviceType.setText("");
							break;
						}
					}
				}).show();
			}
		});

		rl_batchId.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("请选择批次编号").setItems(batchId, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
							batchId_value = "1";
							tv_batchId.setText(batchId[0]);
							break;
						case 1:
							batchId_value = "2";
							tv_batchId.setText(batchId[1]);
							break;
						case 2:
							batchId_value = "3";
							tv_batchId.setText(batchId[2]);
							break;
						case 3:
							batchId_value=null;
							tv_batchId.setText("");
							break;
						}
					}
				}).show();
			}
		});


		tv_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(tv_deviceId.getText().toString())&&TextUtils.isEmpty(tv_deviceType.getText().toString())&&TextUtils.isEmpty(tv_batchId.getText().toString())){
					Toast.makeText(getApplicationContext(), "请输入至少一项查询条件！", Toast.LENGTH_SHORT).show();
				}else{
					Intent it = new Intent(MainActivity.this,DeviceListActivity.class);
					it.putExtra("deviceId_value", deviceId_value);
					it.putExtra("deviceType_value", deviceType_value);
					it.putExtra("batchId_value", batchId_value);
					startActivity(it);
				}
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		String code = data.getStringExtra("Code");
		Intent it = new Intent(MainActivity.this,CreateCodeActivity.class);
		it.putExtra("qrCodeString", code);
		String deviceId = CommonUtils.GetDeviceIdFromCode(code);
		if(deviceId==null){
			Toast.makeText(MainActivity.this, "请扫设备专用二维码！", Toast.LENGTH_SHORT).show();
			return;
		}
		it.putExtra("deviceId", deviceId);
		startActivity(it);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
