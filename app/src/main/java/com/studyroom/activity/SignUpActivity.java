package com.studyroom.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.studyroom.R;
import com.studyroom.model.Error;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;

public class SignUpActivity extends Activity implements UpdateAdapters {

	private LinearLayout mLayout;
	private EditText email;
	private ImageView emailBackground;
	private EditText confirmEmail;
	private EditText username;
	private EditText password;
	private EditText confirmPassword;
	private ImageView passwordBackground;
	private ImageView confirmPasswordBackground;
	private ImageView usernameBackground;
	private Button mButton;
	private boolean formValid = false;
	public static String auth_token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.signup);
		mLayout = (LinearLayout) findViewById(R.id.linearLayout);

		email = (EditText) findViewById(R.id.etSignUpEmail);
		emailBackground = (ImageView) findViewById(R.id.ivSignUpEmailBackground);
		username = (EditText) findViewById(R.id.etSignUpUsername);
		password = (EditText) findViewById(R.id.etSignUpPassword);
		passwordBackground = (ImageView) findViewById(R.id.ivSignUpPasswordBackground);
		confirmPassword = (EditText) findViewById(R.id.etSignUpPasswordConfirmation);
		confirmPasswordBackground = (ImageView) findViewById(R.id.ivSignUpPasswordConfirmationBackground);
		usernameBackground = (ImageView) findViewById(R.id.ivSignUpUsernameBackground);

		mButton = (Button) findViewById(R.id.bSignUp);
		TextView textView = new TextView(this);
	}

	// Button
	public void signUp(View view) {
		
		
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

			formValid = true;

			Log.i(this.getClass().getSimpleName(),
			      "--------------------------Progress Dialog --------------------------");
			pDialog = new ProgressDialog(SignUpActivity.this);
			pDialog.setMessage("Signing Up ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

			if (!validateEmail(email.getText().toString().trim())) {
				setErrorField("You must use a carleton university email",
				              emailBackground);
				formValid = false;
			} else {
				emailBackground
						.setBackgroundResource(R.drawable.edit_text_background_valid);
			}

			if (!validateUsername(username.getText().toString().trim())) {
				setErrorField(
						"The Username must be between 2 and 10 characters",
						usernameBackground);
				formValid = false;
			} else {
				usernameBackground
						.setBackgroundResource(R.drawable.edit_text_background_valid);
			}

			if (!validatePassword(password.getText().toString().trim(),
			                      confirmPassword.getText().toString().trim())) {
				setErrorField(
						"Both passwords must match and be between 7 and 20 characters",
						passwordBackground);
				setErrorField("", confirmPasswordBackground);
				formValid = false;

			} else {
				passwordBackground
						.setBackgroundResource(R.drawable.edit_text_background_valid);
				confirmPasswordBackground
						.setBackgroundResource(R.drawable.edit_text_background_valid);

			}

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			if (formValid) {
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("email", email.getText().toString().trim());
				map.put("name", username.getText().toString().trim());
				map.put("password", password.getText().toString().trim());
				map.put("password_confirmation", password.getText().toString()
						.trim());

				PostDispatcher signinPostDispatcher = new PostDispatcher(
						"User", "signup", map, getString(R.string.signupURL),
						mContext, ua);

				signinPostDispatcher.execute();
			}

			return responseList;
		}

		private void setErrorField(String message, ImageView fieldBackground) {
			fieldBackground
					.setBackgroundResource(R.drawable.edit_text_background_error);

			Toast toast = Toast.makeText(getApplicationContext(), message,
			                             Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			if (message != "") {
				toast.show();
			}
		}

		private boolean validateUsername(String username) {
			// TODO Auto-generated method stub
			if (username.length() < 2 || username.length() > 10) {
				return false;
			} else if (username.length() == 0) {
				return false;
			}
			return true;
		}

		private boolean validatePassword(String password, String confirmPassword) {
			Log.i(password, confirmPassword);
			if (!(password.equals(confirmPassword))) {
			
				return false;
			} else if (password.length() < 7 || password.length() > 20 || confirmPassword.length() < 7 || confirmPassword.length() > 20) {
				return false;
			} else if (password.length() == 0 || confirmPassword.length() == 0) {
				return false;
			}
			return true;
		}

		private boolean validateEmail(String email) {
			// TODO Auto-generated method stub
			if (email.contains("carleton.ca")) {
				return true;
			}

			return false;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			pDialog.dismiss();

		}

	}

	@Override
	public void update(ArrayList<Object> s) {
		// TODO Auto-generated method stub

		if (s.size() != 0) {
			if (s.get(0).getClass() == Error.class) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setMessage(((Error) s.get(0)).getMessage())
						.setTitle("Sorry, Something Went Wrong")
						.setCancelable(false)
						.setPositiveButton("Try Again",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										dialog.cancel();
									}
								})
						.setNegativeButton("Sign In",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										Intent i = new Intent(
												getApplicationContext(),
												SignInActivity.class);
										startActivity(i);

									}
								});

				AlertDialog aDialog = builder.create();

				aDialog.setMessage(((com.studyroom.model.Error) s.get(0)).getMessage());
				aDialog.setCancelable(true);
				aDialog.show();
			}
		} else {

			SharedPreferences prefs = getSharedPreferences(
					"com.canal5.studyroom", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("auth_token", auth_token);
			editor.commit();
			
			Toast toast = Toast.makeText(getApplicationContext(), "Please Check Your Email to Validate Your Account",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			Intent i = new Intent(getApplicationContext(), SignInActivity.class);
			startActivity(i);

		}
	}

}