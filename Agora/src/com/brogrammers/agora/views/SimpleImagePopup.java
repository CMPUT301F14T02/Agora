package com.brogrammers.agora.views;

import com.brogrammers.agora.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SimpleImagePopup {
	private Dialog popup; 
	
	public SimpleImagePopup(Bitmap image, Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		ImageView imageView = new ImageView(activity);
		imageView.setImageBitmap(image);
		builder.setView(imageView);
		
		// this prevents white bars on landscape oriented images
		if (image.getWidth() > image.getHeight()) {
			imageView.setAdjustViewBounds(true);
		}
		popup = builder.create();
	}
	
	public void show() {
		popup.show();
	}
}
