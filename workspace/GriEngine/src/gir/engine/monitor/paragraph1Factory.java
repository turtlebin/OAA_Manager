package gir.engine.monitor;

public class paragraph1Factory implements iProvider{

	@Override
	public iSender produce() {
		// TODO Auto-generated method stub
		return new paragraph1Sender();
	}

}
