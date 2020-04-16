package gri.manager.ui.graph;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import gri.driver.model.process.StatsResult;

/**
 * @Description 
 * Created by erik on 2016-9-12
 */
public class LineComposite extends GraphComposite{	
	private Composite composite;
	private StatsResult data;
	private ChartComposite frame;
	
	public LineComposite(Composite composite,StatsResult data) {
		super(composite, SWT.NONE);
		this.composite = composite;
		this.data=data;
		createContents();
	}
	
	/**
	 * Create contents of the dialog.
	 */
	protected void createContents(){
		chart = createChart(createDataset());
		frame = new ChartComposite(composite, SWT.NONE, chart,true);
		frame.setSize(composite.getSize().x,composite.getSize().y);
		//frame.pack();
	}
	
	public void dispose(){
		frame.dispose();
		super.dispose();
	}
	
	
	/**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private CategoryDataset createDataset() {
        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int i=0;i<data.getColName().size();i++)
        	for(int j=0;j<data.getRowName().size();j++)
        		dataset.addValue(data.getData().get(i).get(j), data.getColName().get(i), data.getRowName().get(j));
                return dataset;

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private JFreeChart createChart(CategoryDataset dataset) {
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
    	
    	
        // create the chart...
        JFreeChart chart = ChartFactory.createLineChart(
            data.getTitle(),       // chart title
            data.getRowSummary(),               // domain axis label
            data.getColSummary(),                  // range axis label
            dataset,
			PlotOrientation.VERTICAL,
			true, 
			true, 
			false
        );
        
        
        CategoryPlot mPlot = (CategoryPlot)chart.getPlot();
		mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
		mPlot.setRangeGridlinePaint(Color.BLUE);//背景底部横虚线
		mPlot.setOutlinePaint(Color.RED);//边界线
        
        return chart;

    }
    
   
}
