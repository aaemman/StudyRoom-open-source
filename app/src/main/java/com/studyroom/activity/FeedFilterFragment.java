package com.studyroom.activity;

import com.studyroom.R;
import com.studyroom.adapter.QuestionFeedAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;



public class FeedFilterFragment extends Fragment{
	ListView listView;
	QuestionFeedAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.feed_filter_fragment, container, false);
		return view;
		
	}
}
