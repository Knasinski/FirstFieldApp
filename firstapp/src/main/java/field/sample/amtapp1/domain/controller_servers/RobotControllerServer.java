package field.sample.amtapp1.domain.controller_servers;

//Test
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import com.google.gson.Gson;

//import org.json;
//end Test

import java.util.ArrayList;
import java.util.Comparator;

import field.sample.amtapp1.domain.controller_tasks.RobotControllerTask;
import field.sample.amtapp1.domain.controller_variables.RcStatusRobotGroup;
import field.sample.amtapp1.domain.controller_variables.RcVariable;
import field.sample.amtapp1.domain.model.CommonDataController;
import field.sample.amtapp1.domain.model.CommonDataLink;
import field.sample.amtapp1.domain.service.CommonDataService;
import field.sample.amtapp1.utility_programs.DecodeConfig;
import field.sample.amtapp1.utility_programs.DecodeId;
import field.sample.amtapp1.utility_programs.DecodeName;

public class RobotControllerServer {
	
	private String RobotControllTypeStr = "controller_robot_controller";
	private String ControllerRobotGroupTypeStr = "controller_robot_group";
	private String StatusRCVarTypeStr = "status_robot_controller_variable";
	private String StatusRCTaskTypeStr = "status_robot_controller_task";
	private String StatusRobotGroupTypeStr = "status_robot_group";
	
	
	private String ControllerRobotGroupFindStr = "\"controller_robot_group\":";
	private String StatusRobotGroupFindStr = "\"status_robot_group\":";
	private String StatusRCVarFindStr = "\"id\":\"status_robot_controller_variable";
	private String RobotConfigFindStr = "\"configuration\":";
	private String StatusRCTaskFindStr = "\"id\":\"status_robot_controller_task";
	private String NameFindStr = "\"name\":";

	public String controllerId = "";
	public String controllerName = "";
	public String robotControllerId = "";
	public String controllerRobotGroupId = "";
	public String statusRobotGroupId = "";
	public String statusRobotTaskId = "";
	
	
	public String jointPose = "";
	public CommonDataLink link = new CommonDataLink();
	
	public RcStatusRobotGroup LatestRcStatusRobotGroup;
	
	public int NumExternalAxis = 0;
	
	public int Count = 0;
	
	public double[] LastJointPosition = {0,0,0,0,0,0,0,0,0};
	public double[] JointOdometer = {0,0,0,0,0,0,0,0,0};
	
	
	public double[] CartesianPosition = {0,0,0,0,0,0,0,0,0};
	public String Configuration = "";

	ArrayList<String> StatusRcVars = new ArrayList<String>();
	
	ArrayList<RcVariable> StatusRcVarLst = new ArrayList<RcVariable>();
	
	String StatusRcVarsJson = "";
	
	ArrayList<RobotControllerTask> StatusRcTaskList = new ArrayList<RobotControllerTask>();
	
	public boolean DataGood;
	private boolean JrecStarted = false;
	
	private CommonDataService commonDataServiceImp;
	
	public RobotControllerServer(String cid, CommonDataService cdsi) {
		
		//Start tests
//		JSONParser jsonParser = new JSONParser();
		//End tests
		controllerId = cid;
		
		commonDataServiceImp = cdsi;
		DataGood = false;
		
		//Attempt to get the RcID
		if (getControllerName() && 
				getRobotControllerId() && 
				getCount(RobotControllTypeStr, robotControllerId) && 
				getControllerRobotGroupId() &&
				getStatusRobotGroup() &&
				getStatusRcVariables() &&
				getStatusRobotTaskId()) {
				DataGood = true;
			}
	}
	
	public String getStatusRobotTasks() {
		String mb = String.format("%-10s", controllerName);
		getStatusRcTaskList();
		
		for (int i=0; i<StatusRcTaskList.size(); ++i) {
			RobotControllerTask t = StatusRcTaskList.get(i);
			
			mb += String.format("%-10s%-10s%-10s%-10s", t.Name, t.ProgramName, t.Status, t.LineNumber);
		}
		
		return mb;
	}
	public String getStatusRobotVars() {
		String mb = "";
		
		for (int i=0; i<StatusRcVars.size(); ++i) {
			getStatusRcVarLst();
			RcVariable rcv = StatusRcVarLst.get(i);
			
			String Uu = (rcv.unit == null) ? "" : rcv.unit;
			
			mb += String.format("%-20s%-20s%-20s%-20s%-20s@\0", controllerName, rcv.type, rcv.name, Uu, rcv.getValueUse());
		}
		
		return mb;
	}
	
	public String GetStatusRobotVarsJson() {
		getStatusRcVarLst();
		return StatusRcVarsJson;
	}
	
	public String getRobotStatusGroupJson() {
		return commonDataServiceImp.getLatest(StatusRobotGroupTypeStr, statusRobotGroupId);
	}
	
	public String getRobotJas()
	{
		String rc = "Invalid";
		String mb = "";
		
		LatestRcStatusRobotGroup = getLatestStatusRobotGroup();
		
		
		if (JrecStarted ) {
			for (int i=0; i<LatestRcStatusRobotGroup.joint_position.value.length; ++i) {
				double dj = Math.abs(LastJointPosition[i] - LatestRcStatusRobotGroup.joint_position.value[i]);
				JointOdometer[i] += dj;
			}
		}
		else
			JrecStarted = true;
		
		for (int i=0; i<LatestRcStatusRobotGroup.joint_position.value.length; ++i) {
			LastJointPosition[i] = LatestRcStatusRobotGroup.joint_position.value[i];
			
			String t = String.format("%-9.3f\0", LatestRcStatusRobotGroup.joint_position.value[i]);
			mb += t;
		}
			
		String xxx = String.format("%-55s |  ", mb);
		
		mb = xxx;
		
		for (int i=0; i<6; ++i) {
			String t = String.format("%-15.3f\0", JointOdometer[i]);
			mb += t;
		}
		
		rc = mb;
		
		return rc;
	}
	
	private RcStatusRobotGroup getLatestStatusRobotGroup() {
		String mb = commonDataServiceImp.getLatest(StatusRobotGroupTypeStr, statusRobotGroupId);
		
		return new Gson().fromJson(mb, RcStatusRobotGroup.class);
	}
	
	public String getRobotCartPos()
	{
		String rc = "Invalid";
		String mb = "";
		
		if (LatestRcStatusRobotGroup == null)
			LatestRcStatusRobotGroup = getLatestStatusRobotGroup();
		
		RcStatusRobotGroup db = LatestRcStatusRobotGroup;
		
		if (LatestRcStatusRobotGroup.cartesian_position.value.length > 5) {
			for (int i=0; i<6; ++i) {
				String t = String.format("%-9.3f\0", LatestRcStatusRobotGroup.cartesian_position.value[i]);
				mb += t;
			}
				
			rc =  String.format("%s%-10s%-5d%-5d%-5d%-5d", mb, LatestRcStatusRobotGroup.cartesian_position.configuration,
					LatestRcStatusRobotGroup.tool_frame_id, LatestRcStatusRobotGroup.user_frame_id,
					LatestRcStatusRobotGroup.is_running, LatestRcStatusRobotGroup.is_servo_ready); 
		}
		else {
			rc = "ZIPPO";
		}
		
		return rc;
	}
	
	private boolean getCurrentCartPos() {
		String mb = commonDataServiceImp.getLatest(StatusRobotGroupTypeStr, statusRobotGroupId);
	
	try {
		if ((mb != null) && (mb.length() != 0) && mb.contains("cartesian_position") && !mb.contains("\"cartesian_position\":null")) {
			String rc = mb.substring(mb.indexOf("cartesian_position"));
			rc = rc.substring(rc.indexOf("value")+8);
			rc = rc.substring(0,rc.indexOf("]"));
			
			String[] items = rc.split(",");
			
			for (int i=0; i<6; ++i) 
				CartesianPosition[i] = Double.parseDouble(items[i]);
			
			//Process config
			Configuration = "Invalid";
			
			if (mb.contains(RobotConfigFindStr)) {
				rc = mb.substring(mb.indexOf(RobotConfigFindStr));
				DecodeConfig c = new DecodeConfig(rc);
				
//				void d = JSON.Parse();
				
				if (c.Good)
					Configuration = c.result;
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
	
	private boolean getStatusRobotTaskId() {
		String mb = commonDataServiceImp.getRelations(RobotControllTypeStr, robotControllerId);
		
		if (mb.contains(StatusRCTaskFindStr)) {
			mb=mb.substring(mb.indexOf(StatusRCTaskFindStr));
			DecodeId rc = new DecodeId(StatusRCTaskFindStr, mb);
			
			if (rc.Good)
				statusRobotTaskId = rc.result;
			
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
	
	private RcVariable getStatusRcVariable(String varID) {
		String mb = commonDataServiceImp.getLatest(StatusRCVarTypeStr, varID);
		
		return new Gson().fromJson(mb, RcVariable.class);
	}
	
	private void getStatusRcVarLst() {
		StatusRcVarLst = new ArrayList<RcVariable>();
		StatusRcVarsJson = "[\n";
				
		for (int i=0; i<StatusRcVars.size(); ++i) {
			String mb = commonDataServiceImp.getLatest(StatusRCVarTypeStr, StatusRcVars.get(i)).replace("\n", "");
			StatusRcVarsJson += (i == (StatusRcVars.size()-1) ? (mb + "]\n") : (mb + ",\n"));
		}
		
		//StatusRcVarLst.sort(new SortbyType());		
	}
	
	private void getStatusRcTaskList() {
		String mb = commonDataServiceImp.getLatest(StatusRCTaskTypeStr, statusRobotTaskId);
		
		if (mb.length() != 0) {
			StatusRcTaskList = new ArrayList<RobotControllerTask>();
			
			while (mb.contains(NameFindStr)) {
				mb = mb.substring(mb.indexOf(NameFindStr));
				
				String b1 = mb.substring(0, mb.indexOf("}"));
				
				RobotControllerTask t = new RobotControllerTask(statusRobotTaskId, b1);
				
				if (t.Name.length() == 0)
					break;
				else
					StatusRcTaskList.add(t);
				
				mb = mb.substring(1);
			}
		}
	}
}
	
class SortbyType implements Comparator<RcVariable>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(RcVariable a, RcVariable b)
    {
    	return a.type.compareTo(b.type);
    }
}
