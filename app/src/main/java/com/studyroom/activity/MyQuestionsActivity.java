package com.studyroom.activity;

import java.util.ArrayList;
import java.util.HashMap;





import com.studyroom.R;
import com.studyroom.connectivity.PostDispatcher;
import com.studyroom.application.StudyRoom;
import com.studyroom.connectivity.UpdateAdapters;
import com.studyroom.adapter.MyQuestionsAdapter;
import com.studyroom.model.Question;


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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MyQuestionsActivity extends Activity implements UpdateAdapters {
	private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private static boolean flag;
    private ActionBarDrawerToggle mDrawerToggle;
    
    ListView listView;
	MyQuestionsAdapter adapter;
    // Defined Array values to show in ListView
    public static ArrayList<Question> values = new ArrayList<Question>(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.my_questions);
		super.onCreate(savedInstanceState);
		// Get ListView object from xml
	    
		
		/*listView = (ListView) findViewById(R.id.list);
	    adapter = new FeedAdapter(this, values);
	    listView.setAdapter(adapter);*/
		
			new AddToAdapter(this, this).execute();

	    
	    
	    
	    // 2. App Icon 
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.activity_array);
 
        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);
 
                // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerListViewItems));
        
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        
        
        mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_navigation_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
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

        return super.onOptionsItemSelected(item);
	}

	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            
            
            switch(position) {
            case 0:
				//mainfeed
            	Intent intent4 = new Intent(MyQuestionsActivity.this, QuestionFeedActivity.class);
				startActivity(intent4);
				drawerLayout.closeDrawer(drawerListView);
				break;
			case 1:
				//my questions
				drawerLayout.closeDrawer(drawerListView);
				break;
			case 2:      
				//my answers
				Toast.makeText(MyQuestionsActivity.this, "Sorry, this feature is not available yet",
						Toast.LENGTH_LONG).show();
				drawerLayout.closeDrawer(drawerListView);
				break;

			

			case 3:
				//profile
				Intent intent = new Intent(MyQuestionsActivity.this, ProfilePageActivity.class);
				startActivity(intent);
				drawerLayout.closeDrawer(drawerListView);
				break;
				
			case 4:
				// profile
				Intent intent6 = new Intent(getApplicationContext(), SignInActivity.class);

				SharedPreferences prefs = StudyRoom.getContext().getSharedPreferences(
						"com.canal5.studyroom", Context.MODE_PRIVATE);

				SharedPreferences.Editor editor = prefs.edit();
			    editor.clear();
			    editor.commit();
				
				String auth_token = prefs.getString("auth_token", "auth_token does not exist");
				Log.e(this.getClass().getSimpleName(), "auth_token = " + auth_token);
				
				startActivity(intent6);
				drawerLayout.closeDrawer(drawerListView);
				break;
			default:
				
				break;
			}
            
            
            
 
        }
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

			pDialog = new ProgressDialog(MyQuestionsActivity.this);
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
			PostDispatcher postQuestionDispatcher = new PostDispatcher("questions", "myQuestions",
			                                                           postMap, getString(R.string.myQuestionsURL), mContext, ua);
			//System.out.println(postMap.toString());
			postQuestionDispatcher.execute();

			ArrayList<String> s = new ArrayList<String>();

			return s;
		}

		@Override
		protected void onPostExecute(ArrayList<String> v) {
			pDialog.dismiss();
	            
	            
	            /*for (int i=0; i<values.size(); i++){
	            	redo.add(values.get(i));
	            }*/


			listView = (ListView) findViewById(R.id.list);
			adapter = new MyQuestionsAdapter(MyQuestionsActivity.this, values);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					Intent i = new Intent(MyQuestionsActivity.this, AnswerPageActivity.class);
					System.out.println("mmmmmmmmmm" + values.get(position).getID());
					i.putExtra("question_id", values.get(position).getID());
					startActivity(i);

				}
			});

		}

	}

	@Override
	public void update(ArrayList<Object> s) {
		//adapter.notifyDataSetInvalidated();
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onResume() {
		super.onResume();

		//adapter.notifyDataSetChanged();
		//we don't like this because it creates a new feedadapter!
		//new AddToAdapter(this,this).execute();
	}


}
