package gri.driver.util;

public class DriverConstant {
	// 数格引擎帐号
	public final static String GriEngineAccount = "GriEngine";
	
	//引擎控制
	public final static String shutEngine="shutEngine";

	// 数据源类型
	public static final String DataSourceType_File = "file";
	public static final String DataSourceType_Database = "database";
	public static final String DataSourceType_WebService = "web service";
	public static final String DataSourceType_ParagraphEngine = "paragraph engine";
	public static final String DataSourceType_GriEngine = "gri engine";
	public static final String DataSourceType_View = "view";

	// 同步方向
	public static final String SyncDirectionType_0 = "positive"; // 数据源->数格引擎
	public static final String SyncDirectionType_1 = "negetive"; // 数格引擎->数据源
	public static final String SyncDirectionType_2 = "two way";// 数格引擎<->数据源

	// 同步时机
	public static final String SyncTimeType_0 = "hot"; // 实时同步
	public static final String SyncTimeType_1 = "warm"; // 定时、周期同步
	public static final String SyncTimeType_2 = "cold";// 手动同步

	// 临时一次连接EITP帐号名前缀
	public static final String TempConnctionAccoutPrefix = "Temp_Connction_Accout_Prefix";

	// 测试连接
	public static final String OPERATE_TEST = "test";

	// User
	public static final String OPERATE_GET_ALL_USER = "get all user";
	public static final String OPERATE_ADD_USER = "add user";
	public static final String OPERATE_DELETE_USER = "delete user";
	public static final String OPERATE_UPDATE_USER = "update user";

	// GriDoc
	public static final String OPERATE_GET_ALL_GRIDOC = "get all gridoc";
	public static final String OPERATE_GET_CHILDREN_OF_GRIDOC = "get gridoc's children";
	public static final String OPERATE_ADD_GRIDOC = "add gridoc";
	public static final String OPERATE_DELETE_GRIDOC = "delete gridoc";
	public static final String OPERATE_UPDATE_GRIDOC = "update gridoc";

	// Section
	public static final String OPERATE_GET_CHILDREN_OF_SECTION = "get section's children";

	// GriElement
	public static final String OPERATE_ADD_GRI_ELEMENT = "add gri element";
	public static final String OPERATE_ADD_GRI_ELEMENT2 = "add gri element2";
	public static final String OPERATE_DELETE_GRI_ELEMENT = "delete gri element";
	public static final String OPERATE_DELETE_UNREFERENCE_GRI_ELEMENT = "delete unreference gri element";
	public static final String OPERATE_REMOVE_GRI_ELEMENT = "remove gri element";
	public static final String OPERATE_UPDATE_GRI_ELEMTNT = "update gri element";

	public static final String OPERATE_GET_ALL_PARAGRAPH_OF_GRI_ELEMTNT = "get all paragraph of gri element";

	// Paragraph Data
	public static final String OPERATE_READ_PARAGRAPH_SIZE = "read paragraph data size";
	public static final String OPERATE_READ_PARAGRAPH = "read paragraph";
	public static final String OPERATE_WRITE_PARAGRAPH = "write paragraph";
	public static final String OPERATE_READ_PARAGRAPH_PREVIEW = "read paragraph preview data";
	public static final String OPERATE_REFRESH_PARAGRAPH_PREVIEW = "refresh paragraph preview data";

	// Gri Engine cache
	public static final String OPERATE_SYNC_DATA = "sync paragraph cache with data source";

	public static final String OPERATE_CHECK_CRONEXPRESSION = "CronExpression";

	public static final String OPERATE_TEST_DATA_SOURCE_CONNECT = "test data source connect";

	// GriDoc-User AccessPermission
	public static final String OPERATE_ADD_ACCESS_PERMISSION = "add access permission";
	public static final String OPERATE_DELETE_ACCESS_PERMISSION = "delete access permission";
	public static final String OPERATE_QUERY_ACCESS_PERMISSION = "query access permission";
	public static final String OPERATE_UPDATE_ACCESS_PERMISSION = "update access permission";

	// query GriElement
	public static final String OPERATE_QUERY_GRIELEMENT = "query GriElement";

	//stats
	public static final String OPERATE_COL_LIST = "col list";
	public static final String OPERATE_STATS_CALCULATE = "stats calculate";
	public static final String OPERATE_LIST_STATS = "list stats";
	public static final String OPERATE_ADD_STATS = "add stats";
	public static final String OPERATE_UPDATE_STATS = "update stats";
	public static final String OPERATE_DEL_STATS = "del stats";
	
	//process
	public static final String OPERATE_LIST_PROCESSOR = "list processor";
	public static final String OPERATE_ADD_PROCESSOR = "add processor";
	public static final String OPERATE_UPDATE_PROCESSOR = "update processor";
	public static final String OPERATE_DEL_PROCESSOR = "del processor";
	
	public static final String OPERATE_LIST_CONTAINER = "list container";
	public static final String OPERATE_LIST_CONTAINER_BY_PROCESSOR = "list container by processor";
	public static final String OPERATE_ADD_CONTAINER = "add container";
	public static final String OPERATE_UPDATE_CONTAINER = "update container";
	public static final String OPERATE_DEL_CONTAINER = "del container";
	
	public static final String OPERATE_LIST_PROCESS = "list process";
	public static final String OPERATE_ADD_PROCESS = "add process";
	public static final String OPERATE_UPDATE_PROCESS = "update process";
	public static final String OPERATE_DEL_PROCESS = "del process";
	
	public static final String OPERATE_READ_VIEW = "read view";
	public static final String OPERATE_LIST_VIEW = "list view";
	public static final String OPERATE_LIST_VIEW_BY_PROCESSOR = "list view by processor";
	public static final String OPERATE_GET_VIEW_BY_NAME = "get view by name";
	public static final String OPERATE_ADD_VIEW = "add view";
	public static final String OPERATE_UPDATE_VIEW = "update view";
	public static final String OPERATE_DEL_VIEW = "del view";	
	public static final String OPERATE_LIST_VIEWS_BY_CONTAINER = "list views by container";
	
	//管道，海量读写相关
	public static final String OPERATE_GET_PARAGRAPH_BY_ID = "get paragraph by id";
	public static final String OPERATE_GET_PARAGRAPH_BY_NAME = "get paragraph by name";
	public static final String OPERATE_GET_REAL_PARAGRAPH = "get real paragraph";
	public static final String OPERATE_READ_DB_PAGE = "read db page";
	
	//区块链
	public static final String OPERATE_SET_BLOCK_NAME = "set block name";
	public static final String OPERATE_GET_BLOCK_NAME = "get block name";
	public static final String OPERATE_READ_BLOCK = "read block";
	

	
	
	// query GriElement支持属性
	public static final String GriDocQueryAttr_0 = "名称";
	public static final String GriDocQueryAttr_1 = "关键词";
	public static final String GriDocQueryAttr_2 = "描述";
	public static final String GriDocQueryAttr_3 = "全文检索";

	// data change notify
	public static final String OPERATE_DATA_CHANGE = "data change";

	//GraphType
	//饼图
	public static final int GraphType_Pie = 1;
	//条形图
	public static final int GraphType_Bar = 2;
	//折线图
	public static final int GraphType_Line = 3;
	
	//GroupByType
	//枚举值
	public static final int GroupByType_Enum = 1;
	//固定段数
	public static final int GroupByType_Part = 2;
	//固定段数
	public static final int GroupByType_Interval = 3;
	//日期
	public static final int GroupByType_Day = 21;
	//月份
	public static final int GroupByType_Month = 22;
	//季度
	public static final int GroupByType_Season = 23;
	//年
	public static final int GroupByType_Year = 24;
	//按周数值统计
	public static final int GroupByType_EveryWeekDay = 25;
	
	
	
	//StatsOperType
	//计数
	public static final int StatsOperType_Count = 1;
	//求和
	public static final int StatsOperType_Sum = 2;
	//求平均
	public static final int StatsOperType_Avg = 3;
	//求 最大值
	public static final int StatsOperType_Max = 4;
	//求最小值
	public static final int StatsOperType_Min = 5;
	
	
	//FilterOperType
	//等于
	public static final int FilterOperType_Equal = 1;
	//不等于
	public static final int FilterOperType_NotEqual = 2;
	//大于
	public static final int FilterOperTypeMoreThan = 3;
	//小于
	public static final int FilterOperTypeLessThan = 4;
	//自定义
	public static final int FilterOperTypeCustom = 5;

	public static final String Update_Sync="update_sync";
}
