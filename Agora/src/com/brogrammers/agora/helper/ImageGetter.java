package com.brogrammers.agora.helper;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

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
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/AgoraImages";
		File folder = new File(path);
		if (!folder.exists())
			folder.mkdir();

		String imagePathAndFileName = path + File.separator
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";

		File imageFile = new File(imagePathAndFileName);
		Uri imageUri = Uri.fromFile(imageFile);

		return imageUri;
	}
	
	public void getImage(Uri imageUri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		activity.startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
	}
	
}
