package field.sample.amtapp1.domain.controller_variables;

public class StatusSensorData {

	public SensorDataElement[]	element = new SensorDataElement[1];
	
	public StatusSensorData(String value, String unit) {
		this.element[0] = new SensorDataElement(value, unit);
	}
	
	public StatusSensorData(String both) {
		this.element[0] = new SensorDataElement(both);
	}
}
