package com.globalegrow.ejob.demo.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.globalegrow.ejob.core.ScgEJob;
import com.globalegrow.ejob.demo.service.EjobService;
import com.globalegrow.ejob.java.bean.SchedulerParam;
import com.globalegrow.ejob.java.core.annotation.JobDec;
import com.globalegrow.ejob.java.utils.TimeUtils;

@Service
@JobDec(group="SpringGroup",jobName="SpringJob_1",quartz="0/1 * * * * ?")
public class SpringJob_1 extends ScgEJob {

	@Resource
	private EjobService ejobService;
	
	public void execute(SchedulerParam arg0) {
		System.out.println(ejobService.sayEjob()+"; "+this.getClass().getName()+"--> scheduler at time:"+TimeUtils.getFormatNow());
	}


}
