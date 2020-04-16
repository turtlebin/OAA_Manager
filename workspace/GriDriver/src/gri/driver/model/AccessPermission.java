package gri.driver.model;

import java.io.Serializable;

public class AccessPermission implements Serializable {

	private static final long serialVersionUID = -5339992500593418281L;

	private Integer gridocID;
	private Integer userID;
	private String userAccount;
	private boolean readOnly;

	public AccessPermission(Integer gridocID, Integer userID, String userAccount, boolean readOnly) {
		super();
		this.gridocID = gridocID;
		this.userID = userID;
		this.userAccount = userAccount;
		this.readOnly = readOnly;
	}

	public Integer getGridocID() {
		return gridocID;
	}

	public void setGridocID(Integer gridocID) {
		this.gridocID = gridocID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public String toString() {
		return "AccessPermission [gridocID=" + gridocID + ", userID=" + userID + ", userAccount=" + userAccount
				+ ", readOnly=" + readOnly + "]";
	}

}
