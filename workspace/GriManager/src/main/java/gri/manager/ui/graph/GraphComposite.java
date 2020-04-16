/**
* Created by weiwenjie on 2017-1-3
*/
package gri.manager.ui.graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * @Description 
 * Created by weiwenjie on 2017-1-3
 */
public class GraphComposite extends Composite{
	protected JFreeChart chart;
	
	/** 
	 * @Description: TODO
	 * @param parent
	 * @param style 
	 */
	public GraphComposite(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	 public void saveAsFile(String path){
	    	saveAsFile(chart, path, 600, 400);    
	    }
	    
    public static void saveAsFile(JFreeChart chart, String outputPath,int weight, int height) {       
        FileOutputStream out = null;      
        try {      
            File outFile = new File(outputPath);       
            if (!outFile.getParentFile().exists()) {       
                outFile.getParentFile().mkdirs();      
            }      
            out = new FileOutputStream(outputPath);      
            // 保存为PNG      
            ChartUtilities.writeChartAsPNG(out, chart, weight, height);      
            // 保存为JPEG      
            // ChartUtilities.writeChartAsJPEG(out, chart, weight, height);       
            out.flush();       
        } catch (FileNotFoundException e) {       
            e.printStackTrace();       
        } catch (IOException e) {       
            e.printStackTrace();       
        } finally {       
            if (out != null) {       
                try {       
                    out.close();      
                } catch (IOException e) {      
                    // do nothing      
                }      
            }      
        }      
    }  
}
