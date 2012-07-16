package in.partake.model.dao.postgres9.impl;

import in.partake.base.TimeUtil;
import in.partake.model.dao.DAOException;
import in.partake.model.dao.DataIterator;
import in.partake.model.dao.MapperDataIterator;
import in.partake.model.dao.PartakeConnection;
import in.partake.model.dao.access.IEventScheduleCandidateAccess;
import in.partake.model.dao.postgres9.Postgres9Connection;
import in.partake.model.dao.postgres9.Postgres9Dao;
import in.partake.model.dao.postgres9.Postgres9DataIterator;
import in.partake.model.dao.postgres9.Postgres9Entity;
import in.partake.model.dao.postgres9.Postgres9EntityDao;
import in.partake.model.dao.postgres9.Postgres9EntityDataMapper;
import in.partake.model.dao.postgres9.Postgres9IdMapper;
import in.partake.model.dao.postgres9.Postgres9IndexDao;
import in.partake.model.dao.postgres9.Postgres9StatementAndResultSet;
import in.partake.model.dto.EventScheduleCandidate;
import in.partake.model.dto.EventScheduleCandidate.EventScheduleCandidateId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;

class EntityEventScheduleCandidateMapper extends Postgres9EntityDataMapper<EventScheduleCandidate> {
	public EventScheduleCandidate map(JSONObject obj) {
		return new EventScheduleCandidate(obj).freeze();
	}
}

public class Postgres9EventScheduleCandidateAccessDao extends Postgres9Dao implements IEventScheduleCandidateAccess {
    static final String ENTITY_TABLE_NAME = "EventScheduleCandidateEntities";
    static final String INDEX_TABLE_NAME = "EventScheduleCandidateIndex";
    static final int CURRENT_VERSION = 1;

    private final Postgres9EntityDao entityDao;
    private final Postgres9IndexDao indexDao;
    private final EntityEventScheduleCandidateMapper mapper;

    public Postgres9EventScheduleCandidateAccessDao() {
        this.entityDao = new Postgres9EntityDao(ENTITY_TABLE_NAME);
        this.indexDao = new Postgres9IndexDao(INDEX_TABLE_NAME);
        this.mapper = new EntityEventScheduleCandidateMapper();
    }
    
	@Override
	public void initialize(PartakeConnection con) throws DAOException {
		Postgres9Connection pcon = (Postgres9Connection) con;
		entityDao.initialize(pcon);
		if (!existsTable(pcon, INDEX_TABLE_NAME)) {
			indexDao.createIndexTable(pcon, "CREATE TABLE " + INDEX_TABLE_NAME
					+ "(id TEXT PRIMARY KEY, eventId TEXT NOT NULL, createdAt TIMESTAMP NOT NULL)");
			indexDao.createIndex(pcon, "CREATE INDEX " + INDEX_TABLE_NAME + "EventId" + " ON " + INDEX_TABLE_NAME
					+ "(eventId, createdAt)");
		}
	}

	@Override
	public void truncate(PartakeConnection con) throws DAOException {
		Postgres9Connection pcon = (Postgres9Connection) con;

	    entityDao.truncate(pcon);
	    indexDao.truncate(pcon);
	}

	@Override
	public void put(PartakeConnection con, EventScheduleCandidate t) throws DAOException {
        Postgres9Connection pcon = (Postgres9Connection) con;

        Postgres9Entity entity = new Postgres9Entity(t.getId().getUUId().toString(), CURRENT_VERSION, t.toJSON().toString().getBytes(UTF8), null, TimeUtil.getCurrentDateTime());
        if (entityDao.exists(pcon, t.getId().getUUId()))
            entityDao.update(pcon, entity);
        else
            entityDao.insert(pcon, entity);
        indexDao.put(pcon, new String[] { "id", "eventId", "createdAt" }, new Object[] { t.getId().getUUId().toString(), t.getEventId(), t.getCreatedAt() } );
	}

	@Override
	public EventScheduleCandidate find(PartakeConnection con, EventScheduleCandidateId id) throws DAOException {
        return mapper.map(entityDao.find((Postgres9Connection) con, id.getUUId().toString()));
	}

	@Override
	public boolean exists(PartakeConnection con, EventScheduleCandidateId id) throws DAOException {
        return entityDao.exists((Postgres9Connection) con, id.getUUId().toString());
	}

	@Override
	public void remove(PartakeConnection con, EventScheduleCandidateId id) throws DAOException {
		Postgres9Connection pcon = (Postgres9Connection) con;

	    entityDao.remove(pcon, id.getUUId().toString());
	    indexDao.remove(pcon, "id", id.getUUId().toString());
	}

	@Override
	public DataIterator<EventScheduleCandidate> getIterator(PartakeConnection con) throws DAOException {
        return new MapperDataIterator<Postgres9Entity, EventScheduleCandidate>(mapper, entityDao.getIterator((Postgres9Connection) con));
	}

	@Override
	public int count(PartakeConnection con) throws DAOException {
        return entityDao.count((Postgres9Connection) con);
	}

	@Override
	public String getFreshId(PartakeConnection con) throws DAOException {
        return entityDao.getFreshId((Postgres9Connection) con);
	}

	@Override
	public List<EventScheduleCandidate> findByEventId(PartakeConnection con, String eventId) throws DAOException {
		Postgres9StatementAndResultSet psars = indexDao.select((Postgres9Connection) con,
                "SELECT id FROM " + INDEX_TABLE_NAME + " WHERE eventId = ? ORDER BY createdAt DESC",
                new Object[] { eventId});

        Postgres9IdMapper<EventScheduleCandidate> idMapper = new Postgres9IdMapper<EventScheduleCandidate>((Postgres9Connection) con, mapper, entityDao);

        DataIterator<EventScheduleCandidate> it = new Postgres9DataIterator<EventScheduleCandidate>(idMapper, psars);
        List<EventScheduleCandidate> schedules = new ArrayList<EventScheduleCandidate>();
        try {
            while (it.hasNext()) {
            	EventScheduleCandidate activity = it.next();
                if (activity != null)
                    schedules.add(activity);
            }
        } finally {
            it.close();
        }

        return schedules;
	}

	@Override
	public void removeByEventId(PartakeConnection con, String eventId) throws DAOException {
		Postgres9StatementAndResultSet psars = indexDao.select((Postgres9Connection) con,
                "SELECT id FROM " + INDEX_TABLE_NAME + " WHERE eventId = ? ",
                new Object[] { eventId });
        try {
            ResultSet rs = psars.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                if (id == null)
                    continue;

                remove(con, new EventScheduleCandidateId(UUID.fromString(id)));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            psars.close();
        }
	}
	
}
