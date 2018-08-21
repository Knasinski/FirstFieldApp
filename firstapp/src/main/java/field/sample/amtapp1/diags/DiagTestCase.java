package field.sample.amtapp1.diags;

import java.net.InetAddress;

public class DiagTestCase {
	public String testName = null;
	public boolean isError = false;
	public boolean isReachable = false;
	public String errorMessage = null;
	public String result = null;
	
	public DiagTestCase() { }
	public DiagTestCase(String testName) {
		this.testName = testName;
	}
	
	public DiagTestCase(String testName, String hostName) {
		try {
				this.testName = testName;
				this.result = (hostName.length() == 0) ? 
						InetAddress.getLocalHost().toString() : 
							InetAddress.getByName(hostName).toString();
				
				if (this.result.contains("/")) {
					this.result = this.result.substring(this.result.indexOf("/") + 1);
				}		
				
				isReachable = InetAddress.getByName(hostName).isReachable(10000);
			} catch (Exception e) {
				errorMessage = e.getMessage();
				isError = true;
			}
	}
}
