package in.partake.model.dto;

import in.partake.base.DateTime;
import in.partake.base.Util;
import in.partake.model.dto.auxiliary.EventScheduleStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang.ObjectUtils;

import net.sf.json.JSONObject;

public class EventScheduleCandidate extends PartakeModel<EventScheduleCandidate> {

	public static class EventScheduleCandidateId extends PartakeModelIdHolder {
		private UUID id; 
		public EventScheduleCandidateId(UUID id) {
			this.id = id;
		}
		@Override
		public UUID getUUId() {
			return id;
		}
	}
	
	private EventScheduleCandidateId id;
	private String eventId;
	private DateTime beginDate;
	private DateTime endDate;
	private String freeText;
	private Map<String, EventScheduleStatus> userScheduleStatusMap;
	private DateTime createdAt;
	private DateTime modifiedAt;

	public EventScheduleCandidate(EventScheduleCandidate schedule) {
		this(schedule.getId(), schedule.getEventId(), schedule.getBeginDate(), schedule.getEndDate(), schedule
				.getFreeText(), schedule.getUserScheduleStatusMap(), schedule.getCreatedAt(), schedule.getModifiedAt());
	}
	
	public EventScheduleCandidate(EventScheduleCandidateId id, String eventId, DateTime beginDate, DateTime endDate, String freeText,
			Map<String, EventScheduleStatus> userScheduleStatusMap, DateTime createdAt, DateTime modifiedAt) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.freeText = freeText;
		this.userScheduleStatusMap = new HashMap<String, EventScheduleStatus>(userScheduleStatusMap);
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public EventScheduleCandidate(JSONObject obj) {
		this.id = new EventScheduleCandidateId(UUID.fromString(obj.getString("id")));
		this.eventId = obj.getString("eventId");
		if (obj.containsKey("beginDate"))
			this.beginDate = new DateTime(obj.getLong("beginDate"));
		if (obj.containsKey("endDate"))
			this.endDate = new DateTime(obj.getLong("endDate"));
		this.freeText = obj.getString("freeText");
		if (obj.containsKey("userScheduleStatusMap")) {
			Map<String, EventScheduleStatus> parsedUserScheduleMap = Util.parseUserScheduleCandidate(obj.getJSONObject("userScheduleStatusMap"));
			this.userScheduleStatusMap = parsedUserScheduleMap;
		}
		if (obj.containsKey("createdAt"))
			this.createdAt = new DateTime(obj.getLong("createdAt"));
		if (obj.containsKey("modifiedAt"))
			this.modifiedAt = new DateTime(obj.getLong("modifiedAt"));
	}
	  
	@Override
	public Object getPrimaryKey() {
		return id;
	}

	@Override
	public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", id.getUUId().toString());
        obj.put("eventId", eventId);
        if (beginDate != null)
            obj.put("beginDate", beginDate.getTime());
        if (endDate != null)
            obj.put("endDate", endDate.getTime());
        obj.put("freeText", freeText);
        if (userScheduleStatusMap != null) {
        	JSONObject userScheduleMapJson = new JSONObject();
        	for (Entry<String, EventScheduleStatus> entry : userScheduleStatusMap.entrySet()) {
        		userScheduleMapJson.put(entry.getKey(), entry.getValue());
        	}
        	obj.put("userScheduleStatusMap", userScheduleMapJson);
        }
        if (createdAt != null)
            obj.put("createdAt", createdAt.getTime());
        if (modifiedAt != null)
            obj.put("modifiedAt", modifiedAt.getTime());
		return obj;
	}

	@Override
	public int hashCode() {
	    int code = 0;
        code = code * 37 + ObjectUtils.hashCode(id);
        code = code * 37 + ObjectUtils.hashCode(eventId);
        code = code * 37 + ObjectUtils.hashCode(beginDate);
        code = code * 37 + ObjectUtils.hashCode(endDate);
        code = code * 37 + ObjectUtils.hashCode(freeText);
        code = code * 37 + ObjectUtils.hashCode(userScheduleStatusMap);
        code = code * 37 + ObjectUtils.hashCode(createdAt);
        code = code * 37 + ObjectUtils.hashCode(modifiedAt);
        return code;
	}

	@Override
	public boolean equals(Object obj) {
       if (!(obj instanceof EventScheduleCandidate)) { return false; }

       EventScheduleCandidate lhs = this;
       EventScheduleCandidate rhs = (EventScheduleCandidate) obj;

       if (!ObjectUtils.equals(lhs.id,        rhs.id))        { return false; }
       if (!ObjectUtils.equals(lhs.eventId,   rhs.eventId))   { return false; }
       if (!ObjectUtils.equals(lhs.beginDate,     rhs.beginDate))     { return false; }
       if (!ObjectUtils.equals(lhs.freeText,     rhs.freeText))     { return false; }
       if (!ObjectUtils.equals(lhs.userScheduleStatusMap,     rhs.userScheduleStatusMap))     { return false; }
       if (!ObjectUtils.equals(lhs.endDate,   rhs.endDate))   { return false; }
       if (!ObjectUtils.equals(lhs.createdAt, rhs.createdAt)) { return false; }
       if (!ObjectUtils.equals(lhs.modifiedAt, rhs.modifiedAt)) { return false; }

       return true;
	}

	public EventScheduleCandidateId getId() {
		return id;
	}

	public String getEventId() {
		return eventId;
	}

	public DateTime getBeginDate() {
		return beginDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public String getFreeText() {
		return freeText;
	}

	public Map<String, EventScheduleStatus> getUserScheduleStatusMap() {
		return userScheduleStatusMap;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	public DateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setId(EventScheduleCandidateId id) {
		checkFrozen();
		this.id = id;
	}

	public void setEventId(String eventId) {
		checkFrozen();
		this.eventId = eventId;
	}

	public void setBeginDate(DateTime beginDate) {
		checkFrozen();
		this.beginDate = beginDate;
	}

	public void setEndDate(DateTime endDate) {
		checkFrozen();
		this.endDate = endDate;
	}

	public void setFreeText(String freeText) {
		checkFrozen();
		this.freeText = freeText;
	}

	public void setUserScheduleStatusMap(Map<String, EventScheduleStatus> userScheduleStatusMap) {
		checkFrozen();
		this.userScheduleStatusMap = userScheduleStatusMap;
	}

	public void setCreatedAt(DateTime createdAt) {
		checkFrozen();
		this.createdAt = createdAt;
	}

	public void setModifiedAt(DateTime modifiedAt) {
		checkFrozen();
		this.modifiedAt = modifiedAt;
	}
	
}
