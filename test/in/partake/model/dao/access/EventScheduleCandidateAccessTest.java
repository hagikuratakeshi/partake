package in.partake.model.dao.access;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import in.partake.app.PartakeApp;
import in.partake.app.PartakeTestApp;
import in.partake.base.PartakeException;
import in.partake.model.IPartakeDAOs;
import in.partake.model.access.Transaction;
import in.partake.model.dao.DAOException;
import in.partake.model.dao.PartakeConnection;
import in.partake.model.dto.EventScheduleCandidate;
import in.partake.model.dto.EventScheduleCandidate.EventScheduleCandidateId;
import in.partake.model.dto.auxiliary.EventScheduleStatus;
import in.partake.model.fixture.impl.EventScheduleCandidateTestDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class EventScheduleCandidateAccessTest extends
		AbstractDaoTestCaseBase<IEventScheduleCandidateAccess, EventScheduleCandidate, EventScheduleCandidateId> {

    private EventScheduleCandidateTestDataProvider provider;
    
	@Override
	@Before
	public void setup() throws Exception {
        super.setup(PartakeApp.getDBService().getDAOs().getEventScheduleCandidateAccess());
        provider = PartakeTestApp.getTestService().getTestDataProviderSet().getEventScheduleCandidateProvider();
	}

	@Override
	protected EventScheduleCandidate create(long pkNumber, String pkSalt, int objNumber) {
		return provider.create(pkNumber, pkSalt, objNumber);
	}

	private EventScheduleCandidate createDefaultEntity(EventScheduleCandidateId id, String eventId, int objNumber) {
		EventScheduleCandidate result = provider.create(1, id.getUUId().toString(), objNumber);
		result.setId(id);
		result.setEventId(eventId);
		return result;
	}
	
	@Test
    public void testFindByEventId() throws Exception {
        final String eventId1 = UUID.randomUUID().toString();
        final String eventId2 = UUID.randomUUID().toString();
        final UUID[] ids = new UUID[] { UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID() };
        final String[] eventIds = new String[] { eventId1, eventId2, eventId2 };
        final List<EventScheduleCandidate> samples = new ArrayList<EventScheduleCandidate>();
        
        for (int i=0; i<ids.length; i++) {
        	samples.add(createDefaultEntity(new EventScheduleCandidateId(ids[i]), eventIds[i], i));
        }
        samples.get(0).getUserScheduleStatusMap().put("user1", EventScheduleStatus.CAN_ATTEND);
        
        new Transaction<Void>() {
            @Override
            protected Void doExecute(PartakeConnection con, IPartakeDAOs daos) throws DAOException, PartakeException {
                for (int i = 0; i < ids.length; ++i) {
                    dao.put(con, samples.get(i));
                }

                con.commit();
                con.beginTransaction();

                List<EventScheduleCandidate> candidatesEvent1 = dao.findByEventId(con, eventIds[0]);
                assertThat(candidatesEvent1.size(), is(1));
                assertThat(candidatesEvent1.get(0).getId().getUUId(), is(ids[0]));
                assertThat(samples.get(0), is(candidatesEvent1.get(0)));

                List<EventScheduleCandidate> candidatesEvent2 = dao.findByEventId(con, eventIds[1]);
                assertThat(candidatesEvent2.size(), is(2));
                
                return null;
            }
        }.execute();

    }
}
