package com.studyroom.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studyroom.R;

public class EnrollClassAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;

	public EnrollClassAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.feedrow, values);
		this.context = context;
		this.values = values;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.feedrow, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);

		textView.setText(values.get(position));

		return rowView;
	}

}
