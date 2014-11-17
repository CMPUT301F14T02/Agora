package com.brogrammers.agora.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brogrammers.agora.Agora;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


/**
 * This will resize an image that a user uploads to 64kb
 * automatically to handle US 8s
 * TODO: Implement IMAGE RESIZE
 * @author Group02
 *
 */
public class ImageResizer {
	public static final int size = 64000; // 64KB

	public static byte[] resize(Uri imageUri) {
		Bitmap fullImage = null;	
		File imageFile = new File(imageUri.getPath());
		try {
			fullImage = BitmapFactory.decodeStream(new FileInputStream(imageFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Log.e("RESIZER", "before scale height="+fullImage.getHeight()+" width="+fullImage.getWidth());
		Bitmap scaledImage = scaleImageToMaxDimension(fullImage, 640);
		Log.e("RESIZER", "after scale height="+scaledImage.getHeight()+" width="+scaledImage.getWidth());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quality = 100;
		do {
			baos.reset();
			scaledImage.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			quality -= 5;
		} while (baos.size() > size); 
		
		byte[] imageBytes = baos.toByteArray();
		Toast.makeText(Agora.getContext(), "Compressed photo size = "+imageBytes.length/1000+"KB", 0).show();
		
		return imageBytes;
	}
	
//	https://stackoverflow.com/questions/20467616/how-to-scale-bitmaps-according-to-the-screen-estate-available
	public static Bitmap scaleImageToMaxDimension(Bitmap original, int maxDimension) {
	    int width = original.getWidth();
	    int height = original.getHeight();

	    if (width > maxDimension || height > maxDimension) {
	        int new_width;
	        int new_height;
	        float ratio = (float) width / height;
	        if (ratio > 1.0f) {
	            new_width = maxDimension;
	            new_height = (int) ((float) new_width / ratio);
	        } else {
	            new_height = maxDimension;
	            new_width = (int) ((float) new_height * ratio);
	        }
	        float scaleWidth = ((float) new_width) / width;
	        float scaleHeight = ((float) new_height) / height;
	        Matrix matrix = new Matrix();
	        matrix.postScale(scaleWidth, scaleHeight);
	        Bitmap resizedBitmap = Bitmap.createBitmap(original, 0, 0, width,
	                height, matrix, true);
	        return resizedBitmap;
	    }
	    return original;
	}
}
