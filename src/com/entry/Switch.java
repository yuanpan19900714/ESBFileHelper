package com.entry;

import java.util.List;

public class Switch {
	private String channelId;
	private String mode;
	private String expression;
	private String encode;
	private List<Case> caseList;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public List<Case> getCaseList() {
		return caseList;
	}

	public void setCaseList(List<Case> caseList) {
		this.caseList = caseList;
	}

	public Switch(String channelId, String mode, String expression, String encode,
			List<Case> caseList) {
		super();
		this.channelId = channelId;
		this.mode = mode;
		this.expression = expression;
		this.encode = encode;
		this.caseList = caseList;
	}

}
