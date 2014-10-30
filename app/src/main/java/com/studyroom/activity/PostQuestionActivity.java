package com.studyroom.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.studyroom.R;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class PostQuestionActivity extends Activity implements UpdateAdapters {

	Spinner mSpinner;
	ArrayAdapter<String> spinnerAdapter;
	public static ArrayList<String> classes = new ArrayList<String>();
	ArrayList<String> tempList = new ArrayList<String>();
	TextView tvTitleCharCount;
	EditText etTitle;
	EditText etDescription;
	ImageView ivTitleBackground;
	ImageView ivDescriptionBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.post_question);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		tvTitleCharCount = (TextView) findViewById(R.id.tvTitleCharCount);
		etTitle = (EditText) findViewById(R.id.title);
		etDescription = (EditText) findViewById(R.id.question_description_edit_text);
		mSpinner = (Spinner) findViewById(R.id.class_spinner);
		ivTitleBackground = (ImageView) findViewById(R.id.ivTitleBackground);
		ivDescriptionBackground= (ImageView) findViewById(R.id.post_question_description_background);
		

		new AddToAdapter(this, this).execute("class_relationships");
		etTitle.addTextChangedListener(mTextEditorWatcher);

		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {

		
		if(etTitle.getText().length() > 140){
			ivTitleBackground.setBackgroundResource(R.drawable.edit_text_background_error);
			Toast.makeText(this, "The title must be less than 140 characters", Toast.LENGTH_SHORT).show();
			
		}
		else if(etTitle.getText().length() == 0){
			ivTitleBackground.setBackgroundResource(R.drawable.edit_text_background_error);
			Toast.makeText(this, "You must have a title for your question", Toast.LENGTH_SHORT).show();
			
		}
		else if(etDescription.getText().length() == 0){
			ivDescriptionBackground.setBackgroundResource(R.drawable.edit_text_background_error);
			Toast.makeText(this, "You must describe youe question before posting it", Toast.LENGTH_SHORT).show();
		}
		else{
			new AddToAdapter(this, this).execute("question");
			
			Toast.makeText(this, "Posting Question", Toast.LENGTH_LONG).show();

			finish();
		}
		
		

	}
	
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	        }

	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	           //This sets a textview to the current length
	           tvTitleCharCount.setText(String.valueOf(140 - s.length()));
	        }

	        public void afterTextChanged(Editable s) {
	        	if (140-s.length() < 5){
	        		tvTitleCharCount.setTextColor(getResources().getColor(android.R.color.holo_red_light));
	        	}else{
	        		tvTitleCharCount.setTextColor(getResources().getColor(R.color.studyroom_purple));
	        	}
	        }
	};

	private class AddToAdapter extends
			AsyncTask<String, String, ArrayList<String>> {
		private ProgressDialog pDialog;
		Context        mContext;
		UpdateAdapters ua;
		String         option;

		public AddToAdapter(Context c, UpdateAdapters u) {
			this.mContext = c;
			ua = u;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(PostQuestionActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			option = arg0[0];

			if (arg0[0].equals("question")) {

				HashMap<String, String> postMap = new HashMap<String, String>();


				postMap.put("title", etTitle.getText().toString());
				postMap.put("description", etDescription.getText().toString());
				// postMap.put("user_id", "2");
				postMap.put("class_name", mSpinner.getSelectedItem().toString());
				// postMap.put("null", "null");

				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						"question", "question.Model", postMap,
						getString(R.string.questionsURL), mContext,
						PostQuestionActivity.this);

				postQuestionDispatcher.execute();

			} else {
				HashMap<String, String> postMap = null;

				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						arg0[0], arg0[0], postMap,
						getString(R.string.classesURL), mContext, ua);
				// System.out.println(postMap.toString());
				postQuestionDispatcher.execute();
			}

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<String> values) {
			pDialog.dismiss();
			ArrayList<String> redo = values;// new ArrayList<String>();

			/*
			 * for (int i=0; i<values.size(); i++){ redo.add(values.get(i)); }
			 */

			if (option.equals("questions")) {

			} else {

			}

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);

	}

	@Override
	public void update(ArrayList<Object> s) {
		// TODO Auto-generated method stub

		for (Object obj : s) {
			tempList.add((String) obj);
		}

		spinnerAdapter = new ArrayAdapter<String>(PostQuestionActivity.this,
		                                          android.R.layout.simple_spinner_item, tempList);

		System.out.println(s.toString() + "+++++++++++++++++++++++++++++++++");
		mSpinner.setAdapter(spinnerAdapter);

	}

}
