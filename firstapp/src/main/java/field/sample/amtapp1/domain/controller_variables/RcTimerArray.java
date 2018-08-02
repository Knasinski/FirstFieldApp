package field.sample.amtapp1.domain.controller_variables;

import java.util.ArrayList;

public class RcTimerArray {
	public ArrayList<RcTimer> rcTimers;
	private int IndexUse = 0;
	
	public RcTimerArray() {
		rcTimers = new ArrayList<RcTimer>();
		IndexUse = 0;
	}
	
	public void checkAdd(RcVariable r) {
		
		TypeAndNameMatch(r);
		
		if (IndexUse == -1) {
			rcTimers.add(new RcTimer(r));
		}
		else {
			//Catch decrease in CT (means we have started a new cycle)
			rcTimers.get(IndexUse).Update(r);
		}
	}
	
	private void TypeAndNameMatch(RcVariable r) {
		for (int i=0; i<rcTimers.size(); ++i) {
			String n = rcTimers.get(i).name;
			String t = rcTimers.get(i).type;
			
			if (r.name.equals(n) && r.type.equals(t)) {
				IndexUse = i;
				return;
			}
		}
	
		IndexUse = -1;
	}
	
}
