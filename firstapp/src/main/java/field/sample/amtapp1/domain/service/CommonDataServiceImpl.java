package field.sample.amtapp1.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import field.sample.amtapp1.app.welcome.HomeController;
import field.sample.amtapp1.utility_programs.fieldInstance;


@Service
public class CommonDataServiceImpl implements CommonDataService {
	private static final Logger logger = LoggerFactory.getLogger(CommonDataServiceImpl.class);
	private static String BaseUrl = "";
	private static final String InstanceStr = "/instance/";
	
	public String getInstances(String classId) {
		checkBaseUrl();
		String url = BaseUrl + classId;
//		logger.debug("getInstances : " + url);
		return getJson(url);
	}
	
	public String getInstance(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId;
//		logger.debug("getInstance : " + url);
		return getJson(url);
	}
	
	public String getLatest(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/latest";
//		logger.debug("getLatest : " + url);
		return getJson(url);
	}
		
	public String getHistory(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/history";
//		logger.debug("getHistory : " + url);
		return getJson(url);
	}
	
	public String getRelations(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/relations";
		return getJson(url);
	}
	
	public String getMoments(String classId, String instanceId) {
		String url = BaseUrl + classId + InstanceStr + instanceId + "/moments";
		return getJson(url);
	}
		
	public int getCount(String classId, String instanceId) {
		checkBaseUrl();
		String url = BaseUrl + classId + InstanceStr + instanceId + "/count";
//		logger.debug("getCount : " + url);
		String rc = getJson(url);
		
		if (rc.contains("count") && rc.contains(":")) {
			try {
				String s = rc.substring(rc.indexOf(":") + 1);
				s = s.substring(0, s.indexOf("}"));
				
				int k = Integer.parseInt(s);
				
				return k;
				} catch (Exception e) {
					logger.warn("failedConvertToInt : " + rc);
				}
		}
		
		return 0;
	}
		
	private String getJson(String urlString) {
		try {
			fieldInstance jso = new fieldInstance(urlString);		
			
			return jso.result.toString();
			} catch (Exception e) {
				return e.getMessage();
			}
		}
	
	private void checkBaseUrl() {
			if (BaseUrl.length() == 0) {
				BaseUrl = String.format("http://%s:8083/field_api/v3/class/", HomeController.homeDiag.restIP.result);
			}
		}
	}

