package com.studyroom.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.studyroom.R;
import com.studyroom.model.Comment;

public class DetailsDialogAdapter extends ArrayAdapter<Object> {

	Context context;
	private ArrayList<Object> mValues;

	public DetailsDialogAdapter(Context context, ArrayList<Object> s) {
		super(context, R.layout.comment_row, s);
		this.context = context;
		this.mValues = s;
	}

	
	@Override
	public int getCount() {
		return mValues.size();
	}

	@Override
	public int getViewTypeCount() {
		return 1;

	}

	@Override
	public int getItemViewType(int position) {

		return 1;
	}

	static class CommentViewHolder {
		TextView description;
		TextView username;
		TextView date;
		int position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = convertView;
		CommentViewHolder commentHolder = new CommentViewHolder();

		if (row == null) {
			row = inflater.inflate(R.layout.comment_row, parent, false);

			commentHolder.description = (TextView) row
					.findViewById(R.id.tvCommentRowDescription);
			commentHolder.username = (TextView) row
					.findViewById(R.id.tvCommentRowUsername);
			commentHolder.date = (TextView) row
					.findViewById(R.id.tvCommentRowDate);
			row.setTag(commentHolder);

		}

		commentHolder = (CommentViewHolder) row.getTag();
		commentHolder.description.setText(((Comment) mValues.get(position))
				.getDescription());
		commentHolder.username.setText(((Comment) mValues.get(position))
				.getAuthor());
		commentHolder.date.setText(((Comment) mValues.get(position))
				.getDateTime());

		return row;
	}
}
