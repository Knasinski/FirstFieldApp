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
public static Locale Glocale;
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

private static boolean FirstInit = true;

@Autowired
private ControllerService controllerServiceImpl;

/**
* Simply selects the home view to render by returning its name.
 * @throws InterruptedException 
*/
@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})

public String home(Locale locale, Model model) throws InterruptedException {
	logger.info("Welcome home! The client locale is {}.", locale);
	Glocale = locale;
	
	
	if (FirstInit) {
		
		String JointHeader = String.format("%-10s%-9s%-9s%-9s%-9s%-9s%-9s%4s%-15s%-15s%-15s%-15s%-15s%-15s", 
				"Name", "J1", "J2", "J3", "J4", "J5", "J6", " ",
				"dJ1", "dJ2", "dJ3", "dJ4", "Jd5", "Jd6");
		
		String CartHeader = String.format("%-10s%-9s%-9s%-9s%-9s%-9s%-9s%-9s%-5s%-5s","Name", "X", "Y", "Z", "W", "P", "R", "Config", "UT", "UF");
		
		String TaskHeader = String.format("%-10s%-10s%-10s%-10s%-10s%-10s%-10s%-10s%-10s","Robot", "Main", "Sub", "Status", "Line", "", "", "", "");
		
		// Acquire the list from ControllerService and add it to the model.
		List<field.sample.amtapp1.domain.model.Controller> controllers = controllerServiceImpl.findAll(FirstInit);
		
		logger.info("Controllers = {" + controllers.toString() + "}");
		
		model.addAttribute("Jhdrs", JointHeader);
		model.addAttribute("Chdrs", CartHeader);
		model.addAttribute("Thdrs", TaskHeader);
		
		model.addAttribute("controllers", controllers);
		
//		
//		for(field.sample.amtapp1.domain.model.Controller controller:controllers) {		
//			String d = controller.getControllerType();
//			if("robot_controller".equals(controller.getControllerType())) {
//				
//				// Acquire the instance/history/relations of CNC.
//				controllerServiceImpl.queryCncData(controller.getInstanceId());
//				}
//		}
		
		FirstInit = false;
	}
	

	return "welcome/home";
	}

}
