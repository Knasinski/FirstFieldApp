package field.sample.amtapp1.domain.controller_variables;

public class SensorDataElement {
	public String	value	=	"";
	public String	unit	=	"";
	
	public SensorDataElement(String value, String unit) {
		this.value = value;
		this.unit = unit;
	}
	
	public SensorDataElement(String both) {
		String b1 = both.substring(both.indexOf(":") + 1);
		b1 = b1.substring(0,b1.indexOf(","));
		b1 = b1.replaceAll("\"", "");
		
		String b2 = both.substring(both.indexOf("unit")+4);
		b2 = b2.replaceAll("\"", "").replace(":", "");
		
		this.value = b1;
		this.unit = b2.substring(0,b2.indexOf("}"));
	}
}
