package com.taobao.cun.auge.common;

/**
 * 方便跟前端结合使用
 * 
 * @author chengyu.zhoucy
 *
 */
public class AttachmentVO {
	private String fileType;
	private String fsId;
	private String title;
	private String url;
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFsId() {
		return fsId;
	}
	public void setFsId(String fsId) {
		this.fsId = fsId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
