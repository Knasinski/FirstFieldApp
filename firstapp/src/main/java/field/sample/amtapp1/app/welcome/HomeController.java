package field.sample.amtapp1.app.welcome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import field.sample.amtapp1.domain.controller_servers.RobotControllerServer;
import field.sample.amtapp1.domain.controller_tasks.RobotControllerTask;
import field.sample.amtapp1.domain.controller_variables.RcEventAlarm;
import field.sample.amtapp1.domain.controller_variables.RcEventAlarmMoment;
import field.sample.amtapp1.domain.controller_variables.RcOdometer;
import field.sample.amtapp1.domain.controller_variables.RcTimerArray;
import field.sample.amtapp1.domain.controller_variables.RcVariable;
import field.sample.amtapp1.domain.service.ControllerService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
* Handles requests for the application home page.
* 
*/


@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class HomeController {
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

public static Locale Glocale;
public static Model Gmodel;
private static boolean FirstInit = true;

private static boolean UsingMyHtml = false;

@Bean

public WebMvcConfigurer corsConfigurer() { return new WebMvcConfigurerAdapter() {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                .allowedHeaders("header1", "header2") //What is this for?
                .allowCredentials(true);
    	}
	};
}


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
    
    checkInit();

	return new Greeting(String.format(template, "Howdy"));
	}


@RequestMapping(value = "/r1position")
@ResponseBody
public RcOdometer RcStatusRobotGroup(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;
    
    checkInit();    


    return getRobotPositionUrl(0);
	}

@RequestMapping(value = "/r2position")

@ResponseBody
public RcOdometer getR2PositionUrl(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;
    
    checkInit();    


	return getRobotPositionUrl(1);
	}

@RequestMapping(value = "/r1variables")
public RcVariable[] getR1Variables(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;

    checkInit();    

	return getRobotVariables(0);
	}

@RequestMapping(value = "/r2variables")
public RcVariable[] getR2Variables(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;

    checkInit();    

	return getRobotVariables(1);
	}

@RequestMapping(value = "/r1tasks")
public RobotControllerTask getR1Tasks(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;

    checkInit();    
    
    ArrayList<RobotControllerTask> t = getRobotTasks(0);

	return t.get(0);
	}

@RequestMapping(value = "/r2tasks")
public RobotControllerTask getR2Tasks(Locale locale, Model model) throws InterruptedException {
	Glocale = locale;
	Gmodel = model;

    checkInit();    
    
    ArrayList<RobotControllerTask> t = getRobotTasks(1);

	return t.get(0);
	}


@RequestMapping(value = "/r1lastalarm")
public RcEventAlarm getR1LastAlarm(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;

    checkInit();    
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(0).getRcEventAlarm();
	}


@RequestMapping(value = "/r2lastalarm")
public RcEventAlarm getR2LastAlarm(Locale locale, Model model) throws InterruptedException {

	Glocale = locale;
	Gmodel = model;

    checkInit();    
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(1).getRcEventAlarm();
	}
@RequestMapping(value = "/r1allalarms")
public RcEventAlarmMoment[] getR1AllAlarms(Locale locale, Model model) throws InterruptedException {
	Glocale = locale;
	Gmodel = model;

    checkInit();    
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(0).getAllRcEventAlarms();
	}

@RequestMapping(value = "/r2allalarms")
public RcEventAlarmMoment[] getR2AllAlarms(Locale locale, Model model) throws InterruptedException {
	Glocale = locale;
	Gmodel = model;
	
	checkInit();    
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;
	
	return r.get(1).getAllRcEventAlarms();
	}

@RequestMapping(value = "/r1lastct")
public RcTimerArray getR1LastCT(Locale locale, Model model) throws InterruptedException {
	Glocale = locale;
	Gmodel = model;

    checkInit();    
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(0).GetRcTimersJson();
	}

@RequestMapping(value = "/r2lastct")
public RcTimerArray getR2LastCT(Locale locale, Model model) throws InterruptedException {
	Glocale = locale;
	Gmodel = model;

    checkInit();    
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(1).GetRcTimersJson();
	}

@ResponseBody
private RcOdometer getRobotPositionUrl(int robotNumber) {
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;
	
	return r.get(robotNumber).getRobotOdometerJson();
}

private RcVariable[] getRobotVariables(int robotNumber) {
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(robotNumber).GetStatusRobotVarsJson();
}

private ArrayList<RobotControllerTask> getRobotTasks(int robotNumber) {
	ArrayList<RobotControllerServer> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;

	return r.get(robotNumber).getStatusRobotTasks();
}


	private void checkInit() {
		
		if (FirstInit) {
			corsConfigurer();
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
