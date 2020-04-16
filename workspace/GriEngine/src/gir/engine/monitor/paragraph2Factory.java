package gir.engine.monitor;

public class paragraph2Factory implements iProvider{

	@Override
	public iSender produce() {
		// TODO Auto-generated method stub
		return new paragraph2Sender();
	}
 
}
