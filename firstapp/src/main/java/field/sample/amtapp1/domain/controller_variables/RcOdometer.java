package field.sample.amtapp1.domain.controller_variables;

import com.google.gson.Gson;

import field.sample.amtapp1.domain.service.CommonDataService;

public class RcOdometer {
	private String StatusRobotGroupTypeStr = "status_robot_group";
	private CommonDataService commonDataServiceImp;
	
	public RcStatusRobotGroup currentStatusRobotGroup;
	private RcCartesianPosition lastCartPosition = null;
	public RcCartesianPosition odometerCartPosition = null;
	private RcJointPosition lastJointPosition = null;
	public RcJointPosition odometerJointPosition = null;
	
	public RcOdometer(String mb, CommonDataService cdsi) {
		commonDataServiceImp = cdsi;
		
		currentStatusRobotGroup =  new Gson().fromJson(mb, RcStatusRobotGroup.class);
		lastCartPosition = currentStatusRobotGroup.cartesian_position;
		lastJointPosition = currentStatusRobotGroup.joint_position;
		odometerCartPosition = new RcCartesianPosition();
		odometerJointPosition = new RcJointPosition();
	}
	
	public void upDate(String statusRobotGroupId) {
		double[] dxdydz = {0,0,0};
		
		try {
		currentStatusRobotGroup =  
				new Gson().fromJson(commonDataServiceImp.getLatest(StatusRobotGroupTypeStr, 
						statusRobotGroupId).toString(), RcStatusRobotGroup.class);
		
		for (int i=0; i<odometerJointPosition.value.length; ++i) {			
				if ((currentStatusRobotGroup.joint_position != null) && (currentStatusRobotGroup.cartesian_position != null)) {
					double dj = Math.abs(currentStatusRobotGroup.joint_position.value[i] - lastJointPosition.value[i]);
					odometerJointPosition.value[i] += dj;
					
					if (i<3) {
						double dp = Math.abs(currentStatusRobotGroup.cartesian_position.value[i] - lastCartPosition.value[i]);
						odometerCartPosition.value[i] += dp;
						dxdydz[i] = dp;
					}
					else if (i==3) {
						odometerCartPosition.value[i] = Math.sqrt(Math.pow(odometerCartPosition.value[0], 2) + 
								Math.pow(odometerCartPosition.value[1], 2) + Math.pow(odometerCartPosition.value[2], 2));
					}
				}
			}
		
		lastCartPosition = currentStatusRobotGroup.cartesian_position;
		lastJointPosition = currentStatusRobotGroup.joint_position;	
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

}
