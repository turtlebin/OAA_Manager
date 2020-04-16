/**
* Created by weiwenjie on 2017-1-2
*/
package gri.driver.model.process;

import java.io.Serializable;

import net.sf.json.JSONObject;

/**
 * @Description 
 * Created by weiwenjie on 2017-1-2
 */
public class StatsConfData2 implements Serializable{
	private Integer paragraphID;
	private StatsConf statsConf;

	private static final long serialVersionUID = 1L;
	
	public Integer getParagraphID() {
		return paragraphID;
	}
	public void setParagraphID(Integer paragraphID) {
		this.paragraphID = paragraphID;
	}
	public StatsConf getStatsConf() {
		return statsConf;
	}
	public void setStatsConf(StatsConf statsConf) {
		this.statsConf = statsConf;
	}
	
	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
}
