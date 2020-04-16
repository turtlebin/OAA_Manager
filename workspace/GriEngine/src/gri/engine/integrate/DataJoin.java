package gri.engine.integrate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gri.engine.dest.Pair;

public class DataJoin implements Serializable {
	private String firstDataSource;
	private String secondDataSource;
	private List<Pair<String, String>> list=new ArrayList<Pair<String,String>>();
	private Pair<String,String> preparedJoinPair;

	public Pair<String, String> getPreparedJoinPair() {
		return preparedJoinPair;
	}

	public void setPreparedJoinPair(Pair<String, String> preparedJoinPair) {
		this.preparedJoinPair = preparedJoinPair;
	}

	public String getFirstDataSource() {
		return firstDataSource;
	}

	public void setFirstDataSource(String firstDataSource) {
		this.firstDataSource = firstDataSource;
	}

	public String getSecondDataSource() {
		return secondDataSource;
	}

	public void setSecondDataSource(String secondDataSource) {
		this.secondDataSource = secondDataSource;
	}

	public List<Pair<String, String>> getList() {
		return list;
	}

	public void setList(List<Pair<String, String>> list) {
		this.list = list;
	}

	public DataJoin(String firstDataSource, String secondDataSource, List<Pair<String, String>> list) {
		this.firstDataSource = firstDataSource;
		this.secondDataSource = secondDataSource;
		this.list = list;
		this.preparedJoinPair=new Pair(this.firstDataSource,this.secondDataSource);
	}
	
	public boolean sourceEquals(Pair<String,String> pair)
	{
		if((this.preparedJoinPair.getFirst().equals(pair.getFirst())&&(this.preparedJoinPair.getSecond().equals(pair.getSecond())))
				||(this.preparedJoinPair.getFirst().equals(pair.getSecond())&&(this.preparedJoinPair.getSecond().equals(pair.getFirst()))))
		{
			return true;
		}
		else
		{
		return false;
		}
	}
	
	public void addJoinInfo(Pair<String,String> pair)
	{
		this.list.add(pair);
	}
}
