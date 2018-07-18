package field.sample.amtapp1.utility_programs;

public class DecodeConfig {
	private String NameRecognize = "\"configuration\":";
	public boolean Good;
	public String result;

	public DecodeConfig(String s) {
		if (s.contains(NameRecognize)) {
			result = s.substring(s.indexOf(NameRecognize)+NameRecognize.length());
			result = result.replaceAll("\"", "").replaceAll("}", "").replaceAll("\n", "");
			
			Good = true;
		}
		else {
			Good = false;
		}
	}
}
