package org.xuenan.hyx.init;

import com.globalegrow.core.model.ResponseEntity;

@FunctionalInterface
public interface TokenOutService {
	void setToken(ResponseEntity<?> responseEntity);
}
