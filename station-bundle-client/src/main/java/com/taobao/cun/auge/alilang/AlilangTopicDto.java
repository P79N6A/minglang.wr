package com.taobao.cun.auge.alilang;

import java.io.Serializable;
import java.util.Date;

public class AlilangTopicDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6045721589852686135L;
	
    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer style;

    private Integer type;

    private String icon;

    private String content;

    private String title;

    private String link;

    private Integer orderNum;

    public AlilangTopicDto(){};
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
}
