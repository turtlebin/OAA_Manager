package gri.driver.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gri.driver.model.AccessPermission;
import gri.driver.model.Connection;
import gri.driver.model.GriDoc;
import gri.driver.model.GriElement;
import gri.driver.model.GriElementData2;
import gri.driver.model.GriElementData3;
import gri.driver.model.GriElementData4;
import gri.driver.model.PageDto;
import gri.driver.model.ParagraphDataReadRequest;
import gri.driver.model.ParagraphDataReadResponse;
import gri.driver.model.ParagraphDataWriteRequest;
import gri.driver.model.Section;
import gri.driver.model.SyncConfigHolder;
import gri.driver.model.process.Paragraph;
import gri.driver.util.DriverConstant;

public class GriDocManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(GriDocManager.class);

	private Connection connection;

	public GriDocManager(Connection connecton) {
		this.connection = connecton;
		this.positon = 0;
	}

	private int positon; // 避免positon多线程冲突

	// 避免positon多线程冲突
	public GriDocManager copy() {
		return new GriDocManager(this.connection);
	}

	public UserManager UserManager() {
		return new UserManager(this.connection);
	}
	
	public ProcessManager ProcessManager() {
		return new ProcessManager(this.connection);
	}

	public String getCurrentAccount() {
		return this.connection.getUser();
	}

	public void shutEngine() {
		Object result=this.connection.execute_sync(DriverConstant.shutEngine, (GriDoc)null);
		return;
	}
	
	/******************************************************************/
	/*********** ↓↓↓↓↓↓↓↓↓↓↓****GriElement *****↓↓↓↓↓↓↓↓↓↓↓ ***********/

	// 获取格文档节点的子节点
	@SuppressWarnings("unchecked")
	public List<GriElement> getChildren(GriElement griElement) {
		if (griElement instanceof GriDoc) {
			Object result = this.connection.execute_sync(DriverConstant.OPERATE_GET_CHILDREN_OF_GRIDOC,
					(GriDoc) griElement);
			if (result != null)
				return (List<GriElement>) result;
			else
				return new ArrayList<GriElement>();
		} else if (griElement instanceof Section) {
			Object result = this.connection.execute_sync(DriverConstant.OPERATE_GET_CHILDREN_OF_SECTION,
					(Section) griElement);
			if (result != null)
				return (List<GriElement>) result;
			else
				return new ArrayList<GriElement>();
		}
		return new ArrayList<GriElement>();
	}

	// 增加格文档元素
	public GriElement addGriElement(GriDoc gridoc, GriElement father, GriElement child) {
		if (gridoc == null || father == null || child == null)
			return null;
		if (father instanceof Paragraph || child instanceof GriDoc)
			return null;

		Object result = this.connection.execute_sync(DriverConstant.OPERATE_ADD_GRI_ELEMENT,
				new GriElementData3(gridoc, father, child));
		if (result != null)
			return (GriElement) result;
		else
			return null;
	}
	
	public GriElement addGriElement(GriDoc gridoc, GriElement father, GriElement child,boolean addTableName) {
		if (gridoc == null || father == null || child == null)
			return null;
		if (father instanceof Paragraph || child instanceof GriDoc)
			return null;

		Object result = this.connection.execute_sync(DriverConstant.OPERATE_ADD_GRI_ELEMENT2,
				new GriElementData4(gridoc, father, child,addTableName));
		if (result != null)
			return (GriElement) result;
		else
			return null;
	}


	// 删除格文档元素
	public boolean deleteGriElement(GriDoc gridoc, GriElement griElement) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_DELETE_GRI_ELEMENT,
				new GriElementData2(gridoc, griElement));
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	// 移除格文档元素
	public boolean removeGriElement(GriDoc gridoc, GriElement griElement) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_REMOVE_GRI_ELEMENT,
				new GriElementData2(gridoc, griElement));
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	//// 查询格文档元素下的所有段
	@SuppressWarnings("unchecked")
	public List<Paragraph> getParagraphOfGriElement(GriElement griElement) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_GET_ALL_PARAGRAPH_OF_GRI_ELEMTNT,
				griElement);
		if (result != null)
			return (List<Paragraph>) result;
		else
			return new ArrayList<Paragraph>();
	}

	// 更新格文档元素
	public boolean updateGriElement(GriDoc gridoc, GriElement griElement) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_UPDATE_GRI_ELEMTNT,
				new GriElementData2(gridoc, griElement));
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}
	
	public boolean updateSyncTaskManager(SyncConfigHolder syncHolder){
		Object result=this.connection.execute_sync(DriverConstant.Update_Sync, syncHolder);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	/**************************************************************/
	/*********** ↓↓↓↓↓↓↓↓↓↓↓****GriDoc *****↓↓↓↓↓↓↓↓↓↓↓ ***********/

	// 获取所有的格文档
	@SuppressWarnings("unchecked")
	public List<GriDoc> getAllGriDoc() {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_GET_ALL_GRIDOC, "");
		if (result != null)
			return (List<GriDoc>) result;
		else
			return new ArrayList<GriDoc>();
	}

	// 增加格文档
	public GriDoc addGriDoc(GriDoc gridoc) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_ADD_GRIDOC, gridoc);
		if (result != null)
			return (GriDoc) result;
		else
			return null;
	}

	// 删除格文档
	public boolean deleteGriDoc(GriDoc gridoc) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_DELETE_GRIDOC, gridoc);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	// 修改格文档
	public boolean updateGriDoc(GriDoc gridoc) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_UPDATE_GRIDOC, gridoc);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	// 读取段数据（循环读）
	public int readData(Paragraph paragraph, byte[] bytes) {
		int size = this.readData(paragraph, bytes, this.positon);
		if (size == -1)
			this.positon = 0;
		else
			this.positon = this.positon + size;
		return size;
	}

	// 段数据大小
	public int dataSize(Paragraph paragraph) {
		Object result = getTempConnection().execute_sync(DriverConstant.OPERATE_READ_PARAGRAPH_SIZE, paragraph.getId());
		if (result != null) {
			int size = (Integer) result;
			return size;
		}
		return 0;
	}

	private int readData(Paragraph paragraph, byte[] bytes, int pos) {
		Object result = getTempConnection().execute_sync(DriverConstant.OPERATE_READ_PARAGRAPH,
				new ParagraphDataReadRequest(paragraph.getId(), pos, bytes.length));
		if (result != null) {
			ParagraphDataReadResponse data = (ParagraphDataReadResponse) result;
			int size = data.size;
			for (int i = 0; i < size; i++)
				bytes[i] = data.bytes[i];
			return size;
		} else
			return -1;
	}

	public boolean writeFile(Paragraph paragraph, byte[] bytes, boolean append) {
		Object result = getTempConnection().execute_sync(DriverConstant.OPERATE_WRITE_PARAGRAPH,
				new ParagraphDataWriteRequest(paragraph.getId(), bytes, append));
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	public boolean CronExpressionIsValid(String cronExpressionStr) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_CHECK_CRONEXPRESSION, cronExpressionStr);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	/**
	 * 判断数据源是否有效
	 * 
	 * @param dataSourceConnectPath
	 *            由"DataSourceType###DataSourcePath"组成<br>
	 *            <br>
	 *            DataSourceType可选值: <br>
	 *            DriverConstant.DataSourceType_File<br>
	 *            DriverConstant.DataSourceType_Database<br>
	 *            DriverConstant.DataSourceType_WebService<br>
	 *            DriverConstant.DataSourceType_ParagraphEngine<br>
	 *            <br>
	 *            DataSourcePath可选值:<br>
	 *            "DB类型###主机###端口###数据库名###用户名###密码"<br>
	 *            "ftp URL###user###password"<br>
	 *            <br>
	 * 
	 * @return
	 */
	public boolean testDataSourceConnect(String dataSourceConnectPath) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_TEST_DATA_SOURCE_CONNECT,
				dataSourceConnectPath);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	public boolean addAccessPermission(AccessPermission accessPermission) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_ADD_ACCESS_PERMISSION, accessPermission);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	public boolean deleteAccessPermission(AccessPermission accessPermission) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_DELETE_ACCESS_PERMISSION, accessPermission);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	public boolean updateAccessPermission(AccessPermission accessPermission) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_UPDATE_ACCESS_PERMISSION, accessPermission);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	@SuppressWarnings("unchecked")
	public List<AccessPermission> queryAccessPermission(GriDoc gridoc) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_QUERY_ACCESS_PERMISSION, gridoc);
		if (result != null)
			return (List<AccessPermission>) result;
		else
			return new ArrayList<AccessPermission>();
	}

	// 格文档查询（属性、全文检索）
	/**
	 * 
	 * @param attr
	 *            名称、关键词、描述、全文检索
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GriElement> query(String attr, String value) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_QUERY_GRIELEMENT, attr + "###" + value);
		if (result != null)
			return (List<GriElement>) result;
		else
			return new ArrayList<GriElement>();

	}

	// 强制同步段数据（缓存-数据源）
	public boolean forceSyncData(Integer paragraphID) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_SYNC_DATA, paragraphID);
		LOGGER.info("段" + paragraphID + "强制同步结果：" + result);
		if (result != null)
			return (Boolean) result;
		else
			return false;
	}

	// 查看段预览数据
	public String previewParagraphData(Integer paragraphID) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_READ_PARAGRAPH_PREVIEW, paragraphID);
		if (result != null)
			return (String) result;
		else
			return "";
	}

	// 刷新服务器段预览数据
	public void refreshPreviewParagraphData(Integer paragraphID) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_REFRESH_PARAGRAPH_PREVIEW, paragraphID);
		LOGGER.info("段" + paragraphID + "刷新服务器段预览数据结果：" + result);
	}

	// 清理删除未引用格文档元素
	public void clearUnreferenceParagraph() {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_DELETE_UNREFERENCE_GRI_ELEMENT, "");
		LOGGER.info("清理删除未引用格文档元素结果：" + result);
	}

	private Connection getTempConnection() {
		return new Connection("data upload/download connection", this.connection.getHost(),
				DriverConstant.TempConnctionAccoutPrefix + UUID.randomUUID().toString(), "");
	}
	
	
	
	public gri.driver.model.process.Paragraph getParagraphById(Integer paragraphId){
		Object result = connection.execute_sync(DriverConstant.OPERATE_GET_PARAGRAPH_BY_ID, paragraphId);
		if (result != null)
			return (gri.driver.model.process.Paragraph) result;
		else
			return null;
	}
	
	public gri.driver.model.process.Paragraph getParagraphByName(String name){
		Object result = connection.execute_sync(DriverConstant.OPERATE_GET_PARAGRAPH_BY_NAME, name);
		if (result != null)
			return (gri.driver.model.process.Paragraph) result;
		else
			return null;
	}
	
	public gri.driver.model.process.Paragraph getRealParagraph(String dataSourcePath){
		Object result = connection.execute_sync(DriverConstant.OPERATE_GET_REAL_PARAGRAPH, dataSourcePath);
		if (result != null)
			return (gri.driver.model.process.Paragraph) result;
		else
			return null;
	}
	
	public final static int BLOCKSIZE = 500000;
	//通用的读段数据接口
	public byte[] readData1(Paragraph paragraph){
		byte[] result = new byte[0];
		int size = BLOCKSIZE;
		int pos =0;
		while(size==BLOCKSIZE){
			byte[] tempbBytes;
			
			Object obj = this.connection.execute_sync(DriverConstant.OPERATE_READ_PARAGRAPH,
					new ParagraphDataReadRequest(paragraph.getId(), pos, BLOCKSIZE));
			if (obj != null) {
				ParagraphDataReadResponse data = (ParagraphDataReadResponse) obj;
				size = data.size;
				pos += size;
				
				if(size>0){
					//合并之前的bytes和本次bytes
					byte[] temp = new byte[result.length+size];
					if(result.length>0)
						System.arraycopy(result,0,temp,0,result.length);
					System.arraycopy(data.bytes,0,temp,result.length,size);
					result = temp;
				}
			} else return result;
			
			/*size = readData(paragraph,tempbBytes,pos);
			pos += size;
			//合并之前的bytes和本次bytes
			byte[] temp = new byte[result.length+size];
			System.arraycopy(result,0,temp,0,result.length);
			System.arraycopy(tempbBytes,0,temp,result.length,size);
			result = temp;*/
			
			System.out.println(size+","+pos);
		}
		//System.out.println(new String(result));
		return result;
	}
	
	//通用的写段数据接口
	/*public void writeData1(Integer paragraphId, byte[] datas){
		
	}*/
	
	//process
	/*public String getViewData(Integer viewID) {
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_READ_VIEW, viewID);
		if (result != null)
			return (String) result;
		else
			return "";
	}*/
	
	public PageDto readDbPage(Integer paragraphId, Integer pageNo, Integer pageSize){
		Object result = this.connection.execute_sync(DriverConstant.OPERATE_READ_DB_PAGE, new Integer[]{paragraphId, pageNo, pageSize});
		if (result != null)
			return (PageDto) result;
		else
			return null;
	}

}
