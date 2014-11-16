package com.brogrammers.agora.helper;

import java.io.ByteArrayOutputStream;

import com.brogrammers.agora.Agora;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
		try {
			fullImage = MediaStore.Images.Media.getBitmap(Agora.getContext().getContentResolver(), imageUri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(Agora.getContext(), "height="+fullImage.getHeight()
				+" width="+fullImage.getWidth(), 0).show();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quality = 100;
		do {
			baos.reset();
			fullImage.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			quality -= 5;
		} while (baos.size() > size); 
		
		byte[] imageBytes = baos.toByteArray();
		Toast.makeText(Agora.getContext(), "Compressed photo size = "+imageBytes.length/1000+"KB", 0).show();
		
		return imageBytes;
	}
	
	
}
