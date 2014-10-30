package com.studyroom.application;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

public class StudyRoom extends Application {
	 private static StudyRoom s_instance;

	    public StudyRoom ()
	    {
	    	
	        s_instance = this;
	        
			

	    }
	    
	    public static void setPreference(String key, String value){
	    	SharedPreferences prefs = s_instance.getSharedPreferences( "com.canal5.studyroom", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(key, value);
			editor.commit();
	    }
	    
	    public static SharedPreferences getPreferences(){
	    	return s_instance.getSharedPreferences("com.canal5.studyroom", Context.MODE_PRIVATE);
	    }

	    public static Context getContext()
	    {
	        return s_instance;
	    }

	    public static String staticGetString(int resId)
	    {
	    	
	    	
	        return s_instance.getString(resId);   
	        
	    }
	    
		public static String encodePhoto(String fileUriString) {
			Uri fileUri = Uri.parse(fileUriString);
			Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); 
			byte[] b = baos.toByteArray();

			String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

			return encodedImage;
		}

		public static void startNewActivity(Activity CurrentActivity,
				Class<?> NextActivity) {
			// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(CurrentActivity, NextActivity);
				CurrentActivity.startActivity(myIntent);
			
		}

		public static Resources staticGetResources() {
			// TODO Auto-generated method stub
			return s_instance.getResources();
		}

		
}
