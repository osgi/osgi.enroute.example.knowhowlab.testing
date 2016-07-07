package osgi.enroute.example.knowhowlab.testing.archiver;

import org.osgi.service.event.EventConstants;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import osgi.enroute.example.knowhowlab.testing.archiver.api.ArchiverService;

@Component(
		property = EventConstants.EVENT_TOPIC + "=acme/welcome",
		immediate = true
		)
public class ArchiverComponent implements ArchiverService, EventHandler {

	private String lastName;
	
	@Override
	public void handleEvent(Event event) {
		lastName = String.valueOf(event.getProperty("name"));
	}

	@Override
	public String getLastName() {
		return lastName;
	}

}
