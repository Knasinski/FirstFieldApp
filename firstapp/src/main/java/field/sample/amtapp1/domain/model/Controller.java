package field.sample.amtapp1.domain.model;

import java.io.Serializable;

public class Controller implements Serializable
{
	/**
	 * This stuff was generated to eliminate a warning
	 */
	private static final long serialVersionUID = 1L;
	private String instanceId; // controller instance id of the common data
	private String name; // controller name of the common data
	private String controllerType; // controller type of the common data
	private String jointPose;		// Joint pose - spaces for all but robots
	
	public Controller() {
	}

public Controller(String instanceId, String name, String controllerType, String jas) {
		this.instanceId = instanceId;
		this.name = name;
		this.controllerType = controllerType;
		this.jointPose = jas;
	}
	public String getInstanceId()  {
		return instanceId;
	}
	public String getName()  {
		return name;
	}
	public String getControllerType()  {
		return controllerType;
	}
	public String getJointPose()  {
		return jointPose;
	}
}