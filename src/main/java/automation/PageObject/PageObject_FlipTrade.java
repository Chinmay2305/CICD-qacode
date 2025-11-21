package automation.PageObject;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.bidi.browsingcontext.UserPromptClosed;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;

import automation.BaseMethods.Controls;

public class PageObject_FlipTrade extends Controls
{
	private Properties pf;
	public PageObject_FlipTrade(WebDriver driver)
	{
		this.driver = driver;
	}
	
	//SORTING
	public void sortdata() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/fliptrading");
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, true);
	}

	public void multiplefilter() throws InterruptedException
	{
		driver.get(fliptradeprop.getProperty("fliptradeurl"));
		Thread.sleep(5000);
		Controls.clickElement(By.xpath(fliptradeprop.getProperty("filter_btn")));
		
		String[] fields = {"refno","customer","status","date"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(fliptradeprop.getProperty("filter_"+field+"_title_locator")), fliptradeprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(fliptradeprop.getProperty("filter_"+field)), fliptradeprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}
		
		
		//NOW FILTERING
		String[] attributes = {"status", "customer"};
		String[][] filterdatasets = {
				{"In Progress", "Quote Received"},{"AEROMAR"}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = fliptradeprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(fliptradeprop.getProperty(attributes[i]+"_column"));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(fliptradeprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false, false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(fliptradeprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			);
	}
}