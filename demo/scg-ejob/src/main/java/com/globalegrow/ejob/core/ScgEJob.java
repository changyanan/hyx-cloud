package com.globalegrow.ejob.core;

import com.globalegrow.ejob.java.bean.SchedulerParam;
import com.globalegrow.ejob.java.job.EJob;

public abstract class ScgEJob implements EJob {

	@Override
	public void beforeExecute(SchedulerParam arg0) {

	}

	@Override
	public abstract void execute(SchedulerParam arg0) ;

	@Override
	public void executeFail(SchedulerParam arg0) {

	}

	@Override
	public void executeSuccess(SchedulerParam arg0) {

	}

}
