package com.studyroom.activity;

import com.studyroom.R;
import com.studyroom.application.StudyRoom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class DefaultScreenActivity extends Activity {

	private String PREFS_NAME = "login";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.default_page);

		System.out
				.println("---------------------------------testing DefaultScreenActivity onCreate---------------------------------");
		SharedPreferences prefs = getSharedPreferences(
				"com.canal5.studyroom", Context.MODE_PRIVATE);

		String auth_token = prefs.getString("auth_token", "");
		
		Log.e(this.getClass().getSimpleName(), "auth_token = " + auth_token);
		
		if (auth_token.equals("")) {
			System.out
					.println("-----------------------------------------------------------auth_token null-----------------------------------------------------------");
			StudyRoom.startNewActivity(this, SignInActivity.class);
		} else {
			System.out
					.println("-----------------------------------------------------------auth_token not null-----------------------------------------------------------");
			StudyRoom.startNewActivity(this, QuestionFeedActivity.class);
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		finish();
		
	}
	
	

}
