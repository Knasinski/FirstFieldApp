package field.sample.amtapp1.domain.controller_variables;

public class RcJointPosition {
	public double[] value = {0,0,0,0,0,0,0,0};
	String[] unit = {"", "", "", "", "", "", ""};
	
	private double[] zip  = {0,0,0,0,0,0,0,0};
	private String[] initUnit = {"", "", "", "", "", "", ""};
	
	public RcJointPosition() {
		this.value = zip;
		this.unit = initUnit;
	}
}
