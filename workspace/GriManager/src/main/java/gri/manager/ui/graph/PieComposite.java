package gri.manager.ui.graph;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import gri.driver.model.process.StatsResult;

/**
 * @Description 
 * Created by erik on 2016-9-12
 */
public class PieComposite extends GraphComposite{	
	private Composite composite;
	private StatsResult data;
	private ChartComposite frame;
	
	public PieComposite(Composite composite,StatsResult data) {
		super(composite, SWT.NONE);
		this.composite = composite;
		this.data=data;
		createContents();
	}
	
	
	/**
	 * Create contents of the dialog.
	 */
	protected void createContents(){
		
		JFreeChart chart = createChart(createDataset());
		frame = new ChartComposite(composite, SWT.NONE, chart,
                true);
		frame.setSize(composite.getSize().x,composite.getSize().y);
		//frame.pack();
	}
	
	public void dispose(){
		frame.dispose();
		super.dispose();
	}
	
	/**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        /*double sum=0;
        for(int i=0;i<data.getRowName().size();i++) sum+=data.getData().get(0).get(i);*/
        
        for(int i=0;i<data.getRowName().size();i++)
        	dataset.setValue(data.getRowName().get(i), data.getData().get(0).get(i)/*/sum*100*/);
        return dataset;        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private JFreeChart createChart(PieDataset dataset) {
    	//创建主题样式  
        StandardChartTheme mChartTheme = new StandardChartTheme("CN");  
        //设置标题字体  
        mChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 20));  
        //设置轴向字体  
        mChartTheme.setLargeFont(new Font("宋体", Font.CENTER_BASELINE, 15));  
        //设置图例字体  
        mChartTheme.setRegularFont(new Font("宋体", Font.CENTER_BASELINE, 15));  
        //应用主题样式  
        ChartFactory.setChartTheme(mChartTheme); 
        
        JFreeChart chart = ChartFactory.createPieChart(
            data.getTitle(),  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(  
                "{0}:{1},({2})", NumberFormat.getNumberInstance(),  
                new DecimalFormat("0.00%")));
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
        
    }
}
