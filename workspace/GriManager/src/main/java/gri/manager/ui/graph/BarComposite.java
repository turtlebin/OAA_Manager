package gri.manager.ui.graph;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import gri.driver.model.process.StatsResult;

/**
 * @Description 
 * Created by erik on 2016-9-12
 */
public class BarComposite extends GraphComposite{	
	private Composite composite;
	private StatsResult data;
	private ChartComposite frame;
	
	public BarComposite(Composite composite,StatsResult data) {
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
        JFreeChart chart = ChartFactory.createBarChart(
            data.getTitle(),       // chart title
            data.getRowSummary(),               // domain axis label
            data.getColSummary(),                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );
        
        
        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // the SWTGraphics2D class doesn't handle GradientPaint well, so
        // replace the gradient painter from the default theme with a
        // standard painter...
        renderer.setBarPainter(new StandardBarPainter());

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        renderer.setIncludeBaseInRange(true);   
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());   
        renderer.setBaseItemLabelsVisible(true);
        
        return chart;

    }
    
   
}
