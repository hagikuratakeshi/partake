package in.partake.controller.api.schedule;

import in.partake.base.PartakeException;
import in.partake.controller.api.AbstractPartakeAPI;
import in.partake.controller.base.permission.PrivateEventShowPermission;
import in.partake.model.EventEx;
import in.partake.model.IPartakeDAOs;
import in.partake.model.UserEx;
import in.partake.model.access.DBAccess;
import in.partake.model.dao.DAOException;
import in.partake.model.dao.PartakeConnection;
import in.partake.model.dao.access.IEventScheduleCandidateAccess;
import in.partake.model.daofacade.EventDAOFacade;
import in.partake.model.dto.EventScheduleCandidate;
import in.partake.resource.UserErrorCode;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.mvc.Result;

public class GetAPI extends AbstractPartakeAPI {

	public static Result get() throws DAOException, PartakeException {
		return new GetAPI().execute();
	}

	@Override
	protected Result doExecute() throws DAOException, PartakeException {
		String eventId = getValidEventIdParameter();
		UserEx user = getLoginUser();
		String passcode = getParameter("passcode");
		if (passcode == null)
			passcode = session().get("event:" + eventId);

		List<EventScheduleCandidate> candidates = new GetTransaction(user, eventId, passcode).execute();
		JSONObject obj = new JSONObject();
		JSONArray objArray = new JSONArray();
		for (int i=0; i<candidates.size(); i++) {
			objArray.add(candidates.get(i));
		}
		obj.put("candidates", objArray);
		return renderOK(obj);
	}
}

class GetTransaction extends DBAccess<List<EventScheduleCandidate>> {
	private String eventId;
	private UserEx user;
	private String passcode;

	public GetTransaction(UserEx user, String eventId, String passcode) {
		this.user = user;
		this.eventId = eventId;
		this.passcode = passcode;
	}

	@Override
	protected List<EventScheduleCandidate> doExecute(PartakeConnection con, IPartakeDAOs daos) throws DAOException, PartakeException {
		IEventScheduleCandidateAccess access = daos.getEventScheduleCandidateAccess();
		List<EventScheduleCandidate> candidates = access.findByEventId(con, eventId);
		
		// TODO 処理まとめる
        EventEx event = EventDAOFacade.getEventEx(con, daos, eventId);
		if (event == null)
            throw new PartakeException(UserErrorCode.INVALID_EVENT_ID);

        if (!StringUtils.isBlank(event.getPasscode())) {
            // owner および manager は見ることが出来る。
            if (user != null && PrivateEventShowPermission.check(event, user)) {
                // OK. You have the right to show this event.
            } else if (StringUtils.equals(event.getPasscode(), passcode)) {
                // OK. The same passcode.
            } else {
                // public でなければ、passcode を入れなければ見ることが出来ない
                throw new PartakeException(UserErrorCode.FORBIDDEN_EVENT_SHOW);
            }
        }
        
		return candidates;
	}
}
