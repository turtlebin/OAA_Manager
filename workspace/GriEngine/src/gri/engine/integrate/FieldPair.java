package gri.engine.integrate;

import java.io.Serializable;

///���Զԣ�����������ʱ���������
public class FieldPair<E extends String, F extends String> implements Serializable{
	private E first;
	private F second;

	public FieldPair() {

	}

	public E getFirst() {
		return first;
	}

	public void setFirst(E first) {
		this.first = first;
	}

	public F getSecond() {
		return second;
	}

	public void setSecond(F second) {
		this.second = second;
	}

}
