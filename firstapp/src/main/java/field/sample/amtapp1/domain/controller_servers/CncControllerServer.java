package field.sample.amtapp1.domain.controller_servers;

import field.sample.amtapp1.domain.service.CommonDataService;
import field.sample.amtapp1.utility_programs.DecodeId;
import field.sample.amtapp1.utility_programs.DecodeName;

public class CncControllerServer {

	private String CncControllerTypeStr = "controller_cnc";

	private String CncControllerFindStr = "\"controller_robot_group\":";

	public String controllerId = "";
	public String controllerName = "";
	public String cncControllerId = "";
	
	private CommonDataService commonDataServiceImp;
	
	public CncControllerServer(String cid, CommonDataService cdsi) {
		controllerId = cid;
		
		commonDataServiceImp = cdsi;

		//Get all required data
		if (getControllerName() && 
				getCncControllerId()) {
			
		}
		
	}
	private boolean getControllerName() {
		String mb = commonDataServiceImp.getInstance("controller", controllerId);
		DecodeName rc = new DecodeName(mb);
		
		if (rc.Good)
			controllerName = rc.result;
		
		return rc.Good;
	}
	private boolean getCncControllerId() {
		String mb = commonDataServiceImp.getRelations("controller", controllerId);
		
		DecodeId rc = new DecodeId(CncControllerTypeStr, mb);
		
		if (rc.Good)
			cncControllerId = rc.result;
		
		return rc.Good;
	}
}
