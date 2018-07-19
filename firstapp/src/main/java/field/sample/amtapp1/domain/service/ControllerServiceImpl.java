package field.sample.amtapp1.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import field.sample.amtapp1.domain.controller_servers.GenericRelationsData;
import field.sample.amtapp1.domain.controller_servers.RobotControllerServer;
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
	
	@Autowired
	private CommonDataService commonDataServiceImpl;
	
	@Override
	public List<Controller> findAll(boolean RcListBuild)  {
		ArrayList<Controller> list = new ArrayList<>();
		RcList = new ArrayList<>();
		
		// Acquire a list of instances in the controller class from CommonDataService in a JSON string.
		String controllersJson = commonDataServiceImpl.getInstances("controller");
		
		if(controllersJson == null) {
			logger.warn("failure : get controllers");
			return list;
		}
		
		// Convert the JSON string of the controller class into an array of the CommonDataController class.
		CommonDataController[] controllers = new Gson().fromJson(controllersJson, CommonDataController[].class);
		Integer i=1;
		//CommonDataController
		for(CommonDataController controller:controllers) {
			
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
				
				list.add(new Controller(controller.id, controller.name, controller.controller_type));
			}
		}
		
	return list;
	}

	public void queryCncData(String controllerId) {
	// Acquisition of instance
	String instanceJson = commonDataServiceImpl.getInstance("controller",controllerId);
	
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
	String latestJson = commonDataServiceImpl.getLatest("controller",controllerId);
		if(latestJson == null) {
		logger.warn("failure : get latest " + controllerId);
		return;
	}
	
	CommonDataControllerLatest latest = new Gson().fromJson(latestJson, CommonDataControllerLatest.class);
	
	logger.debug("latest model : " + latest.model);
	// Acquisition of history
	String historyJson = commonDataServiceImpl.getHistory("controller",controllerId);
	
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
	String relationsJson = commonDataServiceImpl.getRelations("controller",controllerId);
	
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
			String mb = commonDataServiceImpl.getRelations("controller", controller.id);
			
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
	