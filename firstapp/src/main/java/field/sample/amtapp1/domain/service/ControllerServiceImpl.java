package field.sample.amtapp1.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.String;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import field.sample.amtapp1.domain.controller_servers.CncControllerServer;
import field.sample.amtapp1.domain.controller_servers.RobotControllerServer;
import field.sample.amtapp1.domain.controller_variables.RcVariable;
import field.sample.amtapp1.domain.model.CommonDataController;
import field.sample.amtapp1.domain.model.CommonDataControllerCnc;
import field.sample.amtapp1.domain.model.CommonDataControllerHistory;
import field.sample.amtapp1.domain.model.CommonDataControllerLatest;
import field.sample.amtapp1.domain.model.CommonDataControllerRelations;
//import field.sample.amtapp1.domain.model.CommonDataControllerHistory;
//import field.sample.amtapp1.domain.model.CommonDataControllerLatest;
//import field.sample.amtapp1.domain.model.CommonDataControllerRelations;
import field.sample.amtapp1.domain.model.Controller;

@Service
public class ControllerServiceImpl implements ControllerService {	
	private static final Logger logger = LoggerFactory.getLogger(ControllerServiceImpl.class);

	public static double meaningless = 0.0;
	
	public static ArrayList<RobotControllerServer> RcList;
	public static ArrayList<CncControllerServer> CncList;
	
	@Autowired
	private CommonDataService commonDataServiceImpl;
	
	@Override
	public List<Controller> findAll(boolean RcListBuild)  {
		ArrayList<Controller> list = new ArrayList<Controller>();
		RcList = new ArrayList<RobotControllerServer>();
		CncList = new ArrayList<CncControllerServer>();
		
		// Acquire a list of instances in the controller class from CommonDataService in a JSON string.
		String controllersJson = commonDataServiceImpl.getInstances("controller").toString();
		
		if(controllersJson == null) {
			logger.warn("failure : get controllers");
			return list;
		}
		
		// Convert the JSON string of the controller class into an array of the CommonDataController class.
		CommonDataController[] controllers = new Gson().fromJson(controllersJson, CommonDataController[].class);
		Integer i=1;
		
		if (controllers == null) {
			return list;
		}
		//CommonDataController
		for (CommonDataController controller:controllers) {
			
			controller.controller_type = getControllerType(controller);
			
			if ("controller_robot_controller".equals(controller.controller_type)) {
				logger.debug("id : " + controller.id + "_" + i.toString());
				logger.debug("name : " + controller.name);
				// Add the controller that has the id and name acquired from the common data to the list.
				
				if (RcListBuild) {
					RobotControllerServer rci = new RobotControllerServer(controller.id, commonDataServiceImpl);
					
					if (rci.DataGood) {
						if (!RcList.contains(rci))
							RcList.add(rci);
					}
				}
			}
			else if ("controller_cnc".equals(controller.controller_type)) {
				if (RcListBuild) {
					CncControllerServer cci = new CncControllerServer(controller.id, commonDataServiceImpl);
					
					if (cci.DataGood) {
						if (!CncList.contains(cci))
							CncList.add(cci);
					}
				}				
			}
			
			list.add(new Controller(controller.id, controller.name, controller.controller_type));
		}
		
		if ((RcList != null) && (RcList.size() != 0))
			Collections.sort(RcList, new SortByControllerName());
		
		if ((CncList != null) && (CncList.size() != 0)) 
			Collections.sort(CncList, new SortByCncControllerName());
		
	return list;
	}

	public void queryCncData(String controllerId) {
	// Acquisition of instance
	String instanceJson = commonDataServiceImpl.getInstance("controller", controllerId).toString();
	
		if(instanceJson == null) {
		logger.warn("failure : get controller " + controllerId);
		return;
	}
	
	CommonDataController controller = new Gson().fromJson(instanceJson, CommonDataController.class);
	logger.debug("id : " + controller.id);
	logger.debug("name : " + controller.name);
	logger.debug("type : " + controller.controller_type);
	
	if(controller.link != null) {
		logger.debug("link.history : " + controller.link.history);
		logger.debug("link.instance : " + controller.link.instance);
		logger.debug("link.latest : " + controller.link.latest);
		logger.debug("link.relations : " + controller.link.relations);
	}
	
	// Acquisition of latest
	String latestJson = commonDataServiceImpl.getLatest("controller",controllerId).toString();
		if(latestJson == null) {
		logger.warn("failure : get latest " + controllerId);
		return;
	}
	
	CommonDataControllerLatest latest = new Gson().fromJson(latestJson, CommonDataControllerLatest.class);
	
	logger.debug("latest model : " + latest.model);
	// Acquisition of history
	String historyJson = commonDataServiceImpl.getHistory("controller",controllerId).toString();
	
	if(historyJson == null) {
		logger.warn("failure : get history " + controllerId);
		return;
	}
	
	CommonDataControllerHistory[] histories = new Gson().fromJson(historyJson, CommonDataControllerHistory[].class);
	
	for (CommonDataControllerHistory history : histories) {
		logger.debug("history unixtime : " + history.unixtime);
		logger.debug("history link : " + history.link);
		logger.debug("history relations : " + history.relations);
	}
	
	// Acquisition of relations
	String relationsJson = commonDataServiceImpl.getRelations("controller",controllerId).toString();
	
	if(relationsJson == null) {
		logger.warn("failure : get relations" + controllerId);
		return;
	}
	
		CommonDataControllerRelations relations = new Gson().fromJson(relationsJson, CommonDataControllerRelations.class);
		
		if (relations.controller_cnc != null) {
			for(CommonDataControllerCnc controllerCnc : relations.controller_cnc) {
				logger.debug("controller_cnc id : " + controllerCnc.id);
				
				if(controllerCnc.link != null) {
					logger.debug("controller_cnc history : " + controllerCnc.link.history);
					logger.debug("controller_cnc instance : " + controllerCnc.link.instance);
					logger.debug("controller_cnc latest : " + controllerCnc.link.latest);
					logger.debug("controller_cnc relations : " + controllerCnc.link.relations);
				}
			}
		}	
	}
	
	private String getControllerType(CommonDataController controller) {
		String ct = "Unknown";
		
		
		if (controller.controller_type.equals(ct) || (controller.controller_type.length() == 0)) {
			String mb = commonDataServiceImpl.getRelations("controller", controller.id).toString();
			
			if ((mb.length() != 0) && mb.contains("id")) {
				if (mb.contains("controller_injection"))
					ct = "controller_injection";
				else if (mb.contains("controller_plc"))
					ct = "controller_plc";
				else if (mb.contains("controller_cnc"))
					ct = "controller_cnc";
				else if (mb.contains("controller_robot_controller"))
					ct = "controller_robot_controller";
				else if (mb.contains("controller_wirecut"))
					ct = "controller_wirecut";
				else if (mb.contains("controller_sensor"))
					ct = "controller_sensor";
				else if (mb.contains("controller_laser"))
					ct = "controller_laser";
			}
		}
		
		return ct;
	}	
}

class SortByControllerName implements Comparator<RobotControllerServer>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(RobotControllerServer a, RobotControllerServer b)
    {
    	if ((a.controllerName == null) && (b.controllerName == null))
    		return 0;
    	
    	if (a.controllerName == null)
    		return 1;
    	
    	if (b.controllerName == null)
    		return -1;
    	
    	return a.controllerName.compareTo(b.controllerName);
    }
}

class SortByCncControllerName implements Comparator<CncControllerServer>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(CncControllerServer a, CncControllerServer b)
    {
    	if ((a.controllerName == null) && (b.controllerName == null))
    		return 0;
    	
    	if (a.controllerName == null)
    		return 1;
    	
    	if (b.controllerName == null)
    		return -1;
    	
    	return a.controllerName.compareTo(b.controllerName);
    }
}
	