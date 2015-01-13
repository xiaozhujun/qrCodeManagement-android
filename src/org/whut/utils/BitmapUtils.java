package org.whut.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class BitmapUtils {

	public static Bitmap createTag(Bitmap title,Bitmap barcode,Bitmap text){
		
		Bitmap bitmap = Bitmap.createBitmap(400, 300, Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		canvas.drawBitmap(title, 0, 0,paint);
		canvas.drawBitmap(text, 0, 100, paint);
		canvas.drawBitmap(barcode, 210, 100, paint);
		
		return bitmap;
		
		
	}
	
	public static Bitmap createBitmapText(String deviceId,String batchId){
		
		Bitmap bitmap = Bitmap.createBitmap(200,200,Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		
		Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(20);
		textPaint.setColor( Color.BLACK);
		
		FontMetrics fontMetrics = textPaint.getFontMetrics();
		
		canvas.drawText(deviceId, 10, 80, textPaint);
		canvas.drawText(batchId, 10, 120, textPaint);
		
		return bitmap;
	}
	

	public static Bitmap createBitmapTitle(String title,Bitmap title_image){
		Bitmap  bitmap = Bitmap.createBitmap(400,90,Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);
		
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(72);
		textPaint.setColor(Color.BLACK);
		
		canvas.drawBitmap(title_image, 10, 9, null);
		canvas.drawText(title, title_image.getWidth()+10, title_image.getHeight(), textPaint);
		
		return bitmap;
	}
	
	

	
}
