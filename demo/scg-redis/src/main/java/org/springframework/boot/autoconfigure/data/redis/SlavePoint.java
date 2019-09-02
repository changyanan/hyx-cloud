package org.springframework.boot.autoconfigure.data.redis;


public class SlavePoint implements AutoCloseable {
	
	public SlavePoint() {
		MasterSlave.slave();
	}

	@Override
	public void close() throws Exception {
		MasterSlave.reset();
	}

}
