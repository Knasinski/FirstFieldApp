package field.sample.amtapp1.app.welcome;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import field.sample.amtapp1.domain.service.ControllerService;

/**
* Handles requests for the application home page.
* 
*/
@Controller
public class HomeController {
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

@Autowired
private ControllerService controllerServiceImpl;

/**
* Simply selects the home view to render by returning its name.
*/
@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})

public String home(Locale locale, Model model) {
	logger.info("Welcome home! The client locale is {}.", locale);
	
	// Acquire the list from ControllerService and add it to the model.
	List<field.sample.amtapp1.domain.model.Controller> controllers = controllerServiceImpl.findAll();
	
	logger.info("Controllers = {" + controllers.toString() + "}");
	
	model.addAttribute("controllers", controllers);
	
	for(field.sample.amtapp1.domain.model.Controller controller:controllers) {
		
		if("robot_controller".equals(controller.getControllerType())){
		// Acquire the instance/history/relations of CNC.
		controllerServiceImpl.queryCncData(controller.getInstanceId());
		}
	}
	
	return "welcome/home";
	}
}