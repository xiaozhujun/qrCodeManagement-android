package org.whut.utils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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
	
	public static synchronized Bitmap decodeSampledBitmapFromStream(  
	        InputStream in, int reqWidth, int reqHeight) {  
	  
	    // First decode with inJustDecodeBounds=true to check dimensions  
	    final BitmapFactory.Options options = new BitmapFactory.Options();  
	    options.inJustDecodeBounds = true;  
	    BitmapFactory.decodeStream(in, null, options);  
	  
	    // Calculate inSampleSize  
	    options.inSampleSize = calculateInSampleSize(options, reqWidth,  
	            reqHeight);  
	  
	    // Decode bitmap with inSampleSize set  
	    options.inJustDecodeBounds = false;  
	    return BitmapFactory.decodeStream(in, null, options);  
	}  
	  
	/** 
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} 
	 * object when decoding bitmaps using the decode* methods from 
	 * {@link BitmapFactory}. This implementation calculates the closest 
	 * inSampleSize that will result in the final decoded bitmap having a width 
	 * and height equal to or larger than the requested width and height. This 
	 * implementation does not ensure a power of 2 is returned for inSampleSize 
	 * which can be faster when decoding but results in a larger bitmap which 
	 * isn't as useful for caching purposes. 
	 *  
	 * @param options 
	 *            An options object with out* params already populated (run 
	 *            through a decode* method with inJustDecodeBounds==true 
	 * @param reqWidth 
	 *            The requested width of the resulting bitmap 
	 * @param reqHeight 
	 *            The requested height of the resulting bitmap 
	 * @return The value to be used for inSampleSize 
	 */  
	public static int calculateInSampleSize(BitmapFactory.Options options,  
	        int reqWidth, int reqHeight) {  
	    // Raw height and width of image  
	    final int height = options.outHeight;  
	    final int width = options.outWidth;  
	    int inSampleSize = 1;  
	  
	    //先根据宽度进行缩小  
	    while (width / inSampleSize > reqWidth) {  
	        inSampleSize++;  
	    }  
	    //然后根据高度进行缩小  
	    while (height / inSampleSize > reqHeight) {  
	        inSampleSize++;  
	    }  
	    return inSampleSize;  
	}  
	

	
}
