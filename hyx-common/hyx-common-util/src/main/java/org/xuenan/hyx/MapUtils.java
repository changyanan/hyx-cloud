package org.xuenan.hyx;

import org.xuenan.hyx.model.SysModel;

import java.util.*;

/**
 * 
 * @author lenovo 方法介绍 n : 创建 a : 添加 ck : 判断key是否存在 cv : 判断是否存在 r : 删除 c : 清理 s
 *         : 大小 g : 取出 to : 转化为原型 each : 遍历 filter : 过滤 array : 转化为列表 map :
 *         重写map的值对象 mapKey : 重写key对象
 * @param <K>
 * @param <V>
 */
public class MapUtils<K, V>extends SysModel {

	private static final long serialVersionUID = 1L;

	@FunctionalInterface
	public static interface EachMap<K, V> {
		void accept(final K k, final V v);
	}

	@FunctionalInterface
	public static interface FilterMap<K, V> {
		boolean test(final K k, final V v);
	}

	@FunctionalInterface
	public static interface ArrayMap<K, V, T> {
		T accept(final K k, final V v);
	}

	@FunctionalInterface
	public static interface ArraysMap<K, V, T> {
		Collection<T> accept(final K k, final V v);
	}

	@FunctionalInterface
	public static interface MapNewMap<K, V, T> {
		T accept(final K k, final V v);
	}

	@FunctionalInterface
	public static interface UniqueMap<K, V, N> {
		N accept(final K k, final V v);
	}
	
	private final Map<K, V> map;

	private MapUtils() {
		map = new HashMap<>();
	}

	private MapUtils(final Map<K, V> map) {
		this();
		if (map != null && map.size() > 0)
			this.map.putAll(map);
	}

	/**
	 * 新建一个MapUtils对象 示例: MapUtils<String,Object> mu =
	 * MapUtils.<String,Object>n();
	 * 
	 * @return
	 */
	public final static <K, V> MapUtils<K, V> n() {
		return new MapUtils<K, V>();
	}
	
	/**
	 * 按map新建一个
	 * @param map
	 * @return
	 */
	public final static <K, V> MapUtils<K, V> n(final Map<K, V> map) {
		return new MapUtils<K, V>(map);
	}

	/**
	 * 添加一个键值对
	 * @param key
	 * @param value
	 * @return
	 */
	public final MapUtils<K, V> a(final K key, final V value) {
		map.put(key, value);
		return this;
	}

	/**
	 * 获取一个值
	 * @param key
	 * @return
	 */
	public final V g(final K key) {
		return map.get(key);
	}
	/**
	 * 获取一组值
	 * @param keys
	 * @return
	 */
	public final ListUtils<V> g(@SuppressWarnings("unchecked") final K... keys) {
		ListUtils<V> lu = ListUtils.n();
		for (K k : keys) {
			V v = this.g(k);
			lu.a(v);
		}
		return lu;
	}
	/**
	 * 删除一个值
	 * @param key
	 * @return
	 */
	public final MapUtils<K, V> r(final K key) {
		map.remove(key);
		return this;
	}
	/**
	 * 删除一组值
	 * @param keys
	 * @return
	 */
	public final MapUtils<K, V> r(@SuppressWarnings("unchecked") final K... keys) {
		for (K key : keys) {
			map.remove(key);
		}
		return this;
	}
	
	/**
	 * 清空mu
	 * @return
	 */
	public final MapUtils<K, V> c() {
		map.clear();
		return this;
	}
	/**
	 * 转化为原型map
	 * @return
	 */
	public final Map<K, V> to() {
		return map;
	}
	/**
	 * 判断一个key是否存在
	 * @param key
	 * @return
	 */
	public final boolean ck(final K key) {
		return map.containsKey(key);
	}
	/**
	 * 判断一个值是否存在
	 * @param value
	 * @return
	 */
	public final boolean cv(final V value) {
		return map.containsValue(value);
	}
	/**
	 * 获取大小
	 * @return
	 */
	public final int s() {
		return map.size();
	}
	/**
	 * 遍历map
	 * @param eachMap
	 * @return
	 */
	public final MapUtils<K, V> each(final EachMap<K, V> eachMap) {
		this.map.forEach((k, v) -> {
			eachMap.accept(k, v);
		});
		return this;
	}
	/**
	 * 按规则过滤，删除规则返回false得
	 * @param filter
	 * @return
	 */
	public final MapUtils<K, V> filter(final FilterMap<K, V> filter) {
		MapUtils<K, V> mu = MapUtils.n();
		this.each((k, v) -> {
			if (filter.test(k, v)) {
				mu.a(k, v);
			}
		});
		return mu;
	}
	/**
	 * 转化值
	 * @param mnm
	 * @return
	 */
	public final <N> MapUtils<K, N> map(final MapNewMap<K, V, N> mnm) {
		MapUtils<K, N> mu = MapUtils.n();
		this.each((k, v) -> {
			N val = mnm.accept(k, v);
			if (val != null)
				mu.a(k, val);
		});
		return mu;
	}
	/**
	 * 转化key
	 * @param mnm
	 * @return
	 */
	public final <NK> MapUtils<NK, V> mapKey(final MapNewMap<K, V, NK> mnm) {
		MapUtils<NK, V> mu = MapUtils.n();
		this.each((k, v) -> {
			NK nk = mnm.accept(k, v);
			if (nk != null)
				mu.a(nk, v);
		});
		return mu;
	}
	/**
	 * 将map按规则转化为数字
	 * @param am
	 * @return
	 */
	public final <T> ListUtils<T> array(final ArrayMap<K, V, T> am) {
		ListUtils<T> lu = ListUtils.n();
		this.each((k, v) -> {
			T t = am.accept(k, v);
			if (t != null)
				lu.a(t);
		});
		return lu;
	}

	/**
	 * 将map按规则转化为数字
	 * @param am
	 * @return
	 */
	public final <T> ListUtils<T> arrays(final ArraysMap<K, V, T> am) {
		ListUtils<T> lu = ListUtils.n();
		this.each((k, v) -> {
			lu.a(am.accept(k, v));
		});
		return lu;
	}
	
	/**
	 * 按规则去重
	 * @param uniqueMap
	 * @return
	 */
	public final <N> MapUtils<K, V> unique(UniqueMap<K,V,N> uniqueMap) {
		List<N> l=new LinkedList<>();
		return this.filter((k,v)->{
			N n = uniqueMap.accept(k, v);
			if(l.contains(n))
				return false;
			l.add(n);
			return true;
		});
	} 
	
	public final static boolean isNotEmpty(final Map<?, ?>... args) {
		for (Map<?, ?> map : args) {
			if (map == null || map.isEmpty())
				return false;
		}
		return true;
	}

	public final static boolean isEmpty(final Map<?, ?>... args) {
		return !isNotEmpty(args);
	}
}