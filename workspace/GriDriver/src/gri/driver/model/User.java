package gri.driver.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 6056557989254073903L;

	private Integer id;
	private String account;
	private String password;
	private String nickname;

	public User(){
		
	}
	
	public User(String account, String password, String nickname) {
		this.account = account;
		this.password = password;
		this.nickname = nickname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", password=" + password + ", nickname=" + nickname + "]";
	}

}
