package com.qyy.jyshop.pojo;

/**
 * 封装返回数据
 * 
 */
public class AjaxResult {

	private int retcode = 0;
	private String retmsg = "操作成功";
	private Object data;
	
	public AjaxResult(int retcode, String retmsg, Object data){
		this.retcode = retcode;
		this.retmsg = retmsg;
		this.data = data;
	}
	
	public AjaxResult(int retcode, String retmsg){
		this.retcode = retcode;
		this.retmsg = retmsg;
	}
	
	public AjaxResult(Object data){
		this.retmsg = "操作成功";
		this.data = data;
	}
	
	public AjaxResult(String retmsg){
		this.retcode = 1;
		this.retmsg = retmsg;
	}
	
	public AjaxResult(String retmsg,Object data){
		this.retcode = 1;
		this.retmsg = retmsg;
		this.data=data;
	}
	
	public AjaxResult(){
		
	}

	public int getRetcode() {
		return retcode;
	}
	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "AjaxResult [retcode=" + retcode + ", retmsg=" + retmsg + ", data=" + data + "]";
	}
	
}
