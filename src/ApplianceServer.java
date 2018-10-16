import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.json.*;

public class ApplianceServer {
	public static final int API_PORT = 8081;
	public static final String SETTINGS_FILE = "appliances.settings";
	public static JSONObject jsonSettings;

	public static void main(String[] args) throws Exception {
		loadSettings();
		Server server = new Server(API_PORT);

        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
        filterHolder.setInitParameter("allowedOrigins", "*");
        filterHolder.setInitParameter("allowedMethods", "GET, POST");

		ServletContextHandler handler = new ServletContextHandler();
		handler.addServlet(GetStateServlet.class, "/appliance/v1/getstate");
		handler.addServlet(SetStateServlet.class, "/appliance/v1/setstate");
        handler.addFilter(filterHolder, "/*", null);

		server.setHandler(handler);    
		server.start();
		server.join();
	}

	public static void loadSettings() {
		BufferedReader reader = null;
		try {
			jsonSettings = new JSONObject();
			File settingsFile = new File(SETTINGS_FILE);
			if (!settingsFile.exists())
				return;
			reader = new BufferedReader(new FileReader(settingsFile));
			String strSettings = reader.readLine();
			jsonSettings = new JSONObject(strSettings);
		}
		catch (Exception e) { e.printStackTrace(); }
		finally {
			try {
				if (reader != null) reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveSettings() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(SETTINGS_FILE);
			String strSettings = jsonSettings.toString();
			writer.println(strSettings);
			writer.flush();
		}
		catch (Exception e) { e.printStackTrace(); }
		finally {
			try {
				if (writer != null) writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void updateSetting(String app, int state) {
		try {
			jsonSettings.put(app,state);
			saveSettings();
		} catch (Exception e) { e.printStackTrace(); }
	}

	@SuppressWarnings("serial")
		public static class SetStateServlet extends HttpServlet {
			protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				System.out.println(request.getParameter("state"));
                Enumeration enAttr = request.getParameterNames();
                System.out.println("hello1"); 
                String app = request.getParameter("app");
				int state = Integer.parseInt(request.getParameter("state"));
				updateSetting(app, state);
				//response.addHeader("Access-Control-Allow-Origin", "*");
				//response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				PrintWriter writer = response.getWriter();
				writer.flush();
				writer.close();
			}
		}

	@SuppressWarnings("serial")
		public static class GetStateServlet extends HttpServlet {
			protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				//response.addHeader("Access-Control-Allow-Origin", "*");
				//response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				PrintWriter writer = response.getWriter();
				writer.println(jsonSettings.toString());
				writer.flush();
				writer.close();
			}
		}

}
