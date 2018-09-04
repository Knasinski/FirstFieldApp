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
	
	public Double Average = 0.0;
	private Double NumRecs = 0.0;
	
	private Double[] bufferedRecs = new Double[40];
	private int	numRecs = 0;
	
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
	
	public void Add2Sum() {
		try {
			Double v = Double.parseDouble(value);
			
			if (v == 0) {
				for (int i=0; i<numRecs; ++i)
					bufferedRecs[i] = 0.0;
				
				numRecs = 0;
			}
			else {
			if (numRecs < 40) {
				bufferedRecs[numRecs] = v;
				++numRecs;
			}
			else {
				for (int i=38; i>0; --i) 
					bufferedRecs[i] = bufferedRecs[i+1];
				
				bufferedRecs[39] = v;
			}
			
			Average = 0.0;
			
			for (int i=0; i<numRecs; ++i)
				Average += bufferedRecs[i];
			
			Average /= numRecs;
			}				
		} catch (Exception e) { }
	}
}
