
package com.hyx.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyx.core.utils.ListUtils;


public class PageInfo<T> extends SysModel {
	private static final long serialVersionUID = 1L;
	private static final int num =6;
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
	
	/**
	 * 默认展示的分页按钮页数为5个
	 * @return
	 */
	@JsonIgnore
	public List<Integer> getPageNos(){
		return getPageNos(num);
	}
	
	@JsonIgnore
	public List<Integer> getPageNos(int num) {
		int totalPage=getTotalPage();
		if(totalPage<pageNo){
			return new ArrayList<>(0);
		}else if(num>=totalPage){
			List<Integer> nums=new ArrayList<>(totalPage);
			for (int i = 0; i < totalPage; i++) {
				nums.add(i+1);
			}
			return nums;
		}else if(pageNo<=(num/2)){
			List<Integer> nums=new ArrayList<>(num);
			for (int i = 0; i < num; i++) {
				nums.add(i+1);
			}
			return nums;
		}else if(totalPage<(pageNo+num/2)){
			List<Integer> nums=new ArrayList<>(num);
			for (int i =  totalPage-num; i <  totalPage  ; i++) {
				nums.add(i+1);
			}
			return nums;
		}else{
			List<Integer> nums=new ArrayList<>(num);
			for (int i = -num/2; i < num/2; i++) {
				nums.add(pageNo+i+1);
			}
			return nums;
		}
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
