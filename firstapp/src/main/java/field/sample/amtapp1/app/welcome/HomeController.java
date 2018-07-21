package field.sample.amtapp1.app.welcome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

import field.sample.amtapp1.domain.controller_servers.RobotControllerServer;
import field.sample.amtapp1.domain.service.ControllerService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
* Handles requests for the application home page.
* 
*/
@RestController
public class HomeController {
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

public static Locale Glocale;
public static Model Gmodel;
private static boolean FirstInit = true;

private static boolean UsingMyHtml = false;

@Autowired
private ControllerService controllerServiceImpl;

/**
* Simply selects the home view to render by returning its name.
 * @throws InterruptedException 
*/
@RequestMapping(value = "/")
public Greeting home(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;
	
	logger.info("Welcome home! The client locale is {}.", locale);
    final String template = "Hello, %s!";
    final AtomicLong xcounter = new AtomicLong();	
    
    checkInit();	

	return new Greeting(xcounter.incrementAndGet(),
            String.format(template, "Howdy"));
	}


@RequestMapping(value = "/r1position")
public String getR1PositionUrl(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;
    
    checkInit();    


	return getRobotPositionUrl(0);
	}
@RequestMapping(value = "/r2position")
public String getR2PositionUrl(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;
    
    checkInit();    


	return getRobotPositionUrl(0);
	}

@RequestMapping(value = "/r1variables")
public String getR1Variables(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;

    checkInit();    

	return getRobotVariables(0);
	}
@RequestMapping(value = "/r2variables")
public String getR2Variables(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;

    checkInit();    

	return getRobotVariables(1);
	}

private String getRobotPositionUrl(int robotNumber) {
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;
	
	return r.get(robotNumber).getRobotStatusGroupJson();
}

private String getRobotVariables(int robotNumber) {
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(robotNumber).GetStatusRobotVarsJson();
}

	private void checkInit() {
		if (FirstInit) {
			
			// Acquire the list from ControllerService and add it to the model.
			List<field.sample.amtapp1.domain.model.Controller> controllers = controllerServiceImpl.findAll(FirstInit);
			
			if (UsingMyHtml) {
				String JointHeader = String.format("%-10s%-9s%-9s%-9s%-9s%-9s%-9s%4s%-15s%-15s%-15s%-15s%-15s%-15s", 
						"Name", "J1", "J2", "J3", "J4", "J5", "J6", " ",
						"dJ1", "dJ2", "dJ3", "dJ4", "Jd5", "Jd6");
				
				String CartHeader = String.format("%-10s%-9s%-9s%-9s%-9s%-9s%-9s%-9s%-5s%-5s","Name", "X", "Y", "Z", "W", "P", "R", "Config", "UT", "UF");
				
				String TaskHeader = String.format("%-10s%-10s%-10s%-10s%-10s%-10s%-10s%-10s%-10s","Robot", "Main", "Sub", "Status", "Line", "", "", "", "");
				
				logger.info("Controllers = {" + controllers.toString() + "}");
				
				Gmodel.addAttribute("Jhdrs", JointHeader);
				Gmodel.addAttribute("Chdrs", CartHeader);
				Gmodel.addAttribute("Thdrs", TaskHeader);
				
				Gmodel.addAttribute("controllers", controllers);
			}
			
			FirstInit = false;
		}
	}

}
