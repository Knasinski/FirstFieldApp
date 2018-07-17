package field.sample.amtapp1.utility_programs;

public class DecodeValue {
	private String TypeRecognize = "\"value\":";
	public boolean Good;
	public String result;

	public DecodeValue(String s) {
		if (s.contains(TypeRecognize)) {
			result = s.substring(s.indexOf(TypeRecognize)+TypeRecognize.length());
			result = result.replaceAll("\"", "").replaceAll("}", "").replaceAll("\n", "");
			
			Good = true;
		}
		else {
			Good = false;
		}
	}
}
