/**
* Created by weiwenjie on 2017-3-24
*/
package gri.engine.service;

import java.util.List;
import java.util.Set;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-24
 */
public interface ViewService {
	public boolean hset(String viewName, String entry, String data);
	public String hget(String viewName, String entry);
	public boolean hexists(String viewName, String entry);
	public void hdel(String viewName, String...entries);
	public long hlen(String viewName);
	public Set<String> hkeys(String viewName);
	public List<String> hvalues(String viewName);
	public List<String> lget(String viewName);
	public long llen(String viewName);
	public void lpush(String viewName,String data);
	public String lpop(String viewName);
}
