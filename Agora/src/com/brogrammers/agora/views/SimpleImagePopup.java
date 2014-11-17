package com.brogrammers.agora.views;

import com.brogrammers.agora.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class SimpleImagePopup {
	private AlertDialog.Builder popup; 
	
	public SimpleImagePopup(Bitmap image, Context activity) {
		popup = new AlertDialog.Builder(activity);
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		View popupView = inflater.inflate(R.layout.full_image_popup, null);
		((ImageView)popupView.findViewById(R.id.popupImage)).setImageBitmap(image);
		
		popup.setView(popupView);
//		popup.setPositiveButton("Close", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//		});
		popup.create();
	}
	
	public void show() {
		popup.show();
	}
}
