package gri.driver.model.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * @Description 
 * Created by weiwenjie on 2016-8-13
 */

public class StatsConf implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String viewName;
	private Integer graphType;
	private String rowSummary;
	private String colSummary;
	private List<FilterCond> filters;
	private List<GroupBy> groupBys;
	private List<StatsContent> statsContents;
	
	public StatsConf(){
		filters = new ArrayList<FilterCond>();
		groupBys = new ArrayList<GroupBy>();
		statsContents = new ArrayList<StatsContent>();
	}
	
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}	
	public Integer getGraphType() {
		return graphType;
	}
	public void setGraphType(Integer graphType) {
		this.graphType = graphType;
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
	public List<FilterCond> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterCond> filters) {
		this.filters = filters;
	}

	public List<GroupBy> getGroupBys() {
		return groupBys;
	}
	public void setGroupBys(List<GroupBy> groupBys) {
		this.groupBys = groupBys;
	}
	public List<StatsContent> getStatsContents() {
		return statsContents;
	}
	public void setStatsContents(List<StatsContent> statsContents) {
		this.statsContents = statsContents;
	}
	
	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
	
}
