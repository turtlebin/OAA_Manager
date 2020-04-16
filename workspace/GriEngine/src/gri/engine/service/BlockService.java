/**
* Created by weiwenjie on 2017-6-25
*/
package gri.engine.service;

import gri.engine.util.Constant;
import gri.engine.util.RedisHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * @Description 
 * Created by weiwenjie on 2017-6-25
 */
// 区块链在reids中的结构为
/*
 * map<paragraphHash,blockName>
 * map<paragraphHash,content>
*/
public class BlockService {
	private RedisHelper redis = new RedisHelper("coiz.block");
	
	//空值处理，set 空表示del
	public String getBlockName(Integer paragraphId){
		String result = redis.hget("blockName", getParagraphHash(paragraphId));
		if(result ==null) result ="";
		return result;
	}
	
	public void setBlockName(Integer paragraphId, String blockName){
		if(blockName==null || blockName.equals("")){
			redis.hdel("blockName", getParagraphHash(paragraphId));
			redis.hdel("content", getParagraphHash(paragraphId));
		}
		else{
			redis.hset("blockName", getParagraphHash(paragraphId),blockName);
			new GriEngineService().dataSync(paragraphId);
		}	
	}
	
	public boolean hasBlock(Integer paragraphId){
		return redis.hexists("content", getParagraphHash(paragraphId));
	}
	
	public String readBlock(String blockName){
		//处理不设置区块链的情况
		if(blockName==null || blockName.equals("")){
			return "[]";
		}
		
		String arr ="[]";
		Map<String,String> names = redis.hgetAll("blockName");
		for (String key : names.keySet()) {
		    String name = names.get(key);
		    if(name.equals(blockName)){
		    	String newContent = redis.hget("content", key);
		    	if(arr.equals("[]") || newContent.equals("[]")) arr = arr.substring(0, arr.length()-1) + newContent.substring(1, newContent.length());
				else arr =  arr.substring(0, arr.length()-1) +"," + newContent.substring(1, newContent.length());
		    }
		}
		return arr;
	}
	
	public void syncBlock(Integer paragraphId, String content){
		if(redis.hexists("content", getParagraphHash(paragraphId))){
			redis.hset("content", getParagraphHash(paragraphId),content);
		}	
	}
	
	public String getParagraphHash(Integer paragraphId){
		String str= "";
		try {
			str+=InetAddress.getLocalHost().getHostAddress()+"###";
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str+=Constant.eitpServerPort+"###";
		str+=paragraphId;
		return str;
	}
}
