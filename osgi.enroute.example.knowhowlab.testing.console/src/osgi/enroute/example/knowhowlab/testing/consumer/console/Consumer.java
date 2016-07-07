package osgi.enroute.example.knowhowlab.testing.consumer.console;

import org.osgi.service.component.annotations.*;

import osgi.enroute.debug.api.Debug;
import osgi.enroute.example.knowhowlab.testing.api.WelcomeService;

@Component(
		service = Object.class,
		property = {
			Debug.COMMAND_SCOPE + "=test",
			Debug.COMMAND_FUNCTION + "=welcome"
		},
		immediate = true
		)
public class Consumer {
	
	@Reference
	WelcomeService welcome;
	
	public void welcome(String name) {
		System.out.println(welcome.hello(name));
	}

}
