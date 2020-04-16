/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process.topology.hello;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class HelloTopology {

	private static final Logger log = Logger.getLogger(HelloTopology.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//System.out.println("HelloTopology run");
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("helloSpout", new HelloSpout(), 1);
		builder.setBolt("helloBolt", new HelloBolt(), 4).shuffleGrouping("helloSpout");
		Config config = new Config();
		//写了这句会把所有emit输出出来，否则在运行阶段没有日志输出
		//config.setDebug(true);
		if (args != null && args.length > 0) {
            config.setNumWorkers(1);
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        } else {
            // 这里是本地模式下运行的启动代码。
        	//官方的hello world中有这一行
        	//但测试并行度的时候要注释掉
            //config.setMaxTaskParallelism(1);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("hello", config, builder.createTopology());
        }
	}

}

