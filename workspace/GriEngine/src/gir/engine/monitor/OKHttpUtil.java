package gir.engine.monitor;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
 

public class OKHttpUtil {
 
	/**
	 * 发起get请求
	 * 
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String httpGet(String url) throws UnsupportedEncodingException {
		String result = null;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		try {
			Response response = client.newCall(request).execute();
			result = response.body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
 
	/**
	 * 发送httppost请求
	 * 
	 * @param url
	 * @param data  提交的参数为key=value&key1=value1的形式
	 * @return
	 * @throws IOException 
	 */
	public static void httpPost(String url, String data) throws IOException {
		OkHttpClient httpClient = new OkHttpClient();
		 FormBody formBody = new FormBody.Builder()
	                .add("MsgHandle","usip.handle.MoniCtrlHandle.handleMsg")
	                .add("VEReqMsgType", "")
	                .add("VEReqMsgName","")
	                .add("VeName","")
	                //.add("EIOVEDATA", "{\"op\":\"sendMoniData\", \"subid\": 20001, \"dataName\": \"power\", \"type\": 0, \"unit\": \"%\", \"value\": 95, \"time\": \"2018-07-10 16:28:03\",\"test\":\"test\"}").build();
		            .add("EIOVEDATA",data).build();
		Request request = new Request.Builder().url(url).post(formBody).build();
//	    Response response = httpClient.newCall(request).execute();
//	    if (response.isSuccessful()) {
//	        return response.body().string();
//	    } else {
//	        throw new IOException("Unexpected code " + response);
//	    }
		Call call=httpClient.newCall(request);
		call.enqueue(new Callback() {
			 @Override
	            public void onFailure(Call call, IOException e) {
	                System.out.println("Fail");
	            }

	            @Override
	            public void onResponse(Call call, Response response) throws IOException {
	                System.out.println(response.body().string());
	            }
		});
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		try {
			httpPost("http://localhost:8080/ECPServer/EIOServletMsgEngine", "{\"op\":\"getAllTypeData\",\"subid\":20001,\"type\":0}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//httpGet("http://localhost:8080/ECPServer/EIOServletMsgEngine?MsgHandle=usip.handle.MoniCtrlHandle.handleMsg&VEReqMsgType&VEReqMsgName&VeName&EIOVEDATA={\"op\":\"sendMoniData\", \"subid\": 20001, \"dataName\": \"power\", \"type\": 0, \"unit\": \"%\", \"value\": 95, \"time\": \"2018-07-10 16:28:03\",\"test\":\"test\"}");
		
	}
}