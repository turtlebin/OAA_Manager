package gri.manager.util;

import java.util.ArrayList;
import java.util.List;

import gri.driver.manager.GriDocManager;
import gri.driver.model.Connection;
import gri.driver.model.GriElement;
import gri.driver.model.User;

public class RunTimeData {

	public static List<Connection> globalView;
	public static List<User> userView;
	public static List<GriElement> queryView;

	public static GriDocManager workingGriDocManager;

	static {
		RunTimeData.globalView = new ArrayList<Connection>();
		RunTimeData.userView = new ArrayList<User>();
		RunTimeData.queryView = new ArrayList<GriElement>();
	}

	private RunTimeData() {
	}

	public static void readHistory() {

	}

}
