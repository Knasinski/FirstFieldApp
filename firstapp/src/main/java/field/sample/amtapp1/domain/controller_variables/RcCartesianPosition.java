package field.sample.amtapp1.domain.controller_variables;

public class RcCartesianPosition {
	public String type = "";
	public double[] value = {0,0,0,0,0,0,0,0};
	public String[] unit = {"", "", "", "", "", "", ""};
	public String configuration = "";
	
	private double[] zip  = {0,0,0,0,0,0,0,0};
	private String[] initUnit = {"", "", "", "", "", "", ""};
	
	public RcCartesianPosition(String t, double[] v, String[] u, String c) {
		this.type = t;
		this.value = v;
		this.unit = u;
		this.configuration = c;
	}
	
	public RcCartesianPosition() {
		this.type = "";
		this.value = zip;
		this.unit = initUnit;
		this.configuration = "";
	}
}
