package gri.engine.util;

import csAsc.EIB.Engine.Authentication;
import csAsc.EIB.Engine.EITPLocation;
import csAsc.EIB.Engine.Engine;
import csAsc.EIB.Engine.Order;

public class EITPSender {

	// 发送EITP请求
	public static Object sendRequest(String paragraphHost, String paragraphUser, String paragraphPasswrod,
			String paragraphEngineName, String operate, Object resquestData) {
		Order response = new Order(); // 响应结果
		Engine eitp_engine = new Engine();
		eitp_engine.start();

		// 发送请求
		int ret = eitp_engine.once(new EITPLocation(paragraphHost),
				new Authentication(Authentication.AT_BASIC, new String[] { paragraphUser, paragraphPasswrod }),
				new Order(paragraphEngineName, paragraphUser, operate, resquestData), response);

		eitp_engine.stop();

		if (ret == 0) {
			return response.data;
		} else {
			return null;
		}
	}

}
