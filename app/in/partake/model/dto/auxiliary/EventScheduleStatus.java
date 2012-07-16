package in.partake.model.dto.auxiliary;

public enum EventScheduleStatus {
	CAN_ATTEND("attend"), 
	MAYBE_ATTEND("maybe"), 
	NOT_ATTEND("cant");
	
	private String readable;

	EventScheduleStatus(String readable) {
		this.readable = readable;
	}
	
	public String getValue() {
		return readable;
	}
}
