package field.sample.amtapp1.utility_programs;

public class DecodeId {
	private String IdRecognize = "\"id\":";
	public boolean Good;
	public String result;
	
	public DecodeId(String containsMe, String s) {
		if (s.contains(containsMe) && s.contains(IdRecognize) && s.contains(",")) {
			result = s.substring(s.indexOf(IdRecognize)+IdRecognize.length(), s.indexOf(",")).replaceAll("\"", "");
			
			Good = true;
		}
		else {
			Good = false;
		}
	}
}
