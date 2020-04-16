package gri.engine.model;

import java.util.EventObject;

public class DataChangeEvent extends EventObject {

	private static final long serialVersionUID = -4266189775030997270L;
	private Object source;

	public DataChangeEvent(Object source) {
		super(source);
		this.source = source;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

}
