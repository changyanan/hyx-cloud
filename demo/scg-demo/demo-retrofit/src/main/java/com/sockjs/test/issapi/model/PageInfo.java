
package com.sockjs.test.issapi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sockjs.test.utils.ListUtils;



public class PageInfo<T>  {
	@FunctionalInterface
	public static interface PageConvert<T,M>{
		M convert(T t);
	}
	private int pageNo = 1;

	private int pageSize = 10;

	private long total = 0;

	private List<T> list = Arrays.asList();
	
	public Integer getTotalPage() {
		double totalPage = total / (pageSize * 1.00);
		return (int) Math.ceil(totalPage);
	}
	
	public PageInfo() {
		super();
	}
 
	public PageInfo(Integer pageNo, Integer pageSize, long total, List<T> list) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
		this.list = list;
	}

	public <M> PageInfo<M> convert(PageConvert<T,M> convert) {
		 List<M> listM = ListUtils.n(this.list).list(convert::convert).to();
		 return new PageInfo<>(pageNo, pageSize, total, listM);
	}
	
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getList() {
		if(list == null){
			return new ArrayList<T>();
		}
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
