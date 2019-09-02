 package demo.redis;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.globalegrow.core.utils.AsyncUtils;
import com.google.common.hash.Hashing;

import io.lettuce.core.RedisCommandExecutionException;

@Component
 public class TestFind implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(TestFind.class);


	@Autowired StringRedisTemplate redisTemplate;
	
	private static final byte[] findStockbySkusAndStockAndOwnerAndTypeLua = (
			  "local bigRet = {};"
			+ "local tag  =  KEYS[1];"
			+ "local skus =  ARGV[1];"
			+ "local regexp= ARGV[2];"
			+ "for sku in string.gmatch(skus, '[^,]+') do "
			+ " local bigKey= tag..sku; "
			+ " if(bigRet[sku] == nil) then "
			+ "		bigRet[sku]= {};"
			+ "	end;"
			+ " if(redis.call('EXISTS', bigKey)==1) then "
			+ "   local hashkeys = redis.call('HKEYS', bigKey);" 
			+ "   for key,hashkey in pairs(hashkeys) do " 
			+ "   	if (string.match(hashkey, regexp)) then "
			+ "       local stock = string.match(hashkey, '(.*):.*:.*');"
			+ "       local owner = string.match(hashkey, '.*:(.*):.*');"
			+ "       local type  = string.match(hashkey, '.*:.*:(.*)');"
			+ "       if(bigRet[sku][stock]==nil) then "
			+ "			 bigRet[sku][stock]= {};"
			+ "		  end;"
			+ "       if(not bigRet[sku][stock][owner]) then "
			+ "			 bigRet[sku][stock][owner] = {};"
			+ "		  end;"
			+ "       bigRet[sku][stock][owner][type] = redis.call('HGET', bigKey, hashkey);"
			+ "   	end;" 
			+ "   end;"
			+ " end;"
			+ "end;"
			+ "return cjson.encode(bigRet);"
		).getBytes();
	public static final int getTagBySku(String sku) {
		return Hashing.consistentHash(sku.hashCode(), 16384);
	}
	@Override
	public void run(String... args) throws Exception {
		
		String sku="sku1";
		ClusterOperations<String, String> cluster = redisTemplate.opsForCluster();
			
//			String json=connection.eval(findStockbySkusAndStockAndOwnerAndTypeLua, ReturnType.STATUS, 1 ,("{"+getTagBySku(sku)+"}").getBytes(),sku.getBytes(), ".*:.*:.*".getBytes() );
//			System.err.println(json);
//			String scriptShas = connection.scriptLoad(findStockbySkusAndStockAndOwnerAndTypeLua);
//			System.err.println(scriptShas);
		AtomicInteger size=new AtomicInteger(0);
			for (int i = 0; i < 50; i++) {
				AsyncUtils.execute(()->{
					redisTemplate.execute((RedisConnection connection)->{
					for (int j = 0; j < 1000; j++) {
						int si=size.incrementAndGet();
//				System.err.println(connection.scriptExists(scriptShas));
						String scriptShas=null;
						if(scriptShas==null) {
							scriptShas = connection.scriptLoad(findStockbySkusAndStockAndOwnerAndTypeLua);
						}
						try {
							String json=connection.eval(findStockbySkusAndStockAndOwnerAndTypeLua, ReturnType.STATUS, 1 ,("{"+si +"}").getBytes(),sku.getBytes(), ".*:.*:.*".getBytes());
//					System.err.println("1-->"+json);
						} catch (RedisSystemException e) {
							if(e.getCause() instanceof RedisCommandExecutionException) {
								String json=connection.evalSha(scriptShas, ReturnType.STATUS, 1 ,("{"+si +"}").getBytes(),sku.getBytes(), ".*:.*:.*".getBytes());
								System.err.println("2-->"+json);
							}
						}
						
						if(si%1000==0) {
							log.info("------->>{}",si);
						}
						
					}
					
				
					return null;
					});
				});
			}
	}

}
