package com.icecream.Dialogs;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.icecream.R;


@SuppressLint("ValidFragment")
public class ProgressDialogFragment extends DialogFragment {
	private String message;
	TextView txtMsg;

	public ProgressDialogFragment() {
		this.message = "";
	}

	public ProgressDialogFragment(String message) {
		this.message = message;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Dialog loading = new Dialog(getActivity());
		loading.setContentView(R.layout.layout_progress);
		loading.setCancelable(false);
		loading.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		int divierId = loading.getContext().getResources()
				.getIdentifier("android:id/titleDivider", null, null);
		View divider = loading.findViewById(divierId);

		txtMsg= (TextView) loading.findViewById(R.id.txtMsg);
		if(message.trim().length()>0){
			txtMsg.setText(message);
		}

		int width = (int)(getResources().getDisplayMetrics().widthPixels*0.7f);
		int height = (int)(getResources().getDisplayMetrics().heightPixels*0.7f);

		loading.getWindow().setLayout(width, height);
		if(divider!=null) {
			divider.setBackgroundColor(Color.TRANSPARENT);
		}

		loading.show();
		return loading;
	}
}
