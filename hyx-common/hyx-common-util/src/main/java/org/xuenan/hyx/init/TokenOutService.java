package org.xuenan.hyx.init;


import org.xuenan.hyx.model.ResponseEntity;

@FunctionalInterface
public interface TokenOutService {
	void setToken(ResponseEntity<?> responseEntity);
}
