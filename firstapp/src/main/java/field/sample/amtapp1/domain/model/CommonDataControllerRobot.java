package field.sample.amtapp1.domain.model;

import org.springframework.beans.factory.annotation.Autowired;

import field.sample.amtapp1.domain.service.CommonDataService;
import field.sample.amtapp1.domain.model.CommonDataController;
import field.sample.amtapp1.domain.model.CommonDataControllerCnc;
import field.sample.amtapp1.domain.model.CommonDataControllerHistory;
import field.sample.amtapp1.domain.model.CommonDataControllerLatest;
import field.sample.amtapp1.domain.model.CommonDataControllerRelations;

import field.sample.amtapp1.domain.model.Controller;

public class CommonDataControllerRobot {
	public String controllerId = "";
	public String robotControlllerId = "";
	public String jointPose = "";
	public CommonDataLink link = new CommonDataLink();

	@Autowired
	private CommonDataService commonDataServiceImpl;
	
	public int NumExternalAxis = 0;
	
	public int Count = 0;
	
	public double[] JointPosition = {0,0,0,0,0,0,0,0,0};
	public double[] CartesianPosition = {0,0,0,0,0,0,0,0,0};
	public String configuration = "";
	public boolean[] ROs = {false,false,false,false,false,false,false,false};
	public boolean[] UOs = {false,false,false,false,false,false,false,false,false,false,false};
	
	public String status_robot_group_id = "";
	
	
	public String[] ROnames = {"","","","","","","",""};
	public String[] UOnames = {"","","","","","","","","","",""};
	
	public CommonDataControllerRobot(String cid, String rcid) {
		controllerId = cid;
		robotControlllerId = rcid;
		
		if (getCount())
		{
			//START HERE
		}
	}
	
	private boolean getCount() {
		Count = commonDataServiceImpl.getCount("controller_robot_controller", robotControlllerId);
		return (Count != 0);
	}
}
