package field.sample.amtapp1.domain.controller_variables;

public class CncSensorData {
	public	String	name = "";
	public	String	manufacturer = "";
	public	String	model = "";
	public 	String	description = "";
	public	String	kind = "";
	public 	String	installation = "";
	public	String	usage = "";

	public 	String	status = null;
	public 	String	value	=	"";
	public 	String	unit	=	"";
	
	public CncSensorData(StatusSensorIndividual statusSensor, ControllerSensorIndividual controllerSensor) {
		this.name = controllerSensor.name;
		this.manufacturer = controllerSensor.manufacturer;
		this.model = controllerSensor.model;
		this.description = controllerSensor.description;
		this.kind = controllerSensor.kind;
		this.installation = controllerSensor.installation;
		this.unit = controllerSensor.usage;
		
		this.status = statusSensor.status;
		this.value = statusSensor.data.element[0].value;
		this.unit = statusSensor.data.element[0].unit;
	}
}
