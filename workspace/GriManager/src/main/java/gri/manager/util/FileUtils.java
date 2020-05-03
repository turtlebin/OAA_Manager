package gri.manager.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static void write(String path,String json) {
        FileOutputStream outputStream=null;
        BufferedOutputStream bufferedOutputStream=null;
        try{
            outputStream=new FileOutputStream(path);
            bufferedOutputStream=new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bufferedOutputStream!=null) {
                    bufferedOutputStream.close();
                }
                if(outputStream!=null) {
                    outputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
