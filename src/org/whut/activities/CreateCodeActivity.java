package org.whut.activities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.SimpleFormatter;

import org.whut.client.CasClient;
import org.whut.qrcodemanagement.R;
import org.whut.services.BlueToothService;
import org.whut.services.BlueToothService.OnReceiveDataHandleEvent;
import org.whut.utils.BarcodeCreater;
import org.whut.utils.BitmapUtils;
import org.whut.utils.CommonUtils;
import org.whut.utils.UrlStrings;




import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class CreateCodeActivity extends Activity{

	private String deviceId;

	private static final int REQUEST_EX = 1;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	private ProgressDialog dialog;
	private Timer timer;

	private Set<BluetoothDevice> devices;

	private BlueToothService mBTService = null;
	private ArrayAdapter<String> mNewDevicesArrayAdapter = null;// 新搜索列表


	public Handler mhandler;
	private Handler handler;

	//缓存图片
	private Bitmap bitmap = null;

	private String qrCodeString;

	private ImageView qrCodeImage;

	private Button btn_connect;
	private Button btn_print;
	private Button btn_finish;

	private String last_address = null;

	private SharedPreferences preference;
	private Editor editor;

	private ImageView arr_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createcode);

		preference = getSharedPreferences("device", MODE_PRIVATE);
		editor = preference.edit();

		if(preference.getBoolean("haveConnected", false)){
			last_address = preference.getString("address", null);
		}

		deviceId = getIntent().getStringExtra("deviceId");
		qrCodeString = getIntent().getStringExtra("qrCodeString");

		qrCodeImage = (ImageView) findViewById(R.id.qrCode);

		btn_connect = (Button) findViewById(R.id.connectDevice);

		btn_print = (Button) findViewById(R.id.printCode);

		btn_finish = (Button) findViewById(R.id.finish);

		arr_back = (ImageView) findViewById(R.id.iv_topbar_left_back);

		arr_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBTService != null) {
					mBTService.DisConnected();
					mBTService = null;
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
			}
		});

		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("初次搜索设备时间较长，请耐心等待，配对成功之后可在已配对设备中直接连接！");
		dialog.setCancelable(true);
		dialog.setIndeterminate(false);


		bitmap = BarcodeCreater.encode2dAsBitmap(qrCodeString, 200,200, 2);

		qrCodeImage.setImageBitmap(bitmap);

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 0:
					dialog.cancel();
					Toast.makeText(CreateCodeActivity.this, "未搜索到设备，请重试！", Toast.LENGTH_SHORT).show();
					break;
				case 1://扫描完毕
					mBTService.StopScan();
					dialog.cancel();
					timer.cancel();
					Builder builder = new AlertDialog.Builder(CreateCodeActivity.this);
					builder.setAdapter(mNewDevicesArrayAdapter, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//连接蓝牙
							if(!mBTService.IsOpen()){
								mBTService.OpenDevice();
								return;
							}

							if (mBTService.GetScanState() == mBTService.STATE_SCANING) {
								Log.i("msg","scanning...");
								Message msg = new Message();
								msg.what = 2;
								handler.sendMessage(msg);
							}
							if (mBTService.getState() == mBTService.STATE_CONNECTING) {
								Log.i("msg","connecting...");
								return;
							}

							String info = mNewDevicesArrayAdapter.getItem(which);
							String address = info.substring(info.length() - 17);
							last_address = address;
							Log.i("msg", info);
							Log.i("msg", address);
							mBTService.DisConnected();
							mBTService.ConnectToDevice(address);// 连接蓝牙


						}
					}).setTitle("扫描到的设备").show();

					break;
				case 2:
					mBTService.StopScan();
					break;
				case 3:
					CreateCodeActivity.this.finish();
					break;
				}

			}
		};

		mhandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MESSAGE_STATE_CHANGE:// 蓝牙连接状态
					switch (msg.arg1) {
					case BlueToothService.STATE_CONNECTED:// 已经连接
						break;
					case BlueToothService.STATE_CONNECTING:// 正在连接
						break;
					case BlueToothService.STATE_LISTEN:
					case BlueToothService.STATE_NONE:
						break;
					case BlueToothService.SUCCESS_CONNECT:
						Toast.makeText(
								CreateCodeActivity.this,
								CreateCodeActivity.this.getResources().getString(
										R.string.str_succonnect), 2000).show();
						btn_connect.setText("已经连接");
						editor.putString("address",last_address);
						editor.putBoolean("haveConnected",true);
						editor.commit();
						break;
					case BlueToothService.FAILED_CONNECT:
						Toast.makeText(
								CreateCodeActivity.this,
								CreateCodeActivity.this.getResources().getString(
										R.string.str_faileconnect), 2000)
										.show();
						break;
					case BlueToothService.LOSE_CONNECT:
						switch(msg.arg2){
						case -1:
							Toast.makeText(
									CreateCodeActivity.this,
									CreateCodeActivity.this.getResources().getString(
											R.string.str_lose), 2000).show();
							btn_connect.setText("连接设备");
							break;
						case 0:
							break;
						}
					}
					break;
				case MESSAGE_READ:
					// sendFlag = false;//缓冲区已满
					break;
				case MESSAGE_WRITE:// 缓冲区未满
					// sendFlag = true;
					break;

				}
			}
		};

		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_device);
		mBTService = new BlueToothService(this, mhandler);

		btn_connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_connect.getText().toString().equals("连接设备")){

					if(preference.getBoolean("haveConnected", false)){
						Builder builder = new AlertDialog.Builder(CreateCodeActivity.this);
						builder.setTitle("提示").setMessage("是否直接连接上次配对成功设备？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if (!mBTService.IsOpen()) {// 判断蓝牙是否打开
									mBTService.OpenDevice();
									return;
								}
								if (mBTService.GetScanState() == mBTService.STATE_SCANING) {
									Message msg = new Message();
									msg.what = 2;
									handler.sendMessage(msg);
								}
								if (mBTService.getState() == mBTService.STATE_CONNECTING) {
									return;
								}


								Log.i("msg", "直接配对"+last_address);
								mBTService.DisConnected();
								mBTService.ConnectToDevice(last_address);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog2, int which) {
								// TODO Auto-generated method stub
								dialog.show();
								timer = new Timer();
								timer.schedule(new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										Message msg = Message.obtain();
										msg.what = 0;
										handler.sendMessage(msg);
									}
								}, 30000);

								if (!mBTService.IsOpen()) {// 判断蓝牙是否打开
									mBTService.OpenDevice();
									dialog.cancel();
									timer.cancel();
									if (mBTService.GetScanState() == mBTService.STATE_SCANING){
										mBTService.StopScan();
									}
									return;
								}

								if (mBTService.GetScanState() == mBTService.STATE_SCANING)
									return;

								mNewDevicesArrayAdapter.clear();
								devices = mBTService.GetBondedDevice();

								if(devices.size()>0){
									for (BluetoothDevice device : devices) {
										mNewDevicesArrayAdapter.add(device.getName() + "\n"
												+ device.getAddress());
									}
								}
								new Thread(){
									public void run(){
										mBTService.ScanDevice();
									}
								}.start();

							}
						}).show();
					}else{
						dialog.show();
						timer = new Timer();
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Message msg = Message.obtain();
								msg.what = 0;
								handler.sendMessage(msg);
							}
						}, 30000);

						if (!mBTService.IsOpen()) {// 判断蓝牙是否打开
							mBTService.OpenDevice();
							return;
						}

						if (mBTService.GetScanState() == mBTService.STATE_SCANING)
							return;

						mNewDevicesArrayAdapter.clear();
						devices = mBTService.GetBondedDevice();

						if(devices.size()>0){
							for (BluetoothDevice device : devices) {
								mNewDevicesArrayAdapter.add(device.getName() + "\n"
										+ device.getAddress());
							}
						}
						new Thread(){
							public void run(){
								mBTService.ScanDevice();
							}
						}.start();
					}


				}else if(btn_connect.getText().toString().equals("已经连接")){
					Builder builder = new AlertDialog.Builder(CreateCodeActivity.this);
					builder.setTitle("提示").setMessage("是否断开连接？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							if (mBTService.getState() == mBTService.STATE_CONNECTED) {
								mBTService.DisConnected();
							}

						}
					}).setNegativeButton("取消", null).show();
				}
			}
		});

		mBTService.setOnReceive(new OnReceiveDataHandleEvent() {

			@Override
			public void OnReceive(BluetoothDevice device) {
				// TODO Auto-generated method stub
				if (device != null) {
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				} else {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				}
			}
		});

		btn_print.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBTService.getState() != mBTService.STATE_CONNECTED) {
					Toast.makeText(
							CreateCodeActivity.this,
							CreateCodeActivity.this.getResources().getString(
									R.string.str_unconnected), 2000).show();
					return;
				}
				if (bitmap != null) {
					Bitmap title = BitmapFactory.decodeResource(getResources(), R.drawable.title_icon); 
					
					Bitmap title2 = resizeImage(title, 72, 72);
					
//					 InputStream in = getResources().openRawResource(R.raw.title_icon); 
//					
//					 Bitmap title = BitmapUtils.decodeSampledBitmapFromStream(in, 72, 72);
					 
					Bitmap bitmapOrg = BitmapUtils.createTag(BitmapUtils.createBitmapTitle("CSEI小组", title2),bitmap, BitmapUtils.createBitmapText("设备编号:"+CommonUtils.GetNumberFromCode(qrCodeString), "批次编号:"+CommonUtils.GetBatchNumberFromCode(qrCodeString)));// BitmapFactory.decodeFile(picPath);
					
					int w = bitmapOrg.getWidth();
					int h = bitmapOrg.getHeight();
					
					
					mBTService.PrintImage(resizeImage(bitmapOrg, 48*8, h)
							, 5000);// 第二个参数代表延时操作，如果时间太短，会导致打印机堵塞。76打印机需要延时4000到5000
					
					
					return;
				}
			}

		});

		btn_finish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new SetPrintThread()).start();
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBTService!=null){
			mBTService.DisConnected();
			mBTService = null;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mBTService != null) {
			mBTService.DisConnected();
			mBTService = null;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}

	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	class SetPrintThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();

			HashMap<String,String> params = new HashMap<String,String>();

			params.put("deviceId", deviceId);

			Log.i("msg", deviceId);

			params.put("havePrint", "1");

			String result = CasClient.getInstance().doPost(UrlStrings.URL_SETPRINT,params);

			Log.i("msg", result);

			msg.what = 3;

			handler.sendMessage(msg);

		}
	}

}
