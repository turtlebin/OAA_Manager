package gri.driver.manager;

import java.util.ArrayList;
import java.util.List;

import gri.driver.model.Connection;
import gri.driver.model.User;

public class UserManager {
	private Connection connecton;

	public UserManager(Connection connecton) {
		super();
		this.connecton = connecton;
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUser() {
		Object result = connecton.execute_sync("get all user", "");
		if (result != null)
			return (ArrayList<User>) result;
		else
			return new ArrayList<User>();
	}

	public boolean updateUser(User user) {
		Object result = connecton.execute_sync("update user", user);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	public User addUser(User user) {
		Object result = connecton.execute_sync("add user", user);
		if (result != null)
			return (User) result;
		else
			return null;
	}

	public boolean deleteUser(User user) {
		Object result = connecton.execute_sync("delete user", user);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

}
