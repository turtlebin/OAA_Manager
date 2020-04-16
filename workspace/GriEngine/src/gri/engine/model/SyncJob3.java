package gri.engine.model;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.engine.core.DataSync2;
import gri.engine.core.DataSync3;

public class SyncJob3 implements Job {
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Integer paragraphID = (Integer) arg0.getJobDetail().getJobDataMap().get("ParagraphID");
		LOGGER.info("触发数据同步任务");
		new DataSync3(paragraphID).run();
	}
}
