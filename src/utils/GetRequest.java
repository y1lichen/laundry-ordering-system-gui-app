package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRequest {
	private int responseCode = 0;
	private String content;
	private HttpURLConnection connection;

	public int getResponseCode() {
		return responseCode;
	}

	public String getContent() {
		return content;
	}

	public void sendRequest(String urlString) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			responseCode = connection.getResponseCode();
			if (responseCode < 300) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					stringBuilder.append(inputLine);
				}
				in.close();
				content = stringBuilder.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		connection.disconnect();
	}
}
