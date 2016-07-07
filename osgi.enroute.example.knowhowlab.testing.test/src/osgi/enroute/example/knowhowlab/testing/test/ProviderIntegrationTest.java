package osgi.enroute.example.knowhowlab.testing.test;

import static java.util.Collections.singletonMap;

import java.util.concurrent.TimeUnit;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.assertions.cmpn.ConfigurationAdminAssert;
import org.knowhowlab.osgi.testing.utils.BundleUtils;
import org.knowhowlab.osgi.testing.utils.ServiceUtils;
import org.knowhowlab.osgi.testing.utils.cmpn.ConfigurationAdminUtils;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

import osgi.enroute.example.knowhowlab.testing.api.WelcomeService;

@RunWith(MockitoJUnitRunner.class)
public class ProviderIntegrationTest {
	private static final BundleContext bc = FrameworkUtil.getBundle(ProviderIntegrationTest.class).getBundleContext();
	
	@After
	public void after() {
		ConfigurationAdminUtils.deleteConfiguration(bc, "welcome-provider", null, 0);
		ConfigurationAdminAssert.assertConfigurationDeleted("welcome-provider", null, null, 500, TimeUnit.MILLISECONDS);
}
	
	@Test
	public void testProvider() throws InvalidSyntaxException {
		ServiceAssert.assertServiceUnavailable(WelcomeService.class);
		
		ConfigurationAdminUtils.supplyConfiguration(bc, "welcome-provider", null, singletonMap(".pattern", "Hi %s!"), 0);
		
		ConfigurationAdminAssert.assertConfigurationUpdated("welcome-provider", null, null, 500, TimeUnit.MILLISECONDS);
		
		ConfigurationAdminAssert.assertConfigurationAvailable("welcome-provider", (String)null, null);
		
		ServiceComponentRuntime scr = ServiceUtils.getService(bc, ServiceComponentRuntime.class);
		
		ComponentDescriptionDTO dto = scr.getComponentDescriptionDTO(BundleUtils.findBundle(bc, "osgi.enroute.example.knowhowlab.testing.provider"), "osgi.enroute.example.knowhowlab.testing.provider.WelcomeProvider");
		Assert.assertThat(scr.isComponentEnabled(dto), CoreMatchers.is(true));
		
		ServiceAssert.assertServiceAvailable(WelcomeService.class, 5, TimeUnit.SECONDS);
		
		WelcomeService ws = ServiceUtils.getService(bc, WelcomeService.class);
		
		Assert.assertThat(ws.hello("John"), CoreMatchers.is("Hi John!"));
	}
}