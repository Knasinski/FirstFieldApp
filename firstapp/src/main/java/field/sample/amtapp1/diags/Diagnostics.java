package field.sample.amtapp1.diags;

import java.net.InetAddress;

public class Diagnostics {
	public DiagTestCase hostIP;
	public DiagTestCase restIP;
	
	public String errorMessage = "";
	
	private InetAddress internalHostAddress = null;
	private InetAddress internalRestAddress = null;
	
	public Diagnostics() {
		try {
			hostIP = new DiagTestCase("Host IP ", "");
			
			internalHostAddress = InetAddress.getLocalHost();
			} catch (Exception e) { 
				hostIP.isError = true;
				hostIP.errorMessage = "Error: cannot get local host";
			}
		
		try {
			restIP = new DiagTestCase("Rest IP", "api-rest");
			
			internalRestAddress = InetAddress.getByName("api-rest");
			} catch (Exception e) { 
				
				errorMessage = "Error: api-rest not found or unreachable";
			}
	}
	
	public InetAddress getHostAddress() {
		return internalHostAddress;
	}
	
	public InetAddress getRestAddress() {
		return internalRestAddress;
	}
}
