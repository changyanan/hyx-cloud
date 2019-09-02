package com.hyx.core.model;

import java.io.Serializable;

import com.hyx.core.utils.JSONUtils;
import com.hyx.core.utils.StringUtils;

public abstract class SysModel implements Serializable {
	public static final long serialVersionUID = 1L;

	public final String toJSONString() {
		return JSONUtils.toJSONString(this);
	}

	public final String toJSONHTML() {
		String model = toJSONString();
		if (StringUtils.isEmpty(model))
			return null;
		return model.replace("'", "&apos;").replace("\"", "&quot;");
	}
	
	@Override
	public String toString() {
		return this.toJSONString();
	}
}
