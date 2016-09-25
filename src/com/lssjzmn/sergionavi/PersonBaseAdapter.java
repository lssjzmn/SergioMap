package com.lssjzmn.sergionavi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonBaseAdapter extends BaseAdapter {

	private Context context;

	public PersonBaseAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View inflatedView, ViewGroup parent) {
		if (inflatedView == null) {
			inflatedView = LayoutInflater.from(context).inflate(
					R.layout.simpleadapter_person, null);
			ImageView img = (ImageView) inflatedView
					.findViewById(R.id.simpleimg0);
			TextView nickname = (TextView) inflatedView
					.findViewById(R.id.simpletxt0);
			TextView name = (TextView) inflatedView
					.findViewById(R.id.simpletxt01);
			ImageView img_code = (ImageView) inflatedView
					.findViewById(R.id.simpleimg0_mine);
			ImageView img_to = (ImageView) inflatedView
					.findViewById(R.id.simpleimg0_to);

			img.setImageResource(R.drawable.ic_launcher);
			nickname.setText("Í«≥∆£∫Sergio Kun");
			name.setText("Œ¢–≈∫≈£∫lssjzmn");
			img_code.setImageResource(R.drawable.code);
			img_to.setImageResource(R.drawable.go);
			img_code.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ImageView imgView = new ImageView(context);
					imgView.setImageResource(R.drawable.mycode);
					Builder builder = new AlertDialog.Builder(context);
					builder.setCancelable(true).setView(imgView)
							.setMessage("…®“ª…®∂˛Œ¨¬Îº”Œ“≈∂");
					Dialog dialog = builder.create();
					dialog.show();
				}
			});
		}
		return inflatedView;
	}

}
