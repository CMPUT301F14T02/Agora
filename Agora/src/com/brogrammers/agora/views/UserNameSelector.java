package com.brogrammers.agora.views;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
import com.brogrammers.agora.data.DeviceUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserNameSelector {

	private final Dialog dialog;

	public UserNameSelector(Activity activity) {
		AlertDialog.Builder popup = new AlertDialog.Builder(activity);
		LayoutInflater inflater = (LayoutInflater) Agora.getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		
		View popupView = inflater.inflate(R.layout.select_username_popup, null);
				
		popup.setView(popupView);
		final EditText usernameField = (EditText)popupView.findViewById(R.id.usernameField);
				
		dialog = popup.create();
		dialog.setCancelable(false);
		
		((Button)popupView.findViewById(R.id.usernameOK)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DeviceUser.getUser().setUsername(usernameField.getText().toString());
				Log.e("USERNAME", usernameField.getText().toString());
				Log.e("USERNAME", DeviceUser.getUser().getUsername());
				dialog.dismiss();
			}
		});
	}

	public void show() {
		dialog.show();
	}

}
