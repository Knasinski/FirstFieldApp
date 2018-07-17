package field.sample.amtapp1.domain.controller_servers;

import java.util.ArrayList;
import java.util.Comparator;

import field.sample.amtapp1.domain.controller_variables.RobotControllerVariable;
import field.sample.amtapp1.domain.model.CommonDataLink;
import field.sample.amtapp1.domain.service.CommonDataService;
import field.sample.amtapp1.utility_programs.DecodeId;
import field.sample.amtapp1.utility_programs.DecodeName;

public class RobotControllerServer {
	
	private String RobotControllTypeStr = "controller_robot_controller";
	private String ControllerRobotGroupTypeStr = "controller_robot_group";
	private String StatusRCVarTypeStr = "status_robot_controller_variable";
	
	
	private String ControllerRobotGroupFindStr = "\"controller_robot_group\":";
	private String StatusRobotGroupFindStr = "\"status_robot_group\":";
	private String StatusRCVarFindStr = "\"id\":\"status_robot_controller_variable";

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
	public double[] LastJointPosition = {0,0,0,0,0,0,0,0,0};
	public double[] JointOdometer = {0,0,0,0,0,0,0,0,0};
	
	
	public double[] CartesianPosition = {0,0,0,0,0,0,0,0,0};

	ArrayList<String> StatusRcVars = new ArrayList<String>();
	ArrayList<RobotControllerVariable> StatusRcVarList = new ArrayList<RobotControllerVariable>();
	
	public boolean DataGood;
	private boolean JrecStarted = false;
	
	private CommonDataService commonDataServiceImp;
	
	public RobotControllerServer(String cid, CommonDataService cdsi) {
		controllerId = cid;
		
		commonDataServiceImp = cdsi;
		DataGood = false;
		
		//Attempt to get the RcID
		if (getControllerName() && 
				getRobotControllerId() && 
				getCount(RobotControllTypeStr, robotControllerId) && 
				getControllerRobotGroupId() &&
				getStatusRobotGroup() &&
				getStatusRcVariables()) {
				getStatusRcVarList();
				DataGood = true;
			}
	}
	
	public String getStatusRobotVars() {
		String mb = "";
		
		for (int i=0; i<StatusRcVars.size(); ++i) {
			RobotControllerVariable rcv = StatusRcVarList.get(i);
			rcv.UpDateValue(commonDataServiceImp);
			
			mb += String.format("%-20s%-20s%-20s%-20s%-20s@\0", controllerName, rcv.Type, rcv.Name, rcv.Unit, rcv.Value);
		}
		
		return mb;
	}
	
	public String getRobotJas()
	{
		String rc = "Invalid";
		String mb = "";
		
		if (getCurrentJas()) {
			try {
			for (int i=0; i<6; ++i) {
				String t = String.format("%-9.3f\0", JointPosition[i]);
				mb += t;
			}
			
			String xxx = String.format("%-55s |  ", mb);
			
			mb = xxx;
			
			for (int i=0; i<6; ++i) {
				String t = String.format("%-15.3f\0", JointOdometer[i]);
				mb += t;
			}
			
			rc = mb;
			} catch (Exception e) { }
		}
		
		return rc;
	}
	
	private boolean getCurrentJas() {
		String mb = commonDataServiceImp.getLatest("status_robot_group", statusRobotGroupId);
		
		try {
			if ((mb != null) && (mb.length() != 0) && mb.contains("joint_position") && !mb.contains("\"joint_position\":null")) {
				String rc = mb.substring(mb.indexOf("value")+8);
				rc = rc.substring(0,rc.indexOf("]"));
				
				String[] items = rc.split(",");
				
				for (int i=0; i<items.length; ++i) {
					JointPosition[i] = Double.parseDouble(items[i]);
				}
			}
		} catch (Exception e) {
			return false;
		}
		
		//Calculate change in position
		if (JrecStarted) {
			for (int i=0; i<6; ++i) 
				JointOdometer[i] += Math.abs(JointPosition[i] - LastJointPosition[i]);
		}
		else 
			JrecStarted = true;
		
	for (int i=0; i<6; ++i)
		LastJointPosition[i] = JointPosition[i];
		
	return true;
	}
	
	public String getRobotCartPos()
	{
		String rc = "Invalid";
		String mb = "";
		
		if (getCurrentCartPos()) {
			try {
			for (int i=0; i<6; ++i) {
				String t = String.format("%-9.3f\0", CartesianPosition[i]);
				mb += t;
			}
			
			rc = mb;
			} catch (Exception e) { }
		}
		
		return rc;
	}
	
	private boolean getCurrentCartPos() {
		String mb = commonDataServiceImp.getLatest("status_robot_group", statusRobotGroupId);
	
	try {
		if ((mb != null) && (mb.length() != 0) && mb.contains("cartesian_position") && !mb.contains("\"cartesian_position\":null")) {
			mb = mb.substring(mb.indexOf("cartesian_position"));
			String rc = mb.substring(mb.indexOf("value")+8);
			rc = rc.substring(0,rc.indexOf("]"));
			
			String[] items = rc.split(",");
			
			for (int i=0; i<6; ++i) {
				CartesianPosition[i] = Double.parseDouble(items[i]);
			}
		}
		} catch (Exception e) {
			return false;
		}
	
	return true;		
	}
	
	private boolean getControllerName() {
		String mb = commonDataServiceImp.getInstance("controller", controllerId);
		DecodeName rc = new DecodeName(mb);
		
		if (rc.Good)
			controllerName = rc.result;
		
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
				
		if (mb.contains(ControllerRobotGroupFindStr)) {
			mb=mb.substring(mb.indexOf(ControllerRobotGroupFindStr));
			DecodeId rc = new DecodeId(ControllerRobotGroupFindStr, mb);
			
			if (rc.Good)
				controllerRobotGroupId = rc.result;
			
			return rc.Good;
		}
		
		return false;				
	}
	
	private boolean getStatusRobotGroup() {
		String mb = commonDataServiceImp.getRelations(ControllerRobotGroupTypeStr, controllerRobotGroupId);
		
		if (mb.contains(StatusRobotGroupFindStr)) {
			mb=mb.substring(mb.indexOf(StatusRobotGroupFindStr));
			DecodeId rc = new DecodeId(StatusRobotGroupFindStr, mb);
			
			if (rc.Good)
				statusRobotGroupId = rc.result;
			
			return rc.Good;
		}
		
		return false;
	}
	
	private boolean getStatusRcVariables() {
		StatusRcVars = new ArrayList<String>();
		String mb = commonDataServiceImp.getRelations(RobotControllTypeStr, robotControllerId);
		
		try {
			while (mb.contains(StatusRCVarFindStr)) {
				mb = mb.substring(mb.indexOf(StatusRCVarFindStr) + 6);
				
				String s = mb.substring(0, mb.indexOf("\""));
				
				StatusRcVars.add(s);
			} 
		} catch (Exception w) {
			return false;
		}
		
		return true;
	}
	
	private RobotControllerVariable getStatusRcVariable(String varID) {
		String mb = commonDataServiceImp.getLatest(StatusRCVarTypeStr, varID);
		
		return new RobotControllerVariable(varID, mb);
	}
	
	private void getStatusRcVarList() {
		StatusRcVarList = new ArrayList<RobotControllerVariable>();
		
		for (int i=0; i<StatusRcVars.size(); ++i) {
			StatusRcVarList.add(getStatusRcVariable(StatusRcVars.get(i)));
		}
		
		StatusRcVarList.sort(new SortbyType());		
	}
}
class SortbyType implements Comparator<RobotControllerVariable>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(RobotControllerVariable a, RobotControllerVariable b)
    {
    	return a.Type.compareTo(b.Type);
    }
}
