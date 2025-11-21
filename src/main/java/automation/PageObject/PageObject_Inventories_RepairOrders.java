package automation.PageObject;

import java.util.List;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import automation.BaseMethods.Controls;

public class PageObject_Inventories_RepairOrders extends Controls
{
	private Properties pi;
	public PageObject_Inventories_RepairOrders(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public void sortinventories() throws InterruptedException
	{
		Controls.clickElement(By.xpath(repairordersinventoryprop.getProperty("inventories_menu_link")));
		Controls.waitforvisibility(driver,By.xpath(repairordersinventoryprop.getProperty("inventories_link")),10);
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, true);
	}
	
	public void filterinventories() throws InterruptedException
	{
		Controls.clickElement(By.xpath(repairordersinventoryprop.getProperty("inventories_menu_link")));
		Controls.waitforvisibility(driver,By.xpath(repairordersinventoryprop.getProperty("inventories_link")),10);
		Thread.sleep(10000);
		Controls.clickElement(By.xpath(repairordersinventoryprop.getProperty("filter_btn")));
		
		String[] fields = {"fromdate","todate","pn","description","ronumber","pntype","avlqty","msnesn","repairtype","repairvendors","recdate",
		"parttier","stockvisible","dynamic","fulldynamic","expecteddeliverydate","roquotedate","roquoteapproveddate","shippedtovendordate","sn", "recievedby",
		"appcode","atachapter","conditioncode","partclassdesc","supplychain","stockline","roentrydate","airframerve","enginerve","rostatus","roworkscope"};
		
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(repairordersinventoryprop.getProperty("filter_"+field+"_title_locator")), repairordersinventoryprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(repairordersinventoryprop.getProperty("filter_"+field)), repairordersinventoryprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}
		
		//NOW FILTERING
		String[] attributes = {"repairtype"};
		String[][] filterdatasets = new String[attributes.length][];
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];

		for (int i = 0; i < attributes.length; i++) {
		    // Build keys dynamically for locators and filter data
		    String attr = attributes[i];
		    
		    // Filter input field XPath
		    filterInputLocators[i] = repairordersinventoryprop.getProperty("filter_" + attr);
		    
		    // Table column locator
		    tableColumnLocators[i] = By.xpath(repairordersinventoryprop.getProperty("column_" + attr));
		    
		    // Filter data (may contain multiple comma-separated values)
		    String filterDataProperty = repairordersinventoryprop.getProperty("filterdata_" + attr);
		    if (filterDataProperty == null || filterDataProperty.trim().isEmpty()) {
		        throw new IllegalArgumentException("Missing property: filterdata_" + attr);
		    }
		    
		    // Support multiple values like "No,Yes,Maybe"
		    filterdatasets[i] = filterDataProperty.split("\\s*,\\s*");
		}

				
		applyVerifyMultipleFilters(
			    By.xpath(repairordersinventoryprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(repairordersinventoryprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			    );
	}
	
	public void columnconfiguration() throws InterruptedException
	{
		Controls.clickElement(By.xpath(repairordersinventoryprop.getProperty("inventories_menu_link")));
		Controls.waitforvisibility(driver,By.xpath(repairordersinventoryprop.getProperty("inventories_link")),10);
		Thread.sleep(3000);
		
		String colconfigbtn = repairordersinventoryprop.getProperty("inventories_colconfig_btn");
		String[] fields = {"fromdate","todate","pn","description","ronumber","pntype","avlqty","msnesn","repairtype","repairvendors","recdate",
				"parttier","stockvisible","dynamic","fulldynamic","expecteddeliverydate","roquotedate","roquoteapproveddate","shippedtovendordate","sn", "recievedby",
				"appcode","atachapter","conditioncode","partclassdesc","supplychain","stockline","roentrydate","airframerve","enginerve","rostatus","roworkscope"};
		
		testColumnVisibilityConfiguration(colconfigbtn, fields, true);
	}
	
	public void columnpositionchange() throws InterruptedException
	{
		Controls.clickElement(By.xpath(repairordersinventoryprop.getProperty("inventories_menu_link")));
		Controls.waitforvisibility(driver,By.xpath(repairordersinventoryprop.getProperty("inventories_link")),10);
	    String colconfigbtn = repairordersinventoryprop.getProperty("inventories_colconfig_btn");
	    String columnconfigxpath = repairordersinventoryprop.getProperty("inventories_colconfig_xpath");
	    testcolumnpositionchange(colconfigbtn, columnconfigxpath, "RO Number","Repair Type");
	}
}