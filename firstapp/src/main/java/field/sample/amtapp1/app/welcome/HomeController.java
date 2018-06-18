package field.sample.amtapp1.app.welcome;

//import java.text.DateFormat;
//import java.util.Date;
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
private static final Logger logger = LoggerFactory
.getLogger(HomeController.class);

// Storage path in the persistence area
//private static final String STRAGE_PATH = "/mnt/field/app/app/amtapp1";

@Autowired
private ControllerService controllerServiceImpl;

/**
* Simply selects the home view to render by returning its name.
*/
@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})

public String home(Locale locale, Model model) {
logger.info("Welcome home! The client locale is {}.", locale);
	// Comment out the date display processing that was automatically generated in the blank project.
	// Date date = new Date();
	// DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
	// DateFormat.LONG, locale);
	//
	// String formattedDate = dateFormat.format(date);
	//
	// model.addAttribute("serverTime", formattedDate);
	// Acquire the list from ControllerService and add it to the model.
	List<field.sample.amtapp1.domain.model.Controller> controllers = controllerServiceImpl.findAll();
	model.addAttribute("controllers", controllers);
	return "welcome/home";
	}
}