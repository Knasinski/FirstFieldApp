package field.sample.amtapp1.utility_programs;

public class DecodeType {
	private String TypeRecognize = "\"type\":";
	public boolean Good;
	public String result;

	public DecodeType(String s) {
		if (s.contains(TypeRecognize) && s.contains(",")) {
			result = s.substring(s.indexOf(TypeRecognize)+TypeRecognize.length());
			result = result.substring(0,result.indexOf(','));
			result = result.replaceAll("\"", "").replaceAll("}", "").replaceAll("\n", "");
			
			Good = true;
		}
		else {
			Good = false;
		}
	}
}
