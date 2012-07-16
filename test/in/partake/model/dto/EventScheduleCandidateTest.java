package in.partake.model.dto;

import in.partake.app.PartakeTestApp;
import in.partake.model.fixture.TestDataProvider;
import junit.framework.Assert;

import org.junit.Test;

public class EventScheduleCandidateTest extends AbstractPartakeModelTest<EventScheduleCandidate> {

	@Override
	protected TestDataProvider<EventScheduleCandidate> getTestDataProvider() {
        return PartakeTestApp.getTestService().getTestDataProviderSet().getEventScheduleCandidateProvider();
	}

	@Override
	protected EventScheduleCandidate copy(EventScheduleCandidate t) {
		return new EventScheduleCandidate(t);
	}
	
	@Test
    public void testToCopy() {
        EventScheduleCandidate candidate = getTestDataProvider().create();
        EventScheduleCandidate copied = new EventScheduleCandidate(candidate);
        Assert.assertEquals(candidate, copied);
    }	
}
