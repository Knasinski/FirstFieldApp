package field.sample.amtapp1.domain.controller_variables;

import field.sample.amtapp1.domain.service.CommonDataService;

public class RobotControllerVariable {
	private String TypeRecognize =  "\"type\":";
	private String NameRecognize =  "\"name\":";
	private String UnitRecognize =  "\"unit\":";
	private String ValueRecognize =  "\"value\":";
	
	public String Id = "";
	public String Type = "";
	public String Name = "";
	public String Unit = "";
	public String Value = "";
	
	public boolean Good = false;
	private String StatusRCVarTypeStr = "status_robot_controller_variable";
	
	public RobotControllerVariable(String Id, String Type, String Name, String Unit, String Value) {
		this.Id = Id;
		this.Type = Type;
		this.Name = Name;
		this.Unit = Unit;
		this.Value = Value;
		Good = true;
	};
	
	public RobotControllerVariable(String Id, String JsonObj) {
		if (JsonObj.contains(TypeRecognize) && JsonObj.contains(TypeRecognize) && JsonObj.contains(TypeRecognize)) {
			this.Id = Id;
			this.Type =  decoder(TypeRecognize, JsonObj);
			this.Name = decoder(NameRecognize, JsonObj);
			this.Unit = decoder(UnitRecognize, JsonObj);
			
			//Handle value processing
			String tempValue = decoder(ValueRecognize, JsonObj);
			
			if (this.Type.startsWith("UO") || this.Type.startsWith("UI") ||
					this.Type.startsWith("DO") || this.Type.startsWith("DI") ||
					this.Type.startsWith("SO") || this.Type.startsWith("SI") ||
					this.Type.startsWith("RO") || this.Type.startsWith("RI"))
				tempValue = tempValue.equals("0") ? "OFF" : "ON";			
			
			this.Value = convertValueString(JsonObj);
		}
	};
	
	public void UpDateValue(CommonDataService commonDataServiceImp) {
		String mb = commonDataServiceImp.getLatest(StatusRCVarTypeStr, Id);
		
		this.Value = convertValueString(mb);
	}
	
	public static int Compare(RobotControllerVariable a, RobotControllerVariable b) {
		RobotControllerVariable A = a;
		RobotControllerVariable B = b;
		
		return A.Type.compareTo(B.Type);
	}
	
	private String convertValueString(String b) {
		String tempValue = decoder(ValueRecognize, b);
		
		if (this.Type.startsWith("UO") || this.Type.startsWith("UI") ||
				this.Type.startsWith("DO") || this.Type.startsWith("DI") ||
				this.Type.startsWith("SO") || this.Type.startsWith("SI") ||
				this.Type.startsWith("RO") || this.Type.startsWith("RI"))
			tempValue = tempValue.equals("0") ? "OFF" : "ON";
		
		return tempValue;
	}
	
	private String decoder(String Type, String JsonObj) {
		
		String s = JsonObj.substring(JsonObj.indexOf(Type) + Type.length());
		
		if (s.contains(","))
			s = s.substring(0,s.indexOf(","));
		
		if (!s.contains("null")) {
			s = s.substring(s.indexOf("\""));
			s = s.replace("\"", "");
			s = s.replace("}", "");
			s = s.replace("\n", "");
			
			return s;
		}
		
		return "";
	}
}
