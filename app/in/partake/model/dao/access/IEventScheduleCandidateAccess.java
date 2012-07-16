package in.partake.model.dao.access;

import in.partake.model.dao.DAOException;
import in.partake.model.dao.PartakeConnection;
import in.partake.model.dto.EventScheduleCandidate;
import in.partake.model.dto.EventScheduleCandidate.EventScheduleCandidateId;

import java.util.List;

public interface IEventScheduleCandidateAccess extends IAccess<EventScheduleCandidate, EventScheduleCandidateId> {
    public String getFreshId(PartakeConnection con) throws DAOException;

    public List<EventScheduleCandidate> findByEventId(PartakeConnection con, String eventId) throws DAOException;
    public void removeByEventId(PartakeConnection con, String eventId) throws DAOException;

}
