package com.studyroom.adapter;

import java.util.ArrayList;

import com.studyroom.R;
import com.studyroom.model.Question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class QuestionFeedAdapter extends ArrayAdapter<Question> implements
		Filterable {
	private final Context context;
	private ArrayList<Question> mValues;
	private ArrayList<Question> originalValues;

	public QuestionFeedAdapter(Context context, ArrayList<Question> values) {
		super(context, R.layout.question_feed_row, values);
		this.context = context;
		this.mValues = values;

	}

	@Override
	public int getCount() {
		return mValues.size();
	}

	static class QuestionViewHolder {
		TextView tvTitle;
		TextView tvUsername;
		TextView tvDate;
		TextView tvClassName;
		TextView tvPoints;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		QuestionViewHolder questionHolder;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.question_feed_row, parent,
					false);
			
			questionHolder = new QuestionViewHolder();

			questionHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.Title);
			questionHolder.tvUsername = (TextView) convertView
					.findViewById(R.id.user);
			questionHolder.tvClassName = (TextView) convertView
					.findViewById(R.id.tvQuestionFeedRowClassName);
			questionHolder.tvDate = (TextView) convertView
					.findViewById(R.id.tvQuestionFeedRowDate);
			questionHolder.tvPoints = (TextView) convertView
					.findViewById(R.id.tvQuestionFeedRowPointsValue);

			convertView.setTag(questionHolder);

		}
		questionHolder = (QuestionViewHolder) convertView.getTag();
		questionHolder.tvTitle.setText(mValues.get(position).getTitle());
		questionHolder.tvUsername.setText(mValues.get(position).getAuthor());
		questionHolder.tvClassName
				.setText(mValues.get(position).getClassName());
		questionHolder.tvDate.setText(mValues.get(position).getDateTime());
		questionHolder.tvPoints.setText(mValues.get(position).getTotalPoints());

		return convertView;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				mValues = (ArrayList<Question>) results.values; // has the
																// filtered
																// values
				notifyDataSetChanged(); // notifies the data with new filtered
										// values

			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (originalValues == null) {
					originalValues = new ArrayList<Question>(mValues); // saves
																		// the
																		// original
																		// data
																		// in
																		// mOriginalValues
				}

				FilterResults result = new FilterResults();
				ArrayList<Question> foundItems = new ArrayList<Question>();

				if (constraint == null || constraint.length() == 0) {

					// set the Original result to return
					result.count = originalValues.size();
					result.values = originalValues;
				} else if (constraint.toString() == "All") {
					// set the Original result to return
					result.count = originalValues.size();
					result.values = originalValues;
				}

				else {
					constraint = constraint.toString().toLowerCase();
					System.out.println("constraints"
							+ constraint.toString().toLowerCase());
					for (int i = 0; i < originalValues.size(); i++) {

						String data = (String) originalValues.get(i)
								.getClassName();
						System.out.println("data: " + data);
						if (data.toLowerCase()
								.startsWith(constraint.toString())) {
							foundItems.add(originalValues.get(i));
						}
					}
					// set the Filtered result to return
					result.count = foundItems.size();
					result.values = foundItems;
				}
				return result;

			}
		};
	}

}
