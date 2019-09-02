package com.hyx.core.init;

import com.hyx.core.model.ResponseEntity;

@FunctionalInterface
public interface TokenOutService {
	void setToken(ResponseEntity<?> responseEntity);
}
