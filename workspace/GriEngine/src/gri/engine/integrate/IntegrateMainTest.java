package gri.engine.integrate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import gri.engine.util.Constant;

public class IntegrateMainTest {

	public IntegrateMainTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
//		IntegrateMain main = FileUtil.ReadMain(Constant.propertiesFolder + file.get(0));
//		File file=new File("C:/Users/admin/Desktop/binProperties");
//		File[] tempFile=file.listFiles();
//		List<String> fileList=new ArrayList<String>();
//		for(int i=0;i<tempFile.length;i++) {
//			fileList.add(tempFile[i].getName());
//		}
//		for(String fileName:fileList) {
//			List<String> files = FileUtil.findFilebyName("C:/Users/admin/Desktop/binProperties", fileName.substring(0,fileName.length()-6));
//			IntegrateMain main = FileUtil.ReadMain("C:/Users/admin/Desktop/binProperties/" + files.get(0));
////			String jsonStr=JSON.toJSONString(main);
//			FileUtil.WriteMainToJson(main, files.get(0));
//		}
//		IntegrateMain main = FileUtil.ReadMainFromJson(Constant.propertiesFolder + file.get(0));
//
//		String jsonStr=JSON.toJSONString(main);
//		System.out.println(jsonStr);
//		System.out.println(jsonStr.length());
//		IntegrateMain main2=JSON.parseObject(jsonStr,IntegrateMain.class);
//		System.out.println(main2);

		File file=new File(Constant.propertiesFolder);
		File[] tempFile=file.listFiles();
		for(int i=0;i<tempFile.length;i++) {
			if(tempFile[i].getName().endsWith("_1.txt_1.txt")) {
				tempFile[i].renameTo(new File(tempFile[i].getName().substring(0, tempFile[i].getName().length()-6)));
			}
		}
	
		
	}
}
