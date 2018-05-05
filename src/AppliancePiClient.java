import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.config.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;
import org.json.*;
import com.pi4j.io.gpio.*;

public class AppliancePiClient {
	public static final String SITE = "xtac.ml";
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
				if (intState > 0) {
					pin.high();
				}
				else {
					pin.low();
				}
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) { e.printStackTrace(); }
		}

	}
	public static int getState(String app) {
		int state = -1;
		HttpGet get = null;
		HttpResponse response = null;
		BufferedReader reader = null;

		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5*1000).build();
			HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			get = new HttpGet(GET_STATE_URL);

			response = client.execute(get);
			reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			line = reader.readLine();
			result.append(line);

			JSONObject json = new JSONObject(result.toString());
			state = (json.getInt(app));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}	
		return state;
	}
}

