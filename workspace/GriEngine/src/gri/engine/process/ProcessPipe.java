/**
* Created by weiwenjie on 2017-3-22
*/
package gri.engine.process;

import java.util.List;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-22
 */
public interface ProcessPipe {
	public void offer(Integer paragraphId, String data);
	
	public List<MessagePack> poll(String processorName ,long timeout);
}
