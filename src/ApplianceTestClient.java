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

public class ApplianceTestClient {
	public static final String SET_STATE_URL = "http://localhost:8081/appliance/v1/setstate";
	public static final String GET_STATE_URL = "http://localhost:8081/appliance/v1/getstate";

	public static void main(String[] args) {
		try {
			if (args.length != 2 && args.length !=0) {
				System.err.println("Incorrect usage. Must include no arguments or two argument specifying the appliance name (String) and state (Integer)");
				System.exit(1);
			}

			if (args.length == 2) {
				String app = args[0];
				String state = args[1];
				HttpClient client = HttpClientBuilder.create().build();
				HttpPost post = new HttpPost(SET_STATE_URL);

				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
				urlParameters.add(new BasicNameValuePair("app", app));
				urlParameters.add(new BasicNameValuePair("state", state));

				post.setEntity(new UrlEncodedFormEntity(urlParameters));

				HttpResponse response = client.execute(post);
			}

			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(GET_STATE_URL);

			HttpResponse response = client.execute(get);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());
			JSONTokener jtok = new JSONTokener(result.toString());

		} catch (Exception e) { e.printStackTrace(); }


	}
}
