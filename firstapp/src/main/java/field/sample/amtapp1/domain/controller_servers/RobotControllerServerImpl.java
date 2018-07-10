package field.sample.amtapp1.domain.controller_servers;

import org.springframework.beans.factory.annotation.Autowired;

import field.sample.amtapp1.domain.model.CommonDataLink;
import field.sample.amtapp1.domain.service.CommonDataService;

public class RobotControllerServerImpl {

	public String controllerId = "";
	public String robotControlllerId = "";
	public String jointPose = "";
	public CommonDataLink link = new CommonDataLink();
	
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
	
	public RobotControllerServerImpl(String cid, String rcid, CommonDataService commonDataServiceImpl) {
		controllerId = cid;
		robotControlllerId = rcid;
		
		CommonDataService x = commonDataServiceImpl;
		robotControlllerId = rcid;
	}
}
