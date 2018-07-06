package field.sample.amtapp1.app.welcome;

import java.util.List;
import java.util.Locale;

import java.text.DateFormat;
import java.util.Date;

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
public static String serverTime = "XXXX";

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
	
	Date date = new Date();
	DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG, locale);
	String formattedDate = dateFormat.format(date);
	model.addAttribute("serverTime", formattedDate);
	
	// Acquire the list from ControllerService and add it to the model.
	List<field.sample.amtapp1.domain.model.Controller> controllers = controllerServiceImpl.findAll();
	
	logger.info("Controllers = {" + controllers.toString() + "}");
	
	model.addAttribute("controllers", controllers);
	
//	for (int i=0; i<10000; ++i) {	
	
	for(field.sample.amtapp1.domain.model.Controller controller:controllers) {
		
		if("robot_controller".equals(controller.getControllerType())){
		// Acquire the instance/history/relations of CNC.
		controllerServiceImpl.queryCncData(controller.getInstanceId());
		}
		
//		Thread.sleep(500);
//	}
	}
	
	return "welcome/home";
	}

}
