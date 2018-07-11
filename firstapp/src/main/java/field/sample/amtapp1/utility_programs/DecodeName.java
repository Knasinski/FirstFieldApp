package field.sample.amtapp1.utility_programs;

public class DecodeName {
	private String NameRecognize = "\"name\":";
	public boolean Good;
	public String result;

	public DecodeName(String s) {
		if (s.contains(NameRecognize) && s.contains(",")) {
			result = s.substring(s.indexOf(NameRecognize)+NameRecognize.length());
			result = result.replaceAll("\"", "").replaceAll("}", "").replaceAll("\n", "");
			
			Good = true;
		}
		else {
			Good = false;
		}
	}
}
