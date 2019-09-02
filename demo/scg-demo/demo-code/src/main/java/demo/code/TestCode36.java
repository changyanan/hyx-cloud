package demo.code;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.globalegrow.core.code.Sequence36;

public class TestCode36 {
	final static Map<String, String> map = new HashMap<>();

	public static void main(String[] args) throws InterruptedException {
		int num = 50;
		CountDownLatch latch = new CountDownLatch(num);
		for (int i = 0; i < num; i++) {
			new Thread() {
				public void run() {
					for (int j = 0; j < 100000; j++) {
						String code = Sequence36.generate().toString();
						if (map.containsKey(code)) {
							String mcode = map.get(code);
							if (!code.equals(mcode))
								System.err.println(code + ":: " + mcode + ":: " + code.equals(mcode));
						} else {
							map.put(code, code);
						}
					}
					latch.countDown();
				};
			}.start();
		}
		latch.await();
	}
}
