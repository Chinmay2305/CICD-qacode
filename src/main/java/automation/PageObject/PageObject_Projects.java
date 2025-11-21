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
	
	public PageObject_Projects(WebDriver driver)
	{
		this.driver=driver;
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