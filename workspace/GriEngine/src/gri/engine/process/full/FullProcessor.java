/**
* Created by weiwenjie on 2017-5-22
*/
package gri.engine.process.full;

import gri.driver.model.process.View;

import java.util.List;

/**
 * @Description 
 * Created by weiwenjie on 2017-5-22
 */
public interface FullProcessor {
	public void process(String data, String viewName, String config);
	public String getResult(String viewName);
	public List<View> getViews(String config);
}
