package gir.engine.monitor;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import gri.engine.integrate.SyncHelper;
import gri.engine.integrate.SyncHelper2;
import gri.engine.integrate.SyncHelper3;
import gri.engine.integrate.sync;

public class TimerHelper {

	private RowGetter getter = null;
	private String messageType = null;
	private aMessage message = null;
	private sync sync = null;
	private Timer timer = new Timer("timer");

	public Timer getTimer() {
		return timer;
	}

	public TimerHelper(String messageType, aMessage message, RowGetter getter, sync sync) {
		this.getter = getter;
		this.messageType = messageType;
		this.sync = sync;
		this.message = message;
		// TODO Auto-generated constructor stub
	}

	public TimerHelper(String messageType, aMessage message, sync sync) {
		this.messageType = messageType;
		this.sync = sync;
		this.message = message;
		// TODO Auto-generated constructor stub
	}

	public void send() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				message.setStatus("running");
				try {
					if (getter != null) {
						if (getter.rowCount == -1) {
							message.setProcess(0);
							SendHelper.send(messageType, message);
						} else {
							if (messageType.equalsIgnoreCase("Message2")) {
								message.setProcess(((SyncHelper3) sync).process * 100 / getter.rowCount);
								SendHelper.send(messageType, message);
							} else if (messageType.equals("Message1")) {
								message.setProcess(((SyncHelper) sync).getProcess() * 100 / getter.rowCount);
								SendHelper.send(messageType, message);
							}
						}
					} else {
						message.setProcess(((SyncHelper2)sync).getProcess());
						((Message3)message).setStatus(((SyncHelper2)sync).getStage());
						SendHelper.send(messageType, message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//System.out.println(Thread.currentThread().getName() + " run ");
			}
		}, 0, 5000);

	}
}
