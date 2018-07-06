package field.sample.amtapp1.domain.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class CommonDataServiceImpl implements CommonDataService {
	private static final Logger logger = LoggerFactory.getLogger(CommonDataServiceImpl.class);
	private static final String BASE_URL = "http://192.168.99.100:8083/field_api/v3/class/";
	private static final String InstanceStr = "/instance/";
	
	public String getInstances(String classId) {
		String url = BASE_URL + classId;
		logger.debug("getInstances : " + url);
		return getJson(url);
	}
	
	public String getInstance(String classId, String instanceId) {
		String url = BASE_URL + classId + InstanceStr + instanceId;
		logger.debug("getInstance : " + url);
		return getJson(url);
	}
	
	public String getLatest(String classId, String instanceId) {
		String url = BASE_URL + classId + InstanceStr + instanceId + "/latest";
		logger.debug("getLatest : " + url);
		return getJson(url);
	}
		
	public String getHistory(String classId, String instanceId) {
		String url = BASE_URL + classId + InstanceStr + instanceId + "/history";
		logger.debug("getHistory : " + url);
		return getJson(url);
	}
		
	public int getCount(String classId, String instanceId) {
		String url = BASE_URL + classId + InstanceStr + instanceId + "/count";
		logger.debug("getCount : " + url);
		String rc = getJson(url);
		
		if (rc.contains("count") && rc.contains(":")) {
			try {
				int k = Integer.parseInt(rc);
				
				return k;
				} catch (Exception e) {
					logger.warn("failedConvertToInt : " + rc);
				}
		}
		
		return 0;
	}
		
	private String getJson(String urlString) {
		StringBuffer result = new StringBuffer();
		HttpURLConnection con;
		
		try {
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();
			con.connect();
		} 
		catch (IOException ioex) {
			logger.warn("openConnection : " + urlString, ioex);
			return null;
		}
		
		InputStream in;
		
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
			
			return null;
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
			return null;
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
		
		return result.toString();
		}
	}
