package automation.PageObject;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import automation.BaseMethods.Controls;

public class PageObject_Inventories_InRepair extends Controls
{
	private Properties pi;
	public PageObject_Inventories_InRepair(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public void sortinventories() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/in-repair");
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, true);
	}
	
	public void filterinventories() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/in-repair");
		Thread.sleep(10000);
		Controls.clickElement(By.xpath(inrepairinventoryprop.getProperty("filter_btn")));
		
/*		String[] fields = {"fromdate","todate","pn","description","ronumber","status","pntype","avlqty","recdate","parttier","dynamic","fulldynamic","serialno","atachapter","conditioncode","partclassdesc","stockline"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(inrepairinventoryprop.getProperty("filter_"+field+"_title_locator")), inrepairinventoryprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(inrepairinventoryprop.getProperty("filter_"+field)), inrepairinventoryprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}*/
		
		//NOW FILTERING
		String[] attributes = {"pn"};
		String[][] filterdatasets = {
				{"7021450-801"}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = inrepairinventoryprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(inrepairinventoryprop.getProperty("column_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(inrepairinventoryprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(inrepairinventoryprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			    );
	}
}