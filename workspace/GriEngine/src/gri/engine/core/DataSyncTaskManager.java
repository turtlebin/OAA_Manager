package gri.engine.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.CronScheduleBuilder;

import gri.driver.util.DriverConstant;
import gri.engine.model.SyncJob;
import gri.engine.model.SyncJob2;
import gri.engine.model.SyncJob3;
import gri.engine.util.DBHelper;

public class DataSyncTaskManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncTaskManager.class);

	private Scheduler scheduler;

	// Test
	public static void main(String[] args) {

		DataSyncTaskManager manager = new DataSyncTaskManager();
		manager.init();
		manager.addTask(16);
		manager.shutdown();
	}

	public void init() {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void addAllTask() {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		String sql = "select * from paragraph";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql);
		try {
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String syncTimeType = rs.getString("sync_time_type");
				if (syncTimeType.equals(DriverConstant.SyncTimeType_1)) {
					String[] syncWarmInfos = rs.getString("warm_sync_detail").split("#")[1].split("@");
					for (String syncWarmInfo : syncWarmInfos) {
						if (syncWarmInfo.equals(""))
							continue;
						JobDetail job = JobBuilder.newJob(SyncJob.class).withIdentity(
								String.valueOf(id) + "." + UUID.randomUUID().toString(), "paragraph data sync").build();
						job.getJobDataMap().put("ParagraphID", id);
						try {
							CronTrigger trigger = TriggerBuilder.newTrigger()
									.withIdentity(String.valueOf(id) + "." + UUID.randomUUID().toString(),
											"paragraph data sync")
									.withSchedule(CronScheduleBuilder.cronSchedule(syncWarmInfo)).build();
							this.scheduler.scheduleJob(job, trigger);
							LOGGER.info("成功添加定时任务");
							LOGGER.info("任务详情：[Paragraph ID:{}, time:{}]", id, syncWarmInfo);
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
	}
	
	public void addAllTask2() {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		String sql = "select * from paragraph";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql);
		try {
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String syncTimeType = rs.getString("sync_time_type");
				boolean isParagraph3=rs.getBoolean("isParagraph3");
				if(!isParagraph3)
				{
					continue;
				}
				if (syncTimeType.equals(DriverConstant.SyncTimeType_1)) {
					
					String[] syncWarmInfos = rs.getString("warm_sync_detail").split("#")[1].split("@");
					for (String syncWarmInfo : syncWarmInfos) {
						if (syncWarmInfo.equals(""))
							continue;
						JobDetail job = JobBuilder.newJob(SyncJob2.class).withIdentity(
								String.valueOf(id) + "." + UUID.randomUUID().toString(), "paragraph data sync").build();
						job.getJobDataMap().put("ParagraphID", id);
						try {
							CronTrigger trigger = TriggerBuilder.newTrigger()
									.withIdentity(String.valueOf(id) + "." + UUID.randomUUID().toString(),
											"paragraph data sync")
									.withSchedule(CronScheduleBuilder.cronSchedule(syncWarmInfo)).build();
							this.scheduler.scheduleJob(job, trigger);
							LOGGER.info("成功添加定时任务");
							LOGGER.info("任务详情：[Paragraph ID:{}, time:{}]", id, syncWarmInfo);
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
	}

	public void addAllTask3() {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		String sql = "select * from paragraph";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql);
		try {
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String syncTimeType = rs.getString("sync_time_type");
				boolean isParagraph2=rs.getBoolean("isParagraph2");
				if(!isParagraph2)
				{
					continue;
				}
				if (syncTimeType.equals(DriverConstant.SyncTimeType_1)) {
					
					String[] syncWarmInfos = rs.getString("warm_sync_detail").split("#")[1].split("@");
					for (String syncWarmInfo : syncWarmInfos) {
						if (syncWarmInfo.equals(""))
							continue;
						JobDetail job = JobBuilder.newJob(SyncJob3.class).withIdentity(
								String.valueOf(id) + "." + UUID.randomUUID().toString(), "paragraph data sync").build();
						job.getJobDataMap().put("ParagraphID", id);
						try {
							CronTrigger trigger = TriggerBuilder.newTrigger()
									.withIdentity(String.valueOf(id) + "." + UUID.randomUUID().toString(),
											"paragraph data sync")
									.withSchedule(CronScheduleBuilder.cronSchedule(syncWarmInfo)).build();
							this.scheduler.scheduleJob(job, trigger);
							LOGGER.info("成功添加定时任务");
							LOGGER.info("任务详情：[Paragraph ID:{}, time:{}]", id, syncWarmInfo);
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs,ps,conn);
		}
	}
	
	public void addTask(Integer paragraphID) {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		String sql = "select * from paragraph where id=?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[] { paragraphID });
		try {
			while (rs.next()) {
				String syncTimeType = rs.getString("sync_time_type");
				if (syncTimeType.equals(DriverConstant.SyncTimeType_1)) {
					String[] syncWarmInfos = rs.getString("warm_sync_detail").split("#")[1].split("@");
					for (String syncWarmInfo : syncWarmInfos) {
						if (syncWarmInfo.equals(""))
							continue;
						JobDetail job = JobBuilder.newJob(SyncJob.class)
								.withIdentity(String.valueOf(paragraphID) + "." + UUID.randomUUID().toString(),
										"paragraph data sync")
								.build();//指明job的名称，所在组的名称，以及绑定job类    

						job.getJobDataMap().put("ParagraphID", paragraphID);
						try {
							CronTrigger trigger = TriggerBuilder.newTrigger()
									.withIdentity(String.valueOf(paragraphID) + "." + UUID.randomUUID().toString(),
											"paragraph data sync")
									.withSchedule(CronScheduleBuilder.cronSchedule(syncWarmInfo)).build();
							this.scheduler.scheduleJob(job, trigger);
							LOGGER.info("成功添加定时任务");
							LOGGER.info("任务详情：[Paragraph ID:{}, time:{}]", paragraphID, syncWarmInfo);
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs);
			DBHelper.free(ps);
			DBHelper.free(conn);
		}
	}
	
	public void addTask2(Integer paragraphID) {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		String sql = "select * from paragraph where id=?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[] { paragraphID });
		try {
			while (rs.next()) {
				String syncTimeType = rs.getString("sync_time_type");
				if (syncTimeType.equals(DriverConstant.SyncTimeType_1)) {
					String[] syncWarmInfos = rs.getString("warm_sync_detail").split("#")[1].split("@");
					for (String syncWarmInfo : syncWarmInfos) {
						if (syncWarmInfo.equals(""))
							continue;
						JobDetail job = JobBuilder.newJob(SyncJob2.class)
								.withIdentity(String.valueOf(paragraphID) + "." + UUID.randomUUID().toString(),
										"paragraph data sync")
								.build();//指明job的名称，所在组的名称，以及绑定job类    

						job.getJobDataMap().put("ParagraphID", paragraphID);
						try {
							CronTrigger trigger = TriggerBuilder.newTrigger()
									.withIdentity(String.valueOf(paragraphID) + "." + UUID.randomUUID().toString(),
											"paragraph data sync")
									.withSchedule(CronScheduleBuilder.cronSchedule(syncWarmInfo)).build();
							this.scheduler.scheduleJob(job, trigger);
							LOGGER.info("成功添加定时任务");
							LOGGER.info("任务详情：[Paragraph ID:{}, time:{}]", paragraphID, syncWarmInfo);
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs);
			DBHelper.free(ps);
			DBHelper.free(conn);
		}
	}
	
	public void addTask3(Integer paragraphID) {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();
			this.scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		String sql = "select * from paragraph where id=?";
		Connection conn=DBHelper.getConnection();
		PreparedStatement ps=null;
		try {
			ps=conn.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs = DBHelper.executeQuery(conn,ps,sql, new Object[] { paragraphID });
		try {
			while (rs.next()) {
				String syncTimeType = rs.getString("sync_time_type");
				if (syncTimeType.equals(DriverConstant.SyncTimeType_1)) {
					String[] syncWarmInfos = rs.getString("warm_sync_detail").split("#")[1].split("@");
					for (String syncWarmInfo : syncWarmInfos) {
						if (syncWarmInfo.equals(""))
							continue;
						JobDetail job = JobBuilder.newJob(SyncJob3.class)
								.withIdentity(String.valueOf(paragraphID) + "." + UUID.randomUUID().toString(),
										"paragraph data sync")
								.build();//指明job的名称，所在组的名称，以及绑定job类    

						job.getJobDataMap().put("ParagraphID", paragraphID);
						try {
							CronTrigger trigger = TriggerBuilder.newTrigger()
									.withIdentity(String.valueOf(paragraphID) + "." + UUID.randomUUID().toString(),
											"paragraph data sync")
									.withSchedule(CronScheduleBuilder.cronSchedule(syncWarmInfo)).build();
							this.scheduler.scheduleJob(job, trigger);
							LOGGER.info("成功添加定时任务");
							LOGGER.info("任务详情：[Paragraph ID:{}, time:{}]", paragraphID, syncWarmInfo);
						} catch (SchedulerException e) {
							e.printStackTrace();
						}
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBHelper.free(rs);
			DBHelper.free(ps);
			DBHelper.free(conn);
		}
	}

	public void shutdown() {
		try {
			this.scheduler.shutdown(true);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void shutdown(boolean WaitForJobsToComplete) {
		try {
			this.scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
