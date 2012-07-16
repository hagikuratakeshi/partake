package in.partake.controller.api.schedule;

import in.partake.controller.ActionProxy;
import in.partake.controller.api.APIControllerTest;

import org.junit.Test;

public class GetAPITest extends APIControllerTest {
	
    @Test
    public void testGetEvent() throws Exception {
        ActionProxy proxy = getActionProxy(GET, "/api/schedule/get?eventId=" + DEFAULT_EVENT_ID);

        proxy.execute();
        assertResultOK(proxy);
    }
}
