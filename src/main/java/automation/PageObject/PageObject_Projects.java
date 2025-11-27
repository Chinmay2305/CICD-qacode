package automation.PageObject;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import automation.BaseMethods.Controls;

public class PageObject_Projects extends Controls
{
	private WebDriver driver;
	private PageObject_Analytics pa;
	
	public PageObject_Projects(WebDriver driver)
	{
		this.driver=driver;
		this.pa = new PageObject_Analytics(driver);
	}
	
	public void openprojects() throws IOException, InterruptedException
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement projects_icon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(projectsprop.getProperty("projects_icon"))));
		projects_icon.click();
	}
	
	public void open1stproject()
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try
		{
			System.out.println("Waiting for 1st project element to be clickable...");
			WebElement firstproject_icon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(projectsprop.getProperty("view_firstproject_icon"))));
			
			if(firstproject_icon!=null)
			{
				System.out.println("1st project element found. Clicking...");
				firstproject_icon.click();
				System.out.println("1st project clicked successfully.");
			}
			else
			{
				System.out.println("1st project element not found!");
	            throw new RuntimeException("1st project element not found.");
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in open 1st project: "+e.getMessage());
			throw e;
		}
	}
	
	public void sortprojects() throws InterruptedException
	{
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, false);
	}
	
	public int singlefilterforanalytics() throws InterruptedException
	{
		System.out.println("FILTER FROM ANALYTICS PART: "+pa.bidstatus);
		
		Controls.get(projectsprop.getProperty("projects_url"));
		Thread.sleep(5000);
		Controls.clickElement(By.xpath(projectsprop.getProperty("filter_btn")));
		
		//NOW FILTERING
		String[] attributes = {"bidstatus"};
		String[][] filterdatasets = {
				{pa.bidstatus}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = projectsprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(projectsprop.getProperty("column_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(projectsprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(projectsprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			);
		
		WebElement rowCount = driver.findElement(By.xpath("(//main[contains(@class,'MuiBox-root')])[2]/div[2]/div/div[2]/div[3]/span"));
		String countText = rowCount.getText().replace(",", "").trim();  // remove commas
		return Integer.parseInt(countText);
	}
	
	public void filterprojects() throws InterruptedException
	{
		Controls.waitforvisibility(driver, By.xpath(projectsprop.getProperty("filter_btn")), 10);
		
/*		String[] fields = {"fromdate","todate","pn","description","status","pntype","avlqty","parttier","recoforrep"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(inventoryprop.getProperty("filter_"+field+"_title_locator")), inventoryprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(inventoryprop.getProperty("filter_"+field)), inventoryprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}
*/		
		//NOW FILTERING
		String[] attributes = {"projectid"};
		String[][] filterdatasets = {
				{"AF-24-0300"}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = projectsprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(projectsprop.getProperty("column_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(projectsprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(projectsprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			    );

	}
}