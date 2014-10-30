package com.studyroom.activity;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.studyroom.R;
import com.studyroom.adapter.QuestionFeedAdapter;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;
import com.studyroom.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionFeedActivity extends Activity implements UpdateAdapters,
		OnNavigationListener {
	ListView            listView;
	QuestionFeedAdapter adapter;
	ArrayAdapter        spinneradapter;

	private String[]              drawerListViewItems;
	private ListView              drawerListView;
	private DrawerLayout          drawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	ActionBar actionBar;

	// Defined Array values to show in ListView
	public static ArrayList<String>   values    = new ArrayList<String>();
	public static ArrayList<Question> questions = new ArrayList<Question>();
	public static ArrayList<String>   classes   = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Get ListView object from xml
		setContentView(R.layout.question_feed);

		new AddToAdapter(this, this).execute("questions");

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		new AddToAdapter(this, this).execute("class_relationships");
		// use default spinner item to show options in spinner
		spinneradapter = new ArrayAdapter<String>(this,
		                                          R.layout.feed_filter_fragment_row, classes);

		spinneradapter
				.setDropDownViewResource(R.layout.feed_filter_fragment);


		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(spinneradapter, this);

		// 2. App Icon
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// get list items from strings.xml
		drawerListViewItems = getResources().getStringArray(
				R.array.activity_array);

		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		drawerListView.setAdapter(new ArrayAdapter<String>(this,
		                                                   R.layout.drawer_list_item, drawerListViewItems));

		drawerListView.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		                                          drawerLayout, /* DrawerLayout object */
		                                          R.drawable.ic_navigation_drawer, /* nav drawer icon to replace 'Up' caret */
		                                          R.string.drawer_open, /* "open drawer" description */
		                                          R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(getTitle());
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(R.string.NavDrawerTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	public void profileButton(View view) {
		Intent profileIntent = new Intent(QuestionFeedActivity.this,
		                                  ProfilePageActivity.class);
		startActivity(profileIntent);
	}

	public static List<String> values2 = new ArrayList<String>() {
		{

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_feed_menu, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		switch (item.getItemId()) {
			case R.id.action_post:
				Intent postQuestionIntent = new Intent(QuestionFeedActivity.this,
				                                       PostQuestionActivity.class);
				startActivity(postQuestionIntent);
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();

		// adapter.notifyDataSetChanged();
		// we don't like this because it creates a new feedadapter!
		new AddToAdapter(this, this).execute("questions");
		new AddToAdapter(this, this).execute("class_relationships");
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
		                        long id) {

			switch (position) {
				case 0:
					// mainfeed
					drawerLayout.closeDrawer(drawerListView);
					break;
				case 1:
					// my questions
					Intent intent2 = new Intent(QuestionFeedActivity.this,
					                            MyQuestionsActivity.class);
					startActivity(intent2);
					drawerLayout.closeDrawer(drawerListView);
					break;
				case 2:
					// my answers
					Toast.makeText(QuestionFeedActivity.this,
					               "Sorry, this feature is not available yet",
					               Toast.LENGTH_LONG).show();
					drawerLayout.closeDrawer(drawerListView);
					break;

				case 3:
					// profile
					Intent intent5 = new Intent(QuestionFeedActivity.this,
					                            ProfilePageActivity.class);
					startActivity(intent5);
					drawerLayout.closeDrawer(drawerListView);
					break;
				case 4:
					// profile
					Intent intent6 = new Intent(getApplicationContext(),
					                            SignInActivity.class);

					SharedPreferences prefs = getSharedPreferences(
							"com.canal5.studyroom", Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = prefs.edit();
					editor.clear();
					editor.commit();

					String auth_token = prefs.getString("auth_token",
					                                    "auth_token does not exist");
					Log.e(this.getClass().getSimpleName(), "auth_token = "
							+ auth_token);

					startActivity(intent6);
					drawerLayout.closeDrawer(drawerListView);
					break;
				default:

					break;
			}

		}
	}

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

			pDialog = new ProgressDialog(QuestionFeedActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			option = arg0[0];
			HashMap<String, String> postMap = null;
			if (arg0[0].equals("questions")) {

				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						arg0[0], arg0[0], postMap,
						getString(R.string.questionsURL), mContext, ua);
				postQuestionDispatcher.execute();
			} else {
				PostDispatcher postQuestionDispatcher = new PostDispatcher(
						arg0[0], arg0[0], postMap,
						getString(R.string.classesURL), mContext, ua);
				postQuestionDispatcher.execute();
			}

			return (ArrayList<String>) values2;
		}

		@Override
		protected void onPostExecute(ArrayList<String> values) {
			pDialog.dismiss();

			if (option.equals("questions")) {
				listView = (ListView) findViewById(R.id.lvQuestionsList);

				listView.setEmptyView(findViewById(R.id.empty));
				adapter = new QuestionFeedAdapter(QuestionFeedActivity.this,
				                                  questions);
				listView.setAdapter(adapter);
			} else {
				spinneradapter = new ArrayAdapter<String>(
						QuestionFeedActivity.this,
						android.R.layout.simple_spinner_item, classes);

			}

		}

	}

	@Override
	public void update(ArrayList<Object> s) {
		adapter.notifyDataSetChanged();
		listView = (ListView) findViewById(R.id.lvQuestionsList);
		adapter = new QuestionFeedAdapter(QuestionFeedActivity.this, questions);
		listView.setAdapter(adapter);
		actionBar.setListNavigationCallbacks(spinneradapter, this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {

				Intent i = new Intent(QuestionFeedActivity.this,
				                      AnswerPageActivity.class);
				i.putExtra("question_id", questions.get(position).getID());
				startActivity(i);

			}
		});

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		String s = classes.get(itemPosition);

		adapter.getFilter().filter(s);
		adapter.notifyDataSetChanged();
		return true;
	}

}
