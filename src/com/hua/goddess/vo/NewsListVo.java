package com.hua.goddess.vo;

import java.util.ArrayList;

public class NewsListVo {
	private int errorCode;
    private String errorMessage;
    private ArrayList<NewsVo> list;
    private ArrayList<?> list2;
    private String str;
    
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public ArrayList<NewsVo> getList() {
		return list;
	}
	public void setList(ArrayList<NewsVo> list) {
		this.list = list;
	}
	public ArrayList<?> getList2() {
		return list2;
	}
	public void setList2(ArrayList<?> list2) {
		this.list2 = list2;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
    
    
}
