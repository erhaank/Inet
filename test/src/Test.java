import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Test {
	
	public Test() {
		String url = "http://api.sl.se/api2/TravelplannerV2/trip.json?key=379fb41899c14bf79e3db966b1c70ce1&originId=Sollentuna&destId=Norrviken";
		String parameters = "key=379fb41899c14bf79e3db966b1c70ce1&originId=Sollentuna&destId=Norrviken";
		String response = "";
		try {
			response = sendRequest(url, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		parse(response);
	}

	public static void main(String[] args) {
		new Test();
	}

	public String sendRequest(String url, String parameters) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();

		int responseCode = connection.getResponseCode();

		BufferedReader in = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
	
	public String parse(String json) {
		JSONObject obj;
		try {
			obj = new JSONObject(json);
			JSONArray tripList = obj.getJSONObject("TripList").getJSONArray("Trip");
			for (int i = 0; i < tripList.length(); i++) {
				JSONObject trip = tripList.getJSONObject(i);
				System.out.println("*** New Trip ***");
				System.out.println("Duration: "+trip.getString("dur")+" min");
				JSONObject leg = trip.getJSONObject("LegList").getJSONObject("Leg");
				System.out.println("Name: "+leg.getString("name"));
				System.out.println("From: "+leg.getJSONObject("Origin").getString("name"));
				System.out.println("To: "+leg.getJSONObject("Destination").getString("name"));
				System.out.println();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

