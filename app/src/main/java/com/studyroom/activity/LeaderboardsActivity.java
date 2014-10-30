package com.studyroom.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.studyroom.R;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.connectivity.UpdateAdapters;
import com.studyroom.adapter.LeaderboardsAdapter;





import android.widget.ExpandableListView;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;


import android.view.MenuItem;
import android.view.View;





public class LeaderboardsActivity extends Activity implements UpdateAdapters {

    private ActionBarDrawerToggle mDrawerToggle;
    
    LeaderboardsAdapter listAdapter;
    ExpandableListView expListView;
    public static List<String> listDataHeader;
    public static HashMap<String, List<String>> listDataChild; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.leaderboards);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get ListView object from xml
	    
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        
        listAdapter = new LeaderboardsAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        new AddToAdapter(this, this).execute();


		super.onCreate(savedInstanceState);

        
        
       
		
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//          return true;
//        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
	}
	
	public void profileButton(View view) {
		Intent profileIntent = new Intent(LeaderboardsActivity.this, ProfilePageActivity.class);
		startActivity(profileIntent);
	}

	
	private class AddToAdapter extends AsyncTask<String, String, ArrayList<String>>{
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

			pDialog = new ProgressDialog(LeaderboardsActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... arg0) {
			// TODO Auto-generated method stub


			HashMap<String, String> postMap = null;// = new HashMap<String, String>();
			//postMap.put("null", "null");
			PostDispatcher postQuestionDispatcher = new PostDispatcher("leaderboard_entries", "leaderboards",
			                                                           postMap, getString(R.string.leaderboardsURL), mContext, ua);
			//System.out.println(postMap.toString());
			postQuestionDispatcher.execute();

			ArrayList<String> s = new ArrayList<String>();

			return s;
		}

		@Override
		protected void onPostExecute(ArrayList<String> v) {
			pDialog.dismiss();


			//	            expListView = (ExpandableListView) findViewById(R.id.lvExp);
			//	    	    listAdapter = new LeaderBoardsAdapter(LeaderBoards.this, listDataHeader, listDataChild);
			//	    	    expListView.setAdapter(listAdapter);


		}

	}

	@Override
	public void update(ArrayList<Object> s) {
		//adapter.notifyDataSetInvalidated();
		//listAdapter.notifyDataSetChanged();
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		listAdapter = new LeaderboardsAdapter(LeaderboardsActivity.this, listDataHeader, listDataChild);
		expListView.setAdapter(listAdapter);


	}

	@Override
	public void onResume() {
		super.onResume();

		//adapter.notifyDataSetChanged();
		//we don't like this because it creates a new feedadapter!
		//new AddToAdapter(this,this).execute();
	}
	
	/*
     * Preparing the list data
     */


}