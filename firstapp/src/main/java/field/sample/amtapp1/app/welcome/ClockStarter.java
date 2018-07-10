package field.sample.amtapp1.app.welcome;

import java.util.Locale;

import org.springframework.ui.Model;

import field.sample.amtapp1.app.welcome.InternalClock;


public class ClockStarter {
	public static Locale locale;
	public static Model model;

	public ClockStarter(Locale l, Model m) {
		locale = l;
		model = m;
		
		InternalClock tim = new InternalClock();
		tim.start();
	}
}
