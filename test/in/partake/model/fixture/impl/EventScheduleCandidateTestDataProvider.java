package in.partake.model.fixture.impl;

import in.partake.base.DateTime;
import in.partake.base.TimeUtil;
import in.partake.model.IPartakeDAOs;
import in.partake.model.dao.DAOException;
import in.partake.model.dao.PartakeConnection;
import in.partake.model.dao.access.IEventScheduleCandidateAccess;
import in.partake.model.dto.EventScheduleCandidate;
import in.partake.model.dto.EventScheduleCandidate.EventScheduleCandidateId;
import in.partake.model.dto.auxiliary.EventScheduleStatus;
import in.partake.model.fixture.TestDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventScheduleCandidateTestDataProvider extends TestDataProvider<EventScheduleCandidate> {

	@Override
	public EventScheduleCandidate create(long pkNumber, String pkSalt, int objNumber) {
		UUID uuid = new UUID(pkNumber, pkSalt.hashCode());
		EventScheduleCandidateId scheduleId = new EventScheduleCandidateId(uuid); 
		DateTime now = TimeUtil.getCurrentDateTime(); 
		Map<String, EventScheduleStatus> userScheduleMap = new HashMap<String, EventScheduleStatus>();
        return new EventScheduleCandidate(scheduleId, "eventId", new DateTime(0), new DateTime(0), "freeText" + objNumber, userScheduleMap, now, now);
	}

	@Override
	public List<EventScheduleCandidate> createSamples() {
		List<EventScheduleCandidate> result = new ArrayList<EventScheduleCandidate>();
		
		DateTime now = TimeUtil.getCurrentDateTime(); 
		Map<String, EventScheduleStatus> userScheduleMap = new HashMap<String, EventScheduleStatus>();
		userScheduleMap.put("user1", EventScheduleStatus.CAN_ATTEND);
		userScheduleMap.put("user2", EventScheduleStatus.NOT_ATTEND);
		userScheduleMap.put("user3", EventScheduleStatus.MAYBE_ATTEND);
		
		EventScheduleCandidateId scheduleId1 = new EventScheduleCandidateId(UUID.randomUUID()); 
		EventScheduleCandidateId scheduleId2 = new EventScheduleCandidateId(UUID.randomUUID()); 
		EventScheduleCandidateId scheduleId3 = new EventScheduleCandidateId(UUID.randomUUID()); 
		EventScheduleCandidateId scheduleId4 = new EventScheduleCandidateId(UUID.randomUUID()); 
        result.add(new EventScheduleCandidate(scheduleId1, "eventId", new DateTime(0), new DateTime(0), "freeText", userScheduleMap, now, now));
        result.add(new EventScheduleCandidate(scheduleId2, "eventId", new DateTime(0), new DateTime(0), "freeText2", userScheduleMap, now, now));
        result.add(new EventScheduleCandidate(scheduleId3, "eventId3", new DateTime(0), new DateTime(0), "freeText2", userScheduleMap, now, now));
        result.add(new EventScheduleCandidate(scheduleId4, "eventId4", new DateTime(0), new DateTime(0), "freeText3", userScheduleMap, now, now));
        
        return result;
	}

	@Override
	public void createFixtures(PartakeConnection con, IPartakeDAOs daos) throws DAOException {
		IEventScheduleCandidateAccess dao = daos.getEventScheduleCandidateAccess();
        dao.truncate(con);	
	}

}
