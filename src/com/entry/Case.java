package com.entry;

public class Case {
	private String switchKey;
	private String value;
	private String isThrough;
	private String isConvert;
	private String text;

	public String getSwitchKey() {
		return switchKey;
	}

	public void setSwitchKey(String switchKey) {
		this.switchKey = switchKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIsThrough() {
		return isThrough;
	}

	public void setIsThrough(String isThrough) {
		this.isThrough = isThrough;
	}

	public String getIsConvert() {
		return isConvert;
	}

	public void setIsConvert(String isConvert) {
		this.isConvert = isConvert;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Case(String switchKey, String value, String isThrough, String isConvert, String text) {
		super();
		this.switchKey = switchKey;
		this.value = value;
		this.isThrough = isThrough;
		this.isConvert = isConvert;
		this.text = text;
	}

}
