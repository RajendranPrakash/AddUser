package com.example.myproject.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionToURL {

	public String fetchUserInformation(URL url) throws IOException {
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		return response.toString();
		
		/*inputLine = response.toString();
		JSONObject jsonResponse, jsonExtract = null;
		try {
			jsonResponse = new JSONObject(inputLine);
			jsonExtract = new JSONObject();
			jsonExtract.put("name", jsonResponse.getString("name") + " " + jsonResponse.getString("surname"));
			jsonExtract.put("email", jsonResponse.getString("email"));
			jsonExtract.put("userName", jsonResponse.getString("email"));
			jsonExtract.put("password", jsonResponse.getString("password"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonExtract;*/
	}

}
