package com.studyroom.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.studyroom.R;
import com.studyroom.model.Error;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;

public class SignInActivity extends Activity implements UpdateAdapters {

	private EditText username;
	private EditText password;
	private ImageView emailBackground;
	private ImageView passwordBackground;
	public static String auth_token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.signin);

		username = (EditText) findViewById(R.id.etUsername);
		password = (EditText) findViewById(R.id.etPassword);
		emailBackground = (ImageView) findViewById(R.id.ivSignInUsernameBackground);
		passwordBackground = (ImageView) findViewById(R.id.ivSignInPasswordBackground);
	}

	// Button
	// Change to SignUpActivity
	public void signUp(View view) {
		Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
		startActivity(i);
	}

	// Button
	// Send Username and Password to Server
	public void signIn(View view) {
		new AddToAdapter(this, this).execute();
	}

	private class AddToAdapter extends
			AsyncTask<String, String, ArrayList<String>> {
		private ProgressDialog pDialog;
		Context        mContext;
		UpdateAdapters ua;
		ArrayList<String> responseList = new ArrayList<String>();

		public AddToAdapter(Context c, UpdateAdapters ua) {
			this.mContext = c;
			this.ua = ua;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SignInActivity.this);
			pDialog.setMessage("Signing In ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

			emailBackground
					.setBackgroundResource(R.drawable.edit_text_background_valid);
			passwordBackground
					.setBackgroundResource(R.drawable.edit_text_background_valid);

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("email", username.getText().toString().trim());
			map.put("password", password.getText().toString().trim());
			PostDispatcher signinPostDispatcher = new PostDispatcher("User",
			                                                         "signin", map, getString(R.string.signinURL), mContext, ua);

			signinPostDispatcher.execute();

			return responseList;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			pDialog.dismiss();
			// Parse Response

		}

	}

	@Override
	public void update(ArrayList<Object> s) {
		// TODO Auto-generated method stub

		if (s.size() != 0) {
			if (s.get(0).getClass() == Error.class) {

				if (((Error) s.get(0)).getCode().equals("11-5-2-1")) {
					passwordBackground
							.setBackgroundResource(R.drawable.edit_text_background_error);

				} else {
					emailBackground
							.setBackgroundResource(R.drawable.edit_text_background_error);

					passwordBackground
							.setBackgroundResource(R.drawable.edit_text_background_error);
				}

				String message = ((com.studyroom.model.Error) s.get(0)).getMessage();
				Toast toast = Toast.makeText(getApplicationContext(), message,
				                             Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				if (message != "") {
					toast.show();
				}
			} else {

				SharedPreferences prefs = getSharedPreferences(
						"com.canal5.studyroom", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("auth_token", ((String) s.get(0)).toString());
				editor.putString("user_id", ((String) s.get(1)).toString());
				editor.commit();
				Intent i = new Intent(getApplicationContext(),
						QuestionFeedActivity.class);
				startActivity(i);
			}

		}
	}

}
