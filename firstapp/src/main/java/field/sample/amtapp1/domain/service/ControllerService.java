package field.sample.amtapp1.domain.service;
import java.util.List;
import field.sample.amtapp1.domain.model.Controller;

/**
* A service that acquires controller information from the common data.
*/
public interface ControllerService {
	/**
	* Acquires all controllers.
	*
	* List of @return controller.
	*/
	public List<Controller> findAll();
	
//	/**
//	* Acquires the instance/history/relations information of the specified controller.
//	*
//	* The instance id of the @param controllerId controller.
//	*/
	public void queryCncData(String controllerId);
//	
//	/**
//	* Acquires the instance/history/relations information of the specified controller.
//	*
//	* The instance id of the @param controllerId controller.
//	*/
//	public void queryRobotData(String controllerId);	
}
