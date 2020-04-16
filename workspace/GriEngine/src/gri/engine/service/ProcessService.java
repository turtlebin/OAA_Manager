/**
* Created by weiwenjie on 2017-3-24
*/
package gri.engine.service;

import gri.engine.model.dao.ProcessDao;
import gri.driver.model.process.Processor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description 
 * Created by weiwenjie on 2017-3-24
 */
public class ProcessService {
	public void initTopology(String className){
		try {
			Class<?> cls = Class.forName(className);
			Method mainFunc = cls.getMethod("main", String[].class);
			mainFunc.invoke(null, (Object) new String[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initAllTopology(){
		List<Processor> ls = new ProcessDao().listProcessor();
		for(Processor processor : ls) initTopology(processor.getClassName());
	}
}
