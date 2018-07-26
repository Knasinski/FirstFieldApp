package field.sample.amtapp1.domain.controller_servers;


import com.google.gson.Gson;

//import org.json;
//end Test

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import field.sample.amtapp1.domain.controller_tasks.RobotControllerTask;
import field.sample.amtapp1.domain.controller_variables.RcEventAlarm;
import field.sample.amtapp1.domain.controller_variables.RcEventAlarmMoment;
import field.sample.amtapp1.domain.controller_variables.RcStatusRobotGroup;
import field.sample.amtapp1.domain.controller_variables.RcVariable;
import field.sample.amtapp1.domain.model.CommonDataLink;
import field.sample.amtapp1.domain.service.CommonDataService;
import field.sample.amtapp1.utility_programs.DecodeId;
import field.sample.amtapp1.utility_programs.DecodeName;
import field.sample.amtapp1.utility_programs.ObjectOfTypeBelongingTo;



public class RobotControllerServer {
	
	private String RobotControllTypeStr = "controller_robot_controller";
	private String ControllerRobotGroupTypeStr = "controller_robot_group";
	private String StatusRCVarTypeStr = "status_robot_controller_variable";
	private String StatusRCTaskTypeStr = "status_robot_controller_task";
	private String StatusRobotGroupTypeStr = "status_robot_group";
	private String EventAlarmTypeStr = "event_alarm";
	
	
	private String ControllerRobotGroupFindStr = "\"controller_robot_group\":";
	private String StatusRobotGroupFindStr = "\"status_robot_group\":";
	private String StatusRCVarFindStr = "\"id\":\"status_robot_controller_variable";
	private String StatusRCTaskFindStr = "\"id\":\"status_robot_controller_task";
	private String NameFindStr = "\"name\":";
	private String EventAlarmFindStr = "\"id\":\"event_alarm";

	public String controllerId = "";
	public String controllerName = "";
	public String robotControllerId = "";
	public String controllerRobotGroupId = "";
	public String statusRobotGroupId = "";
	public String statusRobotTaskId = "";
	public String controllerRobotEventId = "";
	
	
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
	
//	String StatusRcVarsJson = "";
	
	ArrayList<RobotControllerTask> StatusRcTaskList = new ArrayList<RobotControllerTask>();
	
	public boolean DataGood;
	
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
				getControllerRobotEventId() &&
				getStatusRobotTaskId()) {
				DataGood = true;
			}
	}
	
	public ArrayList<RobotControllerTask> getStatusRobotTasks() {
		getStatusRcTaskList();
		
		return StatusRcTaskList;
	}
	
	public RcVariable[] GetStatusRobotVarsJson() {
		getStatusRcVarLst();
		RcVariable[] v = new RcVariable[StatusRcVarLst.size()];
		
		for (int i=0; i<StatusRcVarLst.size(); ++i)
			v[i] = StatusRcVarLst.get(i);
		
		return v;
	}
	
	public RcEventAlarm getRcEventAlarm() {
		String mb = commonDataServiceImp.getLatest(EventAlarmTypeStr, controllerRobotEventId);
		
		return new Gson().fromJson(mb, RcEventAlarm.class);
	}
	
	public RcEventAlarmMoment[] getAllRcEventAlarms() {
		String mb = commonDataServiceImp.getMoments(EventAlarmTypeStr, controllerRobotEventId);
		
		RcEventAlarmMoment[] v = new Gson().fromJson(mb, RcEventAlarmMoment[].class);
		
		return v;
	}
	
	public RcStatusRobotGroup getRobotStatusGroupJson() {

		LatestRcStatusRobotGroup = getLatestStatusRobotGroup();
		
	    return LatestRcStatusRobotGroup;
	}
	
	
	private RcStatusRobotGroup getLatestStatusRobotGroup() {
		String mb = commonDataServiceImp.getLatest(StatusRobotGroupTypeStr, statusRobotGroupId);
		
		return new Gson().fromJson(mb, RcStatusRobotGroup.class);
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
	
	private boolean getControllerRobotEventId() {
		ObjectOfTypeBelongingTo ootbt = 
				new ObjectOfTypeBelongingTo(EventAlarmFindStr, EventAlarmTypeStr, robotControllerId, commonDataServiceImp);

		if (ootbt.Good)
			controllerRobotEventId = ootbt.id;
		
		return ootbt.Good;				
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
	
	private void getStatusRcVarLst() {
		StatusRcVarLst = new ArrayList<RcVariable>();
				
		for (int i=0; i<StatusRcVars.size(); ++i) {
			String mb = commonDataServiceImp.getLatest(StatusRCVarTypeStr, StatusRcVars.get(i)).replace("\n", "");
			StatusRcVarLst.add(new Gson().fromJson(mb, RcVariable.class));
		}	
		
		Collections.sort(StatusRcVarLst, new SortByType());
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

class SortByType implements Comparator<RcVariable>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(RcVariable a, RcVariable b)
    {
    	return a.type.compareTo(b.type);
    }
}
