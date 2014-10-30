package com.studyroom.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class JSONSendReceiver {

	private static String response = null;
	private static HttpURLConnection conn;
	private static OutputStream os;

	private JSONObject obj;
	private String requestURL;
	private Context context;

	InputStream responseStream = null;
	int responseCode = -1;

	private static URL url;

	public JSONSendReceiver(String requestURL, JSONObject obj, Context context) {
		this.obj = obj;
		this.requestURL = requestURL;
		this.context = context;
	}

	private void setConnectionHeaders(HttpURLConnection conn,
			String requestMethod) {
		if (requestMethod=="POST")
			conn.setDoOutput(true);

		try {
			conn.setRequestMethod(requestMethod);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(this.getClass().getSimpleName(), "Setting Content-Type Header");
		conn.setRequestProperty("Content-Type", "application/json");

		Log.i(this.getClass().getSimpleName(), "Setting Accept Header");
		conn.setRequestProperty("Accept", "application/json");

		Log.i(this.getClass().getSimpleName(), "Setting Authorization Header");
		conn.setRequestProperty("Authorization",
				"0009bdeb1b88626c80cd286da51bc2c3".trim());

		Log.i(this.getClass().getSimpleName(),
				"Setting User Authorization Header");

		SharedPreferences prefs = context.getSharedPreferences(
				"com.canal5.studyroom", Context.MODE_PRIVATE);

		
		String auth_token = prefs.getString("auth_token", "");
		//String auth_token = Canal5.staticGetString(R.string.auth_token);
		

		//String auth_token = "7d25f561de64d9161b2bd83fad7b2d0e";

		
		System.out.println("***************"+auth_token);
		
		
		if (!(auth_token.equals(""))) {
			
			conn.setRequestProperty("User_Authorization", auth_token);
		} else {
			Log.e(this.getClass().getSimpleName(),
					"ERROR: auth token is not set or is invalid");
		}
		Log.i(this.getClass().getSimpleName(), "All headers set");
	}

	public String postRequest() {
		if (context != null) {
			try {
				Log.i(this.getClass().getSimpleName(), "Starting Post Request");
				Log.i(this.getClass().getSimpleName(), "    URL = "
						+ requestURL);
				url = new URL(requestURL);
				conn = (HttpURLConnection) url.openConnection();

				Log.i(this.getClass().getSimpleName(),
						"Setting Connection Headers");
				setConnectionHeaders(conn, "POST");

				Log.i(this.getClass().getSimpleName(), "creating output stream");
				os = conn.getOutputStream();

				Log.i(this.getClass().getSimpleName(),
						"creating output stream writer");
				OutputStreamWriter osw = new OutputStreamWriter(os);

				Log.i(this.getClass().getSimpleName(),
						"writing to output stream");
				osw.write(obj.toString());

				Log.i(this.getClass().getSimpleName(), "flushing output stream");
				osw.flush();

				Log.i(this.getClass().getSimpleName(),
						"Getting Request Response");
				response = postResponse(conn);

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}
		Log.i(this.getClass().getSimpleName(), "the Context provided to the JSONsedReceiver was null");
		return null;
	}

	public String postResponse(HttpURLConnection c) {
		StringBuilder res = new StringBuilder();
		String output;
		String response = null;
		BufferedReader br;

		try {
			Log.i(this.getClass().getSimpleName(), " Initializing input stream");

			responseCode = c.getResponseCode();
			responseStream = c.getInputStream();
			
			br = new BufferedReader(new InputStreamReader(responseStream));
			while ((output = br.readLine()) != null) {
				res.append(output);
			}

			// Log.i(this.getClass().getSimpleName(), "Full response received");
			response = res.toString();

			// Log.i(this.getClass().getSimpleName(),
			// "Disconnecting from host");
			c.disconnect();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			Log.e(this.getClass().getSimpleName(), "ERROR: could not make a connection. Response Code: " + responseCode );
//			if(LoginActivity.progressDialog.getWindow() != null){
//				LoginActivity.progressDialog.dismiss();
//			}
			
			
			
			e.printStackTrace();
		}

		// Log.i(this.getClass().getSimpleName(), "Printing response");
		// System.out.println(response);
		return response;
	}

	public String getRequest() {
		try {
			url = new URL(requestURL);
			conn = (HttpURLConnection) url.openConnection();
			setConnectionHeaders(conn, "GET");

			response = getResponse(conn);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(response);
		return response;

	}

	public String getResponse(HttpURLConnection c) {
		String output;
		StringBuilder sb = new StringBuilder();
		BufferedReader br;

		try {
			br = new BufferedReader(new InputStreamReader((c.getInputStream())));
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.disconnect();

		return sb.toString();
	}

	public String send() {

		if (this.obj == null) {
			Log.i(this.getClass().getSimpleName().toString(),
					"Calling Get Request");
			return getRequest();
		} else {
			Log.i(this.getClass().getSimpleName().toString(),
					"Calling Post Request");
			return postRequest();

		}

	}
}