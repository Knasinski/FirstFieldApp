package field.sample.amtapp1.domain.controller_tasks;

public class RobotControllerTask {
	private String NameRecognize =  "\"name\":";
	private String ProgramNameRecognize =  "\"program_name\":";
	private String LineNumberRecognize =  "\"line_number\":";
	private String StatusRecognize =  "\"status\":";
	
	public String Id = "";
	public String Name = "";
	public String ProgramName = "";
	public String LineNumber = "";
	public String Status = "";
	
	public boolean Good = false;
	
	public RobotControllerTask(String Id, String JsonObj) {
		this.Id = Id;		

		this.Name = decoder(NameRecognize, JsonObj);
		this.ProgramName = decoder(ProgramNameRecognize, JsonObj);
		this.LineNumber = decoder(LineNumberRecognize, JsonObj);
		this.Status = decoder(StatusRecognize, JsonObj);
		
		Good = true;
	}
	private String decoder(String Type, String JsonObj) {
		
		String s = JsonObj.substring(JsonObj.indexOf(Type) + Type.length());
		
		if (s.contains(","))
			s = s.substring(0,s.indexOf(","));
		
		if (!s.contains("null")) {
			if (s.contains("\""))
				s = s.substring(s.indexOf("\""));
			
			if (s.contains("\""))
				s = s.replace("\"", "");
			
			if (s.contains("}"))
				s = s.replace("}", "");
			
			if (s.contains("\n"))
				s = s.replace("\n", "");
			
			return s;
		}
		
		return "";
	}

}
