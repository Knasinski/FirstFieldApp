package field.sample.amtapp1.domain.controller_variables;

public class StatusSensorIndividual {
	String	status = null;
	StatusSensorData data = null;
	
	public StatusSensorIndividual(String s) {
		if (s.contains("\"status\":") && s.contains("\"data\":")) {
			String mb = s.substring(s.indexOf("\"status\":") + 9);
			mb = mb.substring(0,mb.indexOf(","));
			
			if ((mb != null) && !mb.equals("null"))
				status = mb;
			
			mb = s.substring(s.indexOf("\"data\":") + 7);
			data = new StatusSensorData(mb);
		}
	}
}
