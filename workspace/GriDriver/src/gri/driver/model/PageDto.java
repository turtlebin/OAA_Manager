/**
* Created by weiwenjie on 2017-7-3
*/
package gri.driver.model;

import java.io.Serializable;

/**
 * @Description 
 * Created by weiwenjie on 2017-7-3
 */
public class PageDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String data;
	private int totalRecord;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	
	
}
