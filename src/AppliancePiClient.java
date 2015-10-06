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
import com.pi4j.io.gpio.*;

public class AppliancePiClient {
	public static final String SITE = "xtac.tk";
	public static final int PORT = 8081;
	public static final String SET_STATE_URL = "http://" + SITE + ":" + PORT + "/appliance/v1/setstate";
	public static final String GET_STATE_URL = "http://" + SITE + ":" + PORT + "/appliance/v1/getstate";
	public static final int SLEEP_TIME = 10000;
	public static String app = null;
	public static final Pin PIN = RaspiPin.GPIO_01;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Must supply one argument which is the name of the appliance");
			System.exit(1);
		}

		app = args[0];
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(PIN, "Appliance");
		pin.setShutdownOptions(true, PinState.LOW);
		while (true) {
			try {
				int intState = getState(app);
				if (intState == -1)
					continue;
				if (intState > 0)
					pin.high();
				else
					pin.low();
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) { e.printStackTrace(); }
		}

	}
	public static int getState(String app) {
		int state = -1;
		try {
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
			JSONObject json = new JSONObject(result.toString());
			state = (json.getInt(app));
		} catch (Exception e) { e.printStackTrace(); }
		return state;
	}
}

/*
	 String app = args[0];
	 String state = args[1];
	 HttpClient client = HttpClientBuilder.create().build();
	 HttpPost post = new HttpPost(SET_STATE_URL);

	 List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	 urlParameters.add(new BasicNameValuePair("app", app));
	 urlParameters.add(new BasicNameValuePair("state", state));

	 post.setEntity(new UrlEncodedFormEntity(urlParameters));

	 HttpResponse response = client.execute(post);
	 */
