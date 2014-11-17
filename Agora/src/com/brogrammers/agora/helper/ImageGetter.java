package com.brogrammers.agora.helper;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class ImageGetter {
	public static final int CAMERA_ACTIVITY_REQUEST_CODE = 24672; // 24672 = AGORA
	protected Activity activity;

	public ImageGetter(Activity activity) {
		// Note we must pass in the activity because startActivityForResult
		// cannot be called from a non-activity class.
		this.activity = activity;
	}

	public Uri getUri() {
		// TODO: cite Lab5-3
		File folder = new File("/sdcard/AgoraImages");
		if (!folder.exists())
			folder.mkdir();

		String imagePathAndFileName = folder.getAbsolutePath() + File.separator
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		Log.e("IMAGE", "imagePathAndFileName="+imagePathAndFileName);
		File imageFile = new File(imagePathAndFileName);
		Uri imageUri = Uri.fromFile(imageFile);
		if (imageUri == null) Log.e("IMAGE", "returning null uri");
		return imageUri;
	}
	
	public void getImage(Uri imageUri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		activity.startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
	}
	
}
