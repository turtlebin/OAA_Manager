/**
* Created by weiwenjie on 2017-3-23
*/
package gri.engine.process.topology.stats;

import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
/**
 * @Description 
 * Created by weiwenjie on 2017-3-23
 */
public class StatsTopology {

	private static final Logger log = Logger.getLogger(StatsTopology.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		log.info("StatsTopology run");
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("statsSpout", new StatsSpout(), 1);
		builder.setBolt("groupBolt", new GroupBolt(), 4).shuffleGrouping("statsSpout");
		builder.setBolt("calcBolt", new CalcBolt(), 4).fieldsGrouping("groupBolt", new Fields("group"));
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
            cluster.submitTopology("stats", config, builder.createTopology());
        }
	}

}

