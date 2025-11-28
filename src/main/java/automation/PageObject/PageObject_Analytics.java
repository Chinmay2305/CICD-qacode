package automation.PageObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import automation.BaseMethods.Controls;

public class PageObject_Analytics extends Controls
{
	private WebDriver driver;
	private Properties pa;
	private By chartCanvasLocator = By.xpath("(//canvas[@data-zr-dom-id='zr_0'])[1]"); // Locator for the chart canvas
    private By tooltipLocator = By.xpath("//div[contains(@style, 'visibility: visible')]"); // Locator for visible tooltip
    //HAVE TO KEEP THESE FIELDS HERE ONLY BECAUSE THEY ARE ACCESSED IN PAGEOBJECT CLASSES ALSO
    String vendorregion = "South america";		//FOR VENDORS
    String bidstatus = "closed";	//FOR PROJECTS
    
	public PageObject_Analytics(WebDriver driver)
	{
		this.driver = driver;
		this.pa=new Properties();
		
		// Load the properties file
        try (FileInputStream fis = new FileInputStream("resources/analytics.properties"))
        {
            pa.load(fis);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties file");
        }
	}
	
	public int homevendors_vendorsregiongraph() throws Exception
	{
		driver.get(analyticsprop.getProperty("analytics_url"));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pa.getProperty("homevendors_link"))));
        Controls.clickElement(By.xpath(pa.getProperty("homevendors_link")));
        Thread.sleep(5000);
                
        int countfromPie = Controls.validatePieChartECharts2(2, vendorregion);
        System.out.println("Count recieved in Analytics: "+countfromPie);
        return countfromPie;
	}
	
	public int projectstatusgraph() throws Exception
	{
		driver.get(analyticsprop.getProperty("analytics_url"));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(pa.getProperty("projectstatus_link"))));
		Controls.clickElement(By.xpath(pa.getProperty("projectstatus_link")));
		Thread.sleep(5000);
		
		int countfromPie = Controls.validatePieChartECharts2(1, bidstatus);
		System.out.println("Count recieved in Projects: "+countfromPie);
		return countfromPie;
	}
}