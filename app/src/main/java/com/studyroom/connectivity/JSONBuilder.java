package com.studyroom.connectivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.studyroom.application.StudyRoom;

public class JSONBuilder {

	JSONObject obj = new JSONObject();
	JSONObject postObject = new JSONObject();
	HashMap<String, String> postMap;
	Double latitude, longitude = null;
	Context context;
	String key;
	String requestURL;

	public JSONBuilder(String key, HashMap<String, String> postMap,
			String requestURL, Context context) {
		this.postMap = postMap;
		this.requestURL = requestURL;
		this.context = context;
		this.key = key;
	}

	protected JSONObject build() {
		if(postMap == null){
			return null;
		}
		Iterator<Entry<String, String>> it = postMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = (Entry<String, String>) it
					.next();
			try {
				if (pairs.getKey().equals("photo")) {
					postObject.put(pairs.getKey().toString(),
							(StudyRoom.encodePhoto(pairs.getValue().toString())));
				} else {
					if (pairs.getValue() != null) {
						postObject.put(pairs.getKey().toString(), pairs
								.getValue().toString());
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			it.remove(); // avoids a ConcurrentModificationException
		}

		try {
			obj.put(key, postObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

}
