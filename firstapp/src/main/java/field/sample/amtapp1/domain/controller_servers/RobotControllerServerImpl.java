package field.sample.amtapp1.domain.controller_servers;

import field.sample.amtapp1.domain.model.CommonDataLink;
import field.sample.amtapp1.domain.service.CommonDataService;
import field.sample.amtapp1.utility_programs.DecodeId;
import field.sample.amtapp1.utility_programs.DecodeName;

public class RobotControllerServerImpl {
	
	private String RobotControllTypeStr = "controller_robot_controller";
	private String ControllerRobotGroupTypeStr = "\"controller_robot_group\":";
	private String StatisRobotGroupTypeStr = "\"status_robot_group\":";

	public String controllerId = "";
	public String controllerName = "";
	public String robotControllerId = "";
	public String controllerRobotGroupId = "";
	public String statusRobotGroupId = "";
	
	
	public String jointPose = "";
	public CommonDataLink link = new CommonDataLink();
	
	public int NumExternalAxis = 0;
	
	public int Count = 0;
	
	public double[] JointPosition = {0,0,0,0,0,0,0,0,0};
	public double[] CartesianPosition = {0,0,0,0,0,0,0,0,0};
	public String configuration = "";
	public boolean[] ROs = {false,false,false,false,false,false,false,false};
	public boolean[] UOs = {false,false,false,false,false,false,false,false,false,false,false};
	
	
	public String[] ROnames = {"","","","","","","",""};
	public String[] UOnames = {"","","","","","","","","","",""};
	
	public boolean DataGood;
	
	
	private CommonDataService commonDataServiceImp;
	
	public RobotControllerServerImpl(String cid, CommonDataService cdsi) {
		controllerId = cid;
		
		commonDataServiceImp = cdsi;
		DataGood = false;
		
		//Attempt to get the RcID
		if (getControllerName() && 
				getRobotControllerId() && 
				getCount(RobotControllTypeStr, robotControllerId) && 
				getControllerRobotGroupId() &&
				getStatusRobotGroup()) {
				DataGood = true;
			}
	}
	private boolean getControllerName() {
		String mb = commonDataServiceImp.getInstance("controller", controllerId);
		DecodeName rc = new DecodeName(mb);
		
		if (rc.Good)
			robotControllerId = rc.result;
		
		return rc.Good;
	}
	private boolean getRobotControllerId() {
		String mb = commonDataServiceImp.getRelations("controller", controllerId);
		
		DecodeId rc = new DecodeId(RobotControllTypeStr, mb);
		
		if (rc.Good)
			robotControllerId = rc.result;
		
		return rc.Good;
	}
	
	private boolean getCount(String Type, String Id) {
		Count = commonDataServiceImp.getCount(Type, Id);
		return (Count != 0);
	}
	
	private boolean getControllerRobotGroupId() {
		String mb = commonDataServiceImp.getRelations(RobotControllTypeStr, robotControllerId);
				
		if (mb.contains(ControllerRobotGroupTypeStr)) {
			mb=mb.substring(mb.indexOf(ControllerRobotGroupTypeStr));
			DecodeId rc = new DecodeId(ControllerRobotGroupTypeStr, mb);
			
			if (rc.Good)
				controllerRobotGroupId = rc.result;
			
			return rc.Good;
		}
		
		return false;				
	}
	
	private boolean getStatusRobotGroup() {
		String mb = commonDataServiceImp.getRelations("controller_robot_group", controllerRobotGroupId);
		
		if (mb.contains(StatisRobotGroupTypeStr)) {
			mb=mb.substring(mb.indexOf(StatisRobotGroupTypeStr));
			DecodeId rc = new DecodeId(StatisRobotGroupTypeStr, mb);
			
			if (rc.Good)
				statusRobotGroupId = rc.result;
			
			return rc.Good;
		}
		
		return false;
	}
}
