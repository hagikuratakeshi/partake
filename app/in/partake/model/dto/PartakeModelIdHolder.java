package in.partake.model.dto;

import java.util.UUID;

import org.apache.commons.lang.ObjectUtils;

public abstract class PartakeModelIdHolder {
	public abstract UUID getUUId();
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PartakeModelIdHolder)) { return false; }

		PartakeModelIdHolder lhs = this;
		PartakeModelIdHolder rhs = (PartakeModelIdHolder) obj;

		if (!ObjectUtils.equals(lhs.getUUId(), rhs.getUUId())) { return false; }
		return true;
	}
	
	@Override
	public int hashCode() {
	    int code = 0;
        code = code * 37 + ObjectUtils.hashCode(getUUId());
        return code;
	}
}
