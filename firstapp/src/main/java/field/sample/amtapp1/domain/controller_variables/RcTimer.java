package field.sample.amtapp1.domain.controller_variables;


import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class RcTimer {
	public String type = "";
	public String name = "";
	public double maxCT = -9999.9;
	public double minCT = 99999.9;
	public double showCT = 0.0;
	public double averageCT = 0.0;
	
	public double sumCT = 0.0;
	public double currentCT = 0.0;
	public double count = 0;
	
	private double lastCT = 0.0;
	
	private static boolean first = true;
	
	private ArrayList<String> debr = new ArrayList<String>();
	
	
	public RcTimer(RcVariable v) {
		try {
			type = v.type;
			name = v.name;
			currentCT = Double.parseDouble(v.value);		
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Update(RcVariable v) {
		currentCT = Double.parseDouble(v.value);
		Double Ddt = (first) ? 0: (currentCT - lastCT);
		first = false;
		
		if (v.type.contains("4")) {
			debr.add(String.format("%f,%f,%f,%f", currentCT, Ddt, showCT, count));
			
			if (debr.size() == 500) {
				ArrayListToFile("J:\\Dropbox\\Work\\AMT\\AmtApplications\\NewFieldProjects\\debug.csv", debr, true);
			}
		}
		
		//Catch decrease in CT (means we have started a new cycle)
		if (Ddt < -4000) {
			if (v.type.contains("4")) {
				showCT = lastCT;
			}
			else
				showCT = lastCT;
				
			sumCT += lastCT;
			
			//Update
			count++;
			
			averageCT = sumCT / count;
			
			if (lastCT < minCT)
				minCT = lastCT;
			
			if (lastCT > maxCT)
				maxCT = lastCT;
		}
		
		lastCT = currentCT;
	}
	 public static void ArrayListToFile(String FilePath, ArrayList<String> Al, Boolean Overwrite) {
		 PrintWriter fs;
         int i;

         try {
             if (Al.size() > 0)
             {
            	 File f = new File(FilePath);
            	 
            	 if(f.exists() && !f.isDirectory())  {
            		 f.delete();
            	 }

            	 f = new File(FilePath);
            	 fs = new PrintWriter(FilePath);
            	 
                 for (i = 0; i < Al.size(); ++i) {
                	 fs.println(Al.get(i).toString());
                 }
                 
                 fs.flush();
                 fs.close();
             }
         } catch (Exception e) {
        	 e.printStackTrace();
         }
     }
}
