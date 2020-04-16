/**
* Created by weiwenjie on 2017-4-7
*/
package gri.engine.process.view;

import net.sf.json.JSONArray;
import gri.engine.service.RedisContainerService;

/**
 * @Description 
 * Created by weiwenjie on 2017-4-7
 */
public class AesView implements VirtualView{

	@Override
	public Object result(String containerName, String viewName) {
		Object result = null;
		if(viewName.equals("default")){
			result = defaultView(containerName);
		}
		else{
			result = defaultView(containerName);
		}
			
		return result;
	}
	
	private Object defaultView(String containerName){
		RedisContainerService redisService = new RedisContainerService(containerName);
		String result =JSONArray.fromObject(redisService.lget("result")).toString();
		return result;
	}

}
