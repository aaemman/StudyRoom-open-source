package com.studyroom.connectivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class PostDispatcher extends
		AsyncTask<String, Integer, ArrayList<Object>> {

	private Context context;
	private String requestURL;
	private HashMap<String, String> postMap;
	private JSONObject postObject;
	private final String key, responseType;
	private UpdateAdapters ua;

	public PostDispatcher(String key, String responseType,
	                      HashMap<String, String> postMap, String requestURL,
	                      Context context, UpdateAdapters u) {
		this.postMap = postMap;
		this.context = context;
		this.requestURL = requestURL;
		this.key = key;
		this.responseType = responseType;
		ua = u;

	}

	@Override
	protected ArrayList<Object> doInBackground(String... params) {

		JSONBuilder postJSON = new JSONBuilder(key, postMap, requestURL,
		                                       context);
		postObject = postJSON.build();

		JSONSendReceiver srJSON = new JSONSendReceiver(requestURL, postObject,
				context);

		// JSONResponseHandler jsonResponseHandler = new JSONResponseHandler(new
		// CustomJSONParser());

		// Object[] responseObjectArray =
		// null;//jsonResponseHandler.split(srJSON.send(), key);

		String s = srJSON.send();
//		System.out.println(s);
		ArrayList<Object> s1 = JSONParser
				.setClassAdapterValues(s, responseType);

		return s1;
	}

	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		Log.i(this.getClass().getSimpleName(),
				"----------------- ENTERING OnPostExecute() -----------------");
		Log.i(this.getClass().getSimpleName(), "result => " + result.toString());
		if (ua != null)
			ua.update(result);
	}

	

}