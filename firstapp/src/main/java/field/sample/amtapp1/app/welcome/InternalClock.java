package field.sample.amtapp1.app.welcome;



//pLocal

public class InternalClock extends Thread {
		public void run() {
			
			boolean stop = false;
			
			do {		
//				ClockStarter.model.addAttribute("serverTime", formattedDate);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				ClockStarter.model.addAttribute("serverTime", "XXXXX");
			} while (!stop);
		}
	
}
