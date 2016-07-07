package osgi.enroute.example.knowhowlab.testing.provider;

import static java.util.Collections.singletonMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import osgi.enroute.example.knowhowlab.testing.api.WelcomeService;

@Component(
		configurationPolicy = ConfigurationPolicy.REQUIRE,
		configurationPid = "welcome-provider"
		)
@Designate(
		ocd = WelcomeProvider.Config.class
		)
public class WelcomeProvider implements WelcomeService {

	@Reference
	EventAdmin ea;
	
	@ObjectClassDefinition(
			name = "Welcome Provider"
			)
	@interface Config {
		@AttributeDefinition(
				name = ".pattern",
				type = AttributeType.STRING,
				defaultValue = "Welcome %s!"
				)
		String _pattern();
	}

	private String pattern;

	@Activate
	public void activate(Config config) {
		pattern = config._pattern();
	}
	
	@Override
	public String hello(String name) {
		ea.postEvent(new Event("acme/welcome", singletonMap("name", name)));
		return String.format(pattern, name);
	}

}
