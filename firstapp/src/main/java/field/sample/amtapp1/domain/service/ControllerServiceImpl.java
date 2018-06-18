package field.sample.amtapp1.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import field.sample.amtapp1.domain.model.CommonDataController;
//import field.sample.amtapp1.domain.model.CommonDataControllerCnc;
//import field.sample.amtapp1.domain.model.CommonDataControllerHistory;
//import field.sample.amtapp1.domain.model.CommonDataControllerLatest;
//import field.sample.amtapp1.domain.model.CommonDataControllerRelations;
import field.sample.amtapp1.domain.model.Controller;


@Service
public class ControllerServiceImpl implements ControllerService {	
	private static final Logger logger = LoggerFactory.getLogger(ControllerServiceImpl.class);
	
	@Autowired
	private CommonDataService commonDataServiceImpl;
	
	@Override
	public List<Controller> findAll()  {
		ArrayList<Controller> list = new ArrayList<>();
		// Comment out the code from the previous chapter.
		// // In this stage of the chapter, a fixed list of controllers to be displayed is generated.
		// // -> This will be modified to acquire a list from the common data.
		// list.add(new Controller("controllerId0001", "nameAAAA"));
		// list.add(new Controller("controllerId0002", "nameBBBB"));
		// list.add(new Controller("controllerId0003", "nameCCCC"));
		// list.add(new Controller("controllerId0004", "nameDDDD"));
		// list.add(new Controller("controllerId0005", "nameEEEE"));
		// list.add(new Controller("controllerId0006", "nameFFFF"));
		// list.add(new Controller("controllerId0007", "nameGGGG"));
		// list.add(new Controller("controllerId0008", "nameHHHH"));
		// list.add(new Controller("controllerId0009", "nameIIII"));		
		// list.add(new Controller("controllerId0010", "nameJJJJ"));
		
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
			if ("robot_controller".equals(controller.controller_type)) {
				logger.debug("id : " + controller.id + "_" + i.toString());
				logger.debug("name : " + controller.name);
				// Add the controller that has the id and name acquired from the common data to the list.
				list.add(new Controller(controller.id, controller.name, controller.controller_type));
			}
		}
	return list;
	}

}
	