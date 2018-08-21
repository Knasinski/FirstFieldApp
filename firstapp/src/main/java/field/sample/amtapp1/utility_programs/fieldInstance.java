package field.sample.amtapp1.utility_programs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import field.sample.amtapp1.app.welcome.HomeController;


public class fieldInstance {
	private static final Logger logger = LoggerFactory.getLogger(fieldInstance.class);
	public boolean Good = false;
	public StringBuffer result = new StringBuffer();
	public int http_status=0;
	public String errorMessage = null;
	public String urlString = null;
	
	public fieldInstance(String urlString) throws IOException {
		HttpURLConnection con = null;
		this.urlString = urlString;
		
		try {
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();
			con.connect();
		} 
		catch (IOException ioex) {
			errorMessage = "openConnection : " + urlString;
			logger.warn(errorMessage, ioex);
			logIt();
			return;
		}
		
		InputStream in = null;
		
		//Josh - I am getting the status here, it is public
		//		 I plan to send fieldInstance to you during initialization
		http_status = con.getResponseCode();
		
		try {
			in = con.getInputStream();
		} 
		catch (IOException ioex) {
			errorMessage = "getInputStream : " + urlString;
			logger.warn(errorMessage, ioex);
		
			try {
				con.disconnect();
			}
			catch(Exception ex) {
				errorMessage = "in.close() Exception : " + ex.toString();
				logger.warn("in.close()", ex);
			}

			logIt();
			return;
		}
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line;
			
			while((line = br.readLine()) != null)
				{
				result.append(line);
				result.append("\n");
				}
		} 
		catch (IOException ioex) {
			logger.warn("readLine", ioex);
			
			try {
				in.close();
			}
			catch(Exception ex) {
				errorMessage = "in.close() Exception : " + ex.toString();
				logger.warn("in.close()", ex);
			}
			
			try {
				con.disconnect();
			}
			catch(Exception ex) {
				errorMessage = "in.close() Exception : " + ex.toString();
				logger.warn("in.close()", ex);
			}

			logIt();
			return;
		}
		
		try {
			in.close();
		}
		catch(Exception ex) {
			errorMessage = "in.close() Exception : " + ex.toString();
			logger.warn("in.close()", ex);
		}
		
		try {
			con.disconnect();
		}
		catch(Exception ex) {
			errorMessage = "in.close() Exception : " + ex.toString();
			logger.warn("in.close()", ex);
		}
		
		Good = true;
		logIt();
	}
	
	private void logIt() {
		if (HomeController.numDebugged < HomeController.fiDebug.length) {
			HomeController.fiDebug[HomeController.numDebugged] = this;
			++HomeController.numDebugged;
		}
	}
}
