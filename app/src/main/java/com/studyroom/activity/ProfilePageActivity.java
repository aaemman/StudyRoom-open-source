package com.studyroom.activity;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.TextView;
import android.widget.Toast;

import com.studyroom.R;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;
import com.studyroom.model.User;

public class ProfilePageActivity extends Activity implements UpdateAdapters {
	private String[] drawerListViewItems;
	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	String resultString;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_page);

		new AddToAdapter(this, this).execute();

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

	public void badgesButtonOnClick(View view) {
		Toast.makeText(this, "Sorry,  This feature is not available yet",
				Toast.LENGTH_LONG);
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

	public void classesButton(View view) {

		Log.i(this.getClass().getSimpleName(), "classes Button Was Pushed");
		Intent intent1 = new Intent(ProfilePageActivity.this,
				EnrollClassActivity.class);
		startActivity(intent1);
	}

	public void leaderBoardsButton(View view) {

		Log.i(this.getClass().getSimpleName(), "leaderboards Button Was Pushed");
		Intent intent1 = new Intent(ProfilePageActivity.this,
				LeaderboardsActivity.class);
		startActivity(intent1);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {

			switch (position) {
			case 0:
				// main feed
				Intent intent1 = new Intent(ProfilePageActivity.this,
						QuestionFeedActivity.class);
				startActivity(intent1);
				drawerLayout.closeDrawer(drawerListView);
				break;
			case 1:
				// my questions
				Intent intent2 = new Intent(ProfilePageActivity.this,
						MyQuestionsActivity.class);
				startActivity(intent2);
				drawerLayout.closeDrawer(drawerListView);
				break;
			case 2:
				// my answers
				Toast.makeText(ProfilePageActivity.this,
						"Sorry, this feature is not available yet",
						Toast.LENGTH_LONG).show();
				drawerLayout.closeDrawer(drawerListView);
				break;

			case 3:
				// profile
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
		ArrayList<String> responseList = new ArrayList<String>();

		public AddToAdapter(Context c, UpdateAdapters ua) {
			this.mContext = c;
			this.ua = ua;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ProfilePageActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			HashMap<String, String> postMap = null;

			PostDispatcher getUserDispatcher = new PostDispatcher("User",
			                                                      "profile", postMap, getString(R.string.usersURL), mContext,
			                                                      ua);

			getUserDispatcher.execute();

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

		if (s.size() != 0) {
			// update UI
			TextView tvName = (TextView) findViewById(R.id.tvProfilePageUserName);
			TextView tvJoinDate = (TextView) findViewById(R.id.tvProfilePageUserJoinDate);
			TextView tvTotalPoints = (TextView) findViewById(R.id.tvProfilePageTotalPointsCountValue);
			TextView tvQuestionCount = (TextView) findViewById(R.id.tvProfilePageQuestionsPostedCountValue);
			TextView tvAnswerCount = (TextView) findViewById(R.id.tvProfilePageAnswersPostedCountValue);
			TextView tvCorrectAnswerCount = (TextView) findViewById(R.id.tvProfilePageCorrectAnswersCountValue);
			TextView tvPercentCorrectAnswerCount = (TextView) findViewById(R.id.tvProfilePagePercentCorrectAnswersCountValue);

			tvName.setText(((User) s.get(0)).getName());
			tvJoinDate.setText(((User) s.get(0)).getJoinDate());
			tvTotalPoints.setText(((User) s.get(0)).getTotalPoints());
			tvQuestionCount.setText(((User) s.get(0)).getQuestionsCount());
			tvAnswerCount.setText(((User) s.get(0)).getAnswerCount());
			tvCorrectAnswerCount.setText(((User) s.get(0))
					                             .getCorrectAnswerCount());
			tvPercentCorrectAnswerCount.setText(((User) s.get(0))
					                                    .getPercentCorrectAnswer());
		}
	}

}
