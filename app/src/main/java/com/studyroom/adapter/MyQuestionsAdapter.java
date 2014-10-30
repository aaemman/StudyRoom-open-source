package com.studyroom.adapter;



import java.util.ArrayList;

import com.studyroom.R;
import com.studyroom.model.Question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyQuestionsAdapter extends ArrayAdapter<Question> {
	private final Context context;
	private ArrayList<Question> mValues;


	
	
	public MyQuestionsAdapter(Context context, ArrayList<Question> values) {
		super(context, R.layout.question_feed_row, values);
	    this.context = context;
	    this.mValues = values;
	}
	@Override
    public int getCount() {
        return mValues.size();
    }

	
	

	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.question_feed_row, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.Title);
	    TextView textView2 = (TextView) rowView.findViewById(R.id.user); 
	    TextView tvClassName = (TextView) rowView.findViewById(R.id.tvQuestionFeedRowClassName);
	    TextView tvDate = (TextView) rowView.findViewById(R.id.tvQuestionFeedRowDate);
	    TextView tvPoints = (TextView) rowView.findViewById(R.id.tvQuestionFeedRowPointsValue);
	    
	    textView.setText(mValues.get(position).getTitle());
	    textView2.setText(mValues.get(position).getAuthor());
	    tvClassName.setText(mValues.get(position).getClassName());
	    tvDate.setText(mValues.get(position).getDateTime());
	    tvPoints.setText(mValues.get(position).getTotalPoints());

	
	    return rowView;
	  }
	


}
