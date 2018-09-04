package field.sample.amtapp1.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import field.sample.amtapp1.app.welcome.HomeController;
import field.sample.amtapp1.utility_programs.fieldInstance;


@Service
public class CommonDataServiceImpl implements CommonDataService {
	private static final Logger logger = (HomeController.debugLogging) ? LoggerFactory.getLogger(CommonDataServiceImpl.class) : null;
	private static String BaseUrl = "";
	private static final String InstanceStr = "/instance/";
	
	public StringBuffer getInstances(String classId) {
		checkBaseUrl();
		String url = BaseUrl + classId;
		if (HomeController.debugLogging)
			logger.debug("getInstances : " + url);
		
		return getJson(url);
	}
	
	public StringBuffer getInstance(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId;
		if (HomeController.debugLogging)
			logger.debug("getInstance : " + url);
		return getJson(url);
	}
	
	public StringBuffer getLatest(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/latest";
		if (HomeController.debugLogging)
			logger.debug("getLatest : " + url);
		return getJson(url);
	}
		
	public StringBuffer getHistory(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/history";
		if (HomeController.debugLogging)
			logger.debug("getHistory : " + url);
		return getJson(url);
	}
	
	public StringBuffer getRelations(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/relations";
		if (HomeController.debugLogging)
			logger.debug("getRelations : " + url);
			
		return getJson(url);
	}
	
	public StringBuffer getMoments(String classId, String instanceId) {
		String url = BaseUrl + classId + InstanceStr + instanceId + "/moments";
		if (HomeController.debugLogging)
			logger.debug("getMoments : " + url);
		return getJson(url);
	}
		
	public int getCount(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/count";
		if (HomeController.debugLogging)
			logger.debug("getCount : " + url);
		StringBuffer rc = getJson(url);
		
		if (rc.toString().contains("count") && rc.toString().contains(":")) {
			try {
				String s = rc.substring(rc.indexOf(":") + 1);
				s = s.substring(0, s.indexOf("}"));
				
				int k = Integer.parseInt(s);
				
				return k;
				} catch (Exception e) {
					if (HomeController.debugLogging)
						logger.warn("failedConvertToInt : " + rc);
				}
		}
		
		return 0;
	}
		
	private StringBuffer getJson(String urlString) {
		try {
			fieldInstance jso = new fieldInstance(urlString);		
			
			return jso.result;
			} catch (Exception e) {
				
				StringBuffer x = new StringBuffer();
				
				x.append(e.getMessage());
				
				return x;
			}
		}
	
	private void checkBaseUrl() {
			if (BaseUrl.length() == 0) {
				BaseUrl = String.format("http://%s:8083/field_api/v3/class/", HomeController.homeDiag.restIP.result);
			}
		}
	}

