package field.sample.amtapp1.domain.controller_variables;

public class RcVariable {
	public String type = "";
	public String name = "";
	public String unit = "";
	public String value = "";
	
	public String getValueUse() {
		String tempValue = this.value;
		
		if (this.type.startsWith("UO") || this.type.startsWith("UI") ||
				this.type.startsWith("DO") || this.type.startsWith("DI") ||
				this.type.startsWith("SO") || this.type.startsWith("SI") ||
				this.type.startsWith("RO") || this.type.startsWith("RI"))
			tempValue = tempValue.equals("0") ? "OFF" : "ON";			
			
		return tempValue;
	};
}
