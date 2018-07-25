package field.sample.amtapp1.utility_programs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class fieldInstance {
	private static final Logger logger = LoggerFactory.getLogger(fieldInstance.class);
	public boolean Good = false;
	public StringBuffer result = new StringBuffer();
	
	public fieldInstance(String urlString) {
		HttpURLConnection con = null;
		
		try {
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();
			con.connect();
		} 
		catch (IOException ioex) {
			logger.warn("openConnection : " + urlString, ioex);
			return;
		}
		
		InputStream in = null;
		
		try {
			in = con.getInputStream();
		} 
		catch (IOException ioex) {
			logger.warn("getInputStream : " + urlString, ioex);
		
			try {
				con.disconnect();
			}
			catch(Exception ex) {
				logger.warn("in.close()", ex);
			}
			
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
				logger.warn("in.close()", ex);
			}
			
			try {
				con.disconnect();
			}
			catch(Exception ex) {
				logger.warn("in.close()", ex);
			}
			
			return;
		}
		
		try {
			in.close();
		}
		catch(Exception ex) {
			logger.warn("in.close()", ex);
		}
		
		try {
			con.disconnect();
		}
		catch(Exception ex) {
			logger.warn("in.close()", ex);
		}
		
		Good = true;
	}
}
