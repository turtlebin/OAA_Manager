package gir.engine.monitor;

public class paragraph3Factory implements iProvider{

	@Override
	public iSender produce() {
		// TODO Auto-generated method stub
		return new paragraph3Sender();
	}
 
}
