package field.sample.amtapp1.domain.controller_servers;

import java.util.ArrayList;

import com.google.gson.Gson;

import field.sample.amtapp1.domain.controller_variables.CncSensorData;
import field.sample.amtapp1.domain.controller_variables.ControllerSensorIndividual;
import field.sample.amtapp1.domain.controller_variables.RcEventAlarm;
import field.sample.amtapp1.domain.controller_variables.StatusSensorIndividual;
import field.sample.amtapp1.domain.service.CommonDataService;
import field.sample.amtapp1.utility_programs.DecodeId;
import field.sample.amtapp1.utility_programs.DecodeName;

public class CncControllerServer {

	private String CncControllerTypeStr = "controller_cnc";
	private String ControllerSensorTypeStr = "controller_sensor";
	private String ControllerSensorIndividualTypeStr = "controller_sensor_individual";
	private String StatusSensorIndividualTypeStr = "status_sensor_individual";

	private String ControllerSensorFindStr = "\"id\":\"controller_sensor";
	private String ControllerSensorIndividualFindStr = "\"id\":\"controller_sensor_individual";
	private String StatusSensorIndividualFindStr = "\"id\":\"status_sensor_individual";
	
	ArrayList<String> ControllerSensorIndividuals = new ArrayList<String>();
	ArrayList<String> StatusSensorIndividuals = new ArrayList<String>();

	public String controllerId = "";
	public String controllerName = "";
	public String cncControllerId = "";
	public String sensorCompId = "";
	
	public StatusSensorIndividual[]  StatusSensorIndividualData = null;
	public ControllerSensorIndividual[] ControllerSensorIndividualData = null;
	
	public CncSensorData[] CncSensorDataArray = null;
	
	private CommonDataService commonDataServiceImp;
	
	public boolean DataGood;
	
	public CncControllerServer(String cid, CommonDataService cdsi) {
		controllerId = cid;
		
		commonDataServiceImp = cdsi;
		DataGood = false;

		//Get all required data
		if (getControllerName() && 
				getCncControllerId() &&
				getControllerSensors()) {
				CncSensorDataArray = new CncSensorData[ControllerSensorIndividuals.size()];
				
				StatusSensorIndividualData = new StatusSensorIndividual[ControllerSensorIndividuals.size()];
				ControllerSensorIndividualData = new ControllerSensorIndividual[ControllerSensorIndividuals.size()];
								
				startSensorMonitoring();
				
				DataGood = true;
		}
		
	}
	
	public CncSensorData[] getCncSensorData() {
		return CncSensorDataArray;
	}
	
	private void UpdateSensorData() {
		for (int i=0; i<StatusSensorIndividualData.length; ++i) {
			String mb = commonDataServiceImp.getLatest(ControllerSensorIndividualTypeStr, ControllerSensorIndividuals.get(i)).toString();
			
			ControllerSensorIndividual csi = new Gson().fromJson(mb, ControllerSensorIndividual.class);
			
			mb  = commonDataServiceImp.getLatest(StatusSensorIndividualTypeStr, StatusSensorIndividuals.get(i)).toString();
			StatusSensorIndividual ssi = new StatusSensorIndividual(mb);
			
			CncSensorDataArray[i] = new CncSensorData(ssi, csi);
		}
	}
	private void startSensorMonitoring() {
		boolean KeepOn = true;
		Thread t = new Thread() {
        @Override
        public void run() {  // override the run() to specify the running behavior
        	while (KeepOn) {
        		UpdateSensorData();
	        	
	        	try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    };
		
	t.start();			
	}
	
	private boolean getControllerName() {
		String mb = commonDataServiceImp.getInstance("controller", controllerId).toString();
		DecodeName rc = new DecodeName(mb);
		
		if (rc.Good)
			controllerName = rc.result;
		
		return rc.Good;
	}
	
	private boolean getCncControllerId() {
		String mb = commonDataServiceImp.getRelations("controller", controllerId).toString();
		
		DecodeId rc = new DecodeId(CncControllerTypeStr, mb);
		
		if (rc.Good) {
			cncControllerId = rc.result;
		
			//Shortcut: Get the 2nd controller id
			
			String s = controllerId.substring(14);
			int c1 = Integer.parseInt(s);
			
			sensorCompId = String.format("controller%05d", c1+1);			
			}
		
		return rc.Good;
	}
	
	private boolean getControllerSensors() {
		
		ArrayList<String> Ccnc = new ArrayList<String>();
		ControllerSensorIndividuals = new ArrayList<String>();
		StatusSensorIndividuals = new ArrayList<String>();
		
		//Build Ccnc - local array of all controller_cnc's
		String mb = commonDataServiceImp.getInstances(ControllerSensorTypeStr).toString();
		
		try {
			while (mb.contains(ControllerSensorFindStr)) {
				mb = mb.substring(mb.indexOf(ControllerSensorFindStr) + 6);
				
				String s = mb.substring(0, mb.indexOf("\""));
				
				Ccnc.add(s);
			} 
		} catch (Exception w) {
			return false;
		}
		
		//Loop thru Ccnc processing the controller_sensor_individual & status_sensor_individual
		for (String rCnc:Ccnc ) {
			StringBuffer msb = commonDataServiceImp.getRelations(ControllerSensorTypeStr, rCnc);
			mb = msb.toString();
			
			if (mb.contains(sensorCompId)) {
				//Everything appears in order, make list of controller_sensor_individuals				
				while (mb.contains(ControllerSensorIndividualFindStr)) {
					mb = mb.substring(mb.indexOf(ControllerSensorIndividualFindStr));
					String s = mb.substring(6);
					s = s.substring(0, s.indexOf("\""));
					ControllerSensorIndividuals.add(s);
					mb = mb.substring(5);
					
					//Get relations looking for status_sensor_individual
					String b1 = commonDataServiceImp.getRelations(ControllerSensorIndividualTypeStr, s).toString();
					
					if (b1.contains(StatusSensorIndividualFindStr)) {
						b1 = b1.substring(b1.indexOf(StatusSensorIndividualFindStr));
						b1 = b1.substring(6);
						b1 = b1.substring(0, b1.indexOf("\""));
						StatusSensorIndividuals.add(b1);
					}		
					else
						return false;
				}

				return true;
			}
		}
		
		return false;
	}
}
