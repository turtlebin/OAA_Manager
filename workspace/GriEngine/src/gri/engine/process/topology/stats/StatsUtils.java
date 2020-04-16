/**
* Created by weiwenjie on 2017-4-14
*/
package gri.engine.process.topology.stats;

import gri.driver.model.process.FilterCond;
import gri.driver.model.process.GroupBy;
import gri.driver.model.process.StatsConf;
import gri.driver.model.process.StatsContent;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * @Description 
 * Created by weiwenjie on 2017-4-14
 */
public class StatsUtils {
	public static StatsConf jsonToStatsConf(String json){
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("filters", FilterCond.class);
		classMap.put("groupBys", GroupBy.class);
		classMap.put("statsContents", StatsContent.class);
		
		StatsConf conf = (StatsConf) JSONObject.toBean(JSONObject.fromObject(json),StatsConf.class,classMap);
		return conf;
	}
}
