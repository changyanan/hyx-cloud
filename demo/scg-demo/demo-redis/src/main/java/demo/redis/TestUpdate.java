 package demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;

@Component
 public class TestUpdate implements CommandLineRunner {


	@Autowired StringRedisTemplate redisTemplate;
	

	private static final byte[] hIncrBysLua = (
			  "local tag = KEYS[1];"
			+ "local json = cjson.decode(ARGV[1]);"
					  
			//判断是否扣减库存，扣减库存是否扣得下去
			+ "for sku,sotm in pairs(json) do "
			+ "   for stock,otm in  pairs(sotm) do "
			+ "      for owner,tm in  pairs(otm) do"
			+ "			for type,qty in  pairs(tm) do"
			+ "            if(qty <0) then "
			+ "               local qtyS= redis.call('HGET', tag..sku,stock ..':'..owner..':'..type); "
			+ "               if(qtyS == nil or (not qtyS) ) then"
			+ "					qtyS = 0 ;"
			+ "				  end; "
			+ "               if(( qty+ qtyS)<0) then"
			+ "                  return false;" // 需要扣减的库存减去被扣减的数量，如果剩余库存小于0，则表示不可以扣减库存，直接返回false
			+ "               end;"
			+ "            end;"
			+ "         end;"
			+ "      end;"
			+ "   end;"
			+ "end;"
			  
			+ "for sku,sotm in pairs(json) do "
			+ "   for stock,otm in  pairs(sotm) do "
			+ "      for owner,tm in  pairs(otm) do "
			+ "			for type,qty in  pairs(tm) do "
			+ "             redis.call('HINCRBY', tag..sku, stock ..':'..owner..':'..type , qty); "
			+ "         end;"
			+ "      end;"
			+ "   end;"
			+ "end;"
			
			+ "return true;"
		).getBytes();

	public static final int getTagBySku(String sku) {
		return Hashing.consistentHash(sku.hashCode(), 16384);
	}
	@Override
	public void run(String... args) throws Exception {
		String tag="{11784}";
		String json="{\"sku1\":{\"sc_006\":{\"oc_001\":{\"i\":-145}},\"sc_001\":{\"oc_001\":{\"g\":478}},\"sc_002\":{\"oc_001\":{\"i\":-333}}}}";
		redisTemplate.execute((RedisConnection connection)->{
			
//			boolean ret= connection.eval(hIncrBysLua, ReturnType.BOOLEAN, 1, tag.getBytes(), json.getBytes());
//			System.err.println(ret);
//			String hash = connection.scriptLoad(hIncrBysLua);
//			System.err.println(hash);
			return null;
		});
	}

}
