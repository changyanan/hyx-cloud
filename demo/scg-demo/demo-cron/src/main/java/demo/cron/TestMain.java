package demo.cron;

import java.util.Date;

import org.springframework.scheduling.support.CronSequenceGenerator;

import com.globalegrow.core.utils.DateUtils;

public class TestMain {

	public static void main(String[] args) {
		CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator("0 0 8 * * *");
		Date date = new Date();
		for (int i = 0; i < 100; i++) {
			date =cronSequenceGenerator.next(date);
			System.err.println(DateUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));
		}
	}

}
