package gri.driver.model.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 
 * Created by weiwenjie on 2016-8-13
 */
public class StatsResult implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer type;	
	private String title;
	private String rowSummary;
	private String colSummary;
	private List<String> rowName;
	private List<String> colName;
	//注意，这个data数组是按列存储的，既第一维是列，第二位是行
	private List<List<Double>> data;
	
	public StatsResult() {}
	
	public StatsResult(StatsResult statsResult) {
		super();
		this.type = statsResult.type;
		this.title = statsResult.title;
		this.rowSummary = statsResult.rowSummary;
		this.colSummary = statsResult.colSummary;
		this.rowName = new ArrayList<String>();
		for(int i = 0 ;i<statsResult.rowName.size();i++) this.rowName.add(statsResult.rowName.get(i));
		this.colName = new ArrayList<String>();
		for(int i = 0 ;i<statsResult.colName.size();i++) this.colName.add(statsResult.colName.get(i));
		
		this.data = new ArrayList<List<Double>>();
		for(int i = 0 ;i<statsResult.data.size();i++){
			List<Double> temp=new ArrayList<Double>();
			for(int j = 0 ;j<statsResult.data.get(i).size();j++){
				temp.add(statsResult.data.get(i).get(j));
			}
			this.data.add(temp);
		}
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getRowSummary() {
		return rowSummary;
	}
	public void setRowSummary(String rowSummary) {
		this.rowSummary = rowSummary;
	}
	public String getColSummary() {
		return colSummary;
	}
	public void setColSummary(String colSummary) {
		this.colSummary = colSummary;
	}
	public List<String> getRowName() {
		return rowName;
	}
	public void setRowName(List<String> rowName) {
		this.rowName = rowName;
	}
	public List<String> getColName() {
		return colName;
	}
	public void setColName(List<String> colName) {
		this.colName = colName;
	}
	public List<List<Double>> getData() {
		return data;
	}
	public void setData(List<List<Double>> data) {
		this.data = data;
	}
	
	
}
