import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.json.*;

public class ApplianceClient {
	public static final String CLIENT_ID = "1";
	public static final String GET_STATUS_URL = "http://localhost:8081/api/v1/appliances/getState?client="+CLIENT_ID;
	public static final int SLEEP_TIME = 30000;
	public HashMap <String, GpioController> nameToPinMap;
	public HashMap <String, PinState> nameToStateMap;

	public static void main(String[] args) {
		try {
			
			/*			
							HttpClient client = HttpClientBuilder.create().build();
							HttpPost post = new HttpGet(url);

							List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
							urlParameters.add(new BasicNameValuePair("clientid", CLIENT_ID));
							urlParameters.add(new BasicNameValuePair("fen", "2r4k/p2p1ppp/8/8/2B5/8/P1q1RPPP/4R1K1 w - - 0 21"));

							post.setEntity(new UrlEncodedFormEntity(urlParameters));

							HttpResponse response = client.execute(get);

							BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

							StringBuffer result = new StringBuffer();
							String line = "";
							while ((line = rd.readLine()) != null) {
							result.append(line);
							}

							System.out.println(result.toString());

							Thread.sleep(5000);

							url = "http://localhost:8080/stockfish/geteval?clientid=" + CLIENT_ID;
							*/

		} catch (Exception e) { e.printStackTrace(); }


	}

	private class GetStatusThread extends Thread {

		public void run () {
			while (true) { 
				HttpClient client = HttpClientBuilder.create().build();
				HttpGet get = new HttpGet(url);
				response = client.execute(get);
				rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				result = new StringBuffer();
				line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				System.out.println(result.toString());
				JSONTokener jtok = new JSONTokener(result.toString());

				try {
					Thread.sleep(SLEEP_TIME);
				} catch (Exception e) { e.printStackTrace(); }

			}
		}

	}
}
