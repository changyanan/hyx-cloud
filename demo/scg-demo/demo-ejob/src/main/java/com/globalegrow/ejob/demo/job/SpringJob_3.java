package com.globalegrow.ejob.demo.job;

import org.springframework.stereotype.Service;

import com.globalegrow.ejob.core.ScgEJob;
import com.globalegrow.ejob.java.bean.SchedulerParam;
import com.globalegrow.ejob.java.core.annotation.JobDec;
import com.globalegrow.ejob.java.utils.TimeUtils;

@Service
@JobDec(group="SpringGroup",jobName="SpringJob_3",quartz="0/2 * * * * ?")
public class SpringJob_3 extends ScgEJob{

	public void execute(SchedulerParam arg0) {
		System.out.println(this.getClass().getName()+"; at time:"+TimeUtils.getFormatNow());
	}


}
