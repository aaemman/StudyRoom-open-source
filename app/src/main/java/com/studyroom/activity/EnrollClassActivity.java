package com.studyroom.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.studyroom.R;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;
import com.studyroom.adapter.EnrollClassAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EnrollClassActivity extends Activity implements UpdateAdapters {

	private EditText course;

	ListView listView;
	EnrollClassAdapter adapter;
	private ActionBarDrawerToggle mDrawerToggle;

	// Defined Array values to show in ListView
	// public static ArrayList<String> values = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.enroll);
		course = (EditText) findViewById(R.id.editText);

		/*
		 * listView = (ListView) findViewById(R.id.list); adapter = new
		 * EnrollClassAdapter(this, (ArrayList<String>) values2);
		 * listView.setAdapter(adapter);
		 */

		new AddToAdapter(this, this).execute();

	}

	public static List<String> values2 = new ArrayList<String>();

	public void addToValues(String s) {
		values2.add(s);
	}

	public ArrayList<String> getValues() {
		return (ArrayList<String>) values2;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
		
	}

	public void addCourse(View v) {
		addToValues(course.getText().toString());

		System.out.println("test post");
		HashMap<String, String> postMap = new HashMap<String, String>();

		postMap.put("class_name", course.getText().toString());

		PostDispatcher postQuestionDispatcher = new PostDispatcher(
				"class_relationship", "class_relationship", postMap,
				getString(R.string.classesURL), this, this);
		System.out.println(postMap.toString());
		postQuestionDispatcher.execute();

		course.setText("");
		adapter.notifyDataSetChanged();

	}

	public void update(ArrayList<Object> s) {
		adapter.notifyDataSetChanged();
	}

	private class AddToAdapter extends
			AsyncTask<String, String, ArrayList<String>> {
		private ProgressDialog pDialog;
		Context        mContext;
		UpdateAdapters ua;

		public AddToAdapter(Context c, UpdateAdapters u) {
			this.mContext = c;
			ua = u;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(EnrollClassActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			HashMap<String, String> postMap = null;// = new HashMap<String,
			// String>();
			// postMap.put("null", "null");
			PostDispatcher postQuestionDispatcher = new PostDispatcher(
					"class_relationship", "classes", postMap,
					getString(R.string.classesURL), mContext, ua);
			// System.out.println(postMap.toString());
			postQuestionDispatcher.execute();

			return (ArrayList<String>) values2;
		}

		@Override
		protected void onPostExecute(ArrayList<String> values) {
			pDialog.dismiss();
			ArrayList<String> redo = values;// new ArrayList<String>();

			/*
			 * for (int i=0; i<values.size(); i++){ redo.add(values.get(i)); }
			 */

			listView = (ListView) findViewById(R.id.list);
			adapter = new EnrollClassAdapter(EnrollClassActivity.this, values);
			listView.setAdapter(adapter);

		}

	}

}
