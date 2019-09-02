package demo.guava;

import java.math.BigInteger;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.PrimitiveSink;

public class TestBloomFilter {

	public static BloomFilter<BigInteger> bloomFilter = BloomFilter.create((BigInteger from, PrimitiveSink into) ->  {
		into.putBytes(from.toByteArray());
	}, 1000);

	public static void main(String[] args) {
		bloomFilter.put(new BigInteger("2222"));

		boolean mayBeContained = bloomFilter.test(new BigInteger("22221"));
		
		System.err.println(mayBeContained);
	}

}
