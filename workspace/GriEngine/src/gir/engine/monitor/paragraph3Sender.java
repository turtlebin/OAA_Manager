package gir.engine.monitor;

import java.io.IOException;

public class paragraph3Sender implements iSender{

	@Override
	public void sendMoniData(aMessage message) {
		// TODO Auto-generated method stub
	  try {
		OKHttpUtil.httpPost(message.getUrl(), message.getState());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

}
