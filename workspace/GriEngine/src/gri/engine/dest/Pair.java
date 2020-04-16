package gri.engine.dest;

import gri.engine.integrate.FieldPair;

public class Pair<E extends String,F extends String> extends FieldPair<String,String>{
	private String first;
	private String second;
	public Pair() {
		super();
	}
	public Pair(String first,String second)
	{
		this.first=first;
		this.second=second;
	}
	public String getFirst()
	{
		return first;
	}
	public String getSecond()
	{
		return second;
	}

//	@SuppressWarnings("unchecked")
//	public void setFirst(Object first) 
//	{
//		this.first=(E)first;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void setSecond(Object second) 
//	{
//		this.second=(F)second;
//	}
	
	public void setSecond(String second) {
		this.second = second;
	}
	
	public void setFirst(String first)
	{
		this.first=first;
	}
}

//public class Pair<E extends String,F extends String> extends FieldPair<String,String>  {
//	private E first;
//	private F second;
//	public Pair() {
//		super();
//	}
//	public Pair(E first,F second)
//	{
//		this.first=first;
//		this.second=second;
//	}
//	public E getFirst()
//	{
//		return first;
//	}
//	public F getSecond()
//	{
//		return second;
//	}
//
//	@SuppressWarnings("unchecked")
//	public void setFirst(Object first) 
//	{
//		this.first=(E)first;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public void setSecond(Object second) 
//	{
//		this.second=(F)second;
//	}
//
//	public void setSecond(E second) {//由于泛型擦除的存在，导致此处报错
//		this.second = second;
//	}
//	
//	public void setFirst(F first)
//	{
//		this.first=first;
//	}
//}

