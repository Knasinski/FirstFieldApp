package field.sample.amtapp1.utility_programs;

import java.util.ArrayList;

import field.sample.amtapp1.domain.service.CommonDataService;

public class ObjectOfTypeBelongingTo {
	
	public String id = "";
	public boolean Good=false;
	
	private CommonDataService commonDataServiceImp; 
	
	public ObjectOfTypeBelongingTo(String recogType, String findType, String belongsTo, CommonDataService cdsi ) {
		
		commonDataServiceImp = cdsi;
		String mb = commonDataServiceImp.getInstances(findType);
		
		ArrayList<String> idLst = IdsFound(mb, recogType);
		
		for (int i=0; i<idLst.size(); ++i) {
			String s = commonDataServiceImp.getRelations("event_alarm", idLst.get(i));
			
			if (s.contains("\"id\":")) {
				s = s.substring(s.indexOf("\"id\":") + "\"id\":".length()+1);
				
				if (s.contains("\"")) {
					s = s.substring(0, s.indexOf("\""));
					
					if (s.equals(belongsTo)) {
						id = idLst.get(i);
						Good=true;
						return;
					}
				}
			}
		}
	}
	
	
public ArrayList<String> IdsFound(String InStr, String recogType) {
	ArrayList<String> als = new ArrayList<String>();
	String mb = InStr;
	
	while (mb.contains(recogType)) {
		mb = mb.substring(mb.indexOf(recogType));
		
		if (mb.contains("\"")) {
			String s = mb.substring(mb.indexOf(":")+2);
			
			if (s.contains("\"")) {
				s = s.substring(0, s.indexOf("\""));
				als.add(s);
			}
		}
		
		mb = mb.substring(1);
	}
	
	return als;
	}

}
