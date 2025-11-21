package automation.PageObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

import java.io.File;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import automation.BaseMethods.Controls;

public class PageObject_Assets extends Controls
{
	public PageObject_Assets(WebDriver driver)
	{
		this.driver=driver;
	}
	private String getProgressValue()
	{
		try
		{
			WebElement progressbar = driver.findElement(By.cssSelector("span.MuiLinearProgress-barColorPrimary"));
			return progressbar.getAttribute("style");
		}
		catch(Exception e)
		{
			System.out.println("Progress bar not found: " + e.getMessage());
			return null;
		}
	}
	public void addasset() throws InterruptedException
	{
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		By addBtn = By.xpath(assetsprop.getProperty("add_asset_btn"));
		w.until(ExpectedConditions.visibilityOfElementLocated(addBtn));
		w.until(ExpectedConditions.elementToBeClickable(addBtn));
		waitForProgressBarToDisappear();
		driver.findElement(addBtn).click();
		waitForProgressBarToDisappear();
//		w.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("span.MuiLinearProgress-barColorPrimary")));

		Controls.editText(By.xpath(assetsprop.getProperty("asset_type_box")), assetsprop.getProperty("asset_type"));
		
		//Once asset type is selected, we will verify field labels and place holders
		if(assetsprop.getProperty("asset_type").equals("Airframe"))
		{
			Controls.confirmdata(By.xpath(assetsprop.getProperty("msnesn_box_title")), "MSN", "MSN title");
			Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("msnesn_box")), "Enter MSN", "MSN");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("family_box_title")), "Aircraft Family *", "Family title");
			Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("family_box")), "Select Aircraft Family", "Family");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("type_box_title")), "Aircraft Type *", "Type title");
			Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("type_box")), "Select Aircraft Type", "Type");
		}
		else
		{
			Controls.confirmdata(By.xpath(assetsprop.getProperty("msnesn_box_title")), "ESN", "ESN title");
			Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("msnesn_box")), "Enter ESN", "ESN");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("family_box_title")), "Engine Family *", "Family title");
			Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("family_box")), "Select Engine Family *", "Family");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("type_box_title")), "Engine Type *", "Type title");
			Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("type_box")), "Select Engine Type", "Type");
		}
		
		Controls.confirmdata(By.xpath(assetsprop.getProperty("dom_box_title")), "Year of Manufacture *", "DOM title");
		Controls.confirmdata(By.xpath(assetsprop.getProperty("condition_box_title")), "Condition *", "Condition title");
		Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("condition_box")), "Select Condition", "Condition");
		Controls.confirmdata(By.xpath(assetsprop.getProperty("loc_asset_box_title")), "Location of Asset (Facility)", "Location of Asset (Facility) title");
		Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("loc_asset_box")), "Enter Location of Asset (Facility)", "Location of Asset (Facility)");
		Controls.confirmdata(By.xpath(assetsprop.getProperty("loc_asset_region_box_title")), "Location of Asset (Region)", "Location of Asset (Region) title");
		Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("loc_asset_region_box")), "Select Location of Asset (Region)", "Location of Asset (Region)");
		Controls.confirmdata(By.xpath(assetsprop.getProperty("operating_region_box_title")), "Operating Region", "Operating Region title");
		Controls.confirmplaceholders(By.xpath(assetsprop.getProperty("operating_region_box")), "Select Operating Region", "Operating Region");
		
		//After confirming labels and place holders, start adding the data
		Controls.type(By.xpath(assetsprop.getProperty("msnesn_box")), assetsprop.getProperty("msnesn"));
		
		Controls.pickYear(assetsprop.getProperty("dom"));

		String assetfamily, assettype;
		if(assetsprop.getProperty("asset_type").equalsIgnoreCase("Airframe"))
		{
			assetfamily = "airframe_family";
			assettype = "airframe_type";
		}
		else
		{
			assetfamily = "engine_family";
			assettype = "engine_type";
		}
		
		Controls.dropdown(By.xpath(assetsprop.getProperty("family_box")), assetsprop.getProperty(assetfamily));
		Controls.dropdown(By.xpath(assetsprop.getProperty("type_box")), assetsprop.getProperty(assettype));
		Controls.dropdown(By.xpath(assetsprop.getProperty("condition_box")), assetsprop.getProperty("condition"));
		Controls.type(By.xpath(assetsprop.getProperty("loc_asset_box")), assetsprop.getProperty("loc_asset"));
		Controls.dropdown(By.xpath(assetsprop.getProperty("loc_asset_region_box")), assetsprop.getProperty("loc_asset_region"));
		Controls.dropdown(By.xpath(assetsprop.getProperty("operating_region_box")), assetsprop.getProperty("operating_region"));
		Controls.type(By.xpath(assetsprop.getProperty("last_operator_box")), assetsprop.getProperty("last_operator"));
		Controls.dropdown(By.xpath(assetsprop.getProperty("avionics_rom_box")), assetsprop.getProperty("avionics_rom"));
		Controls.dropdown(By.xpath(assetsprop.getProperty("components_rom_box")), assetsprop.getProperty("components_rom"));
		Controls.type(By.xpath(assetsprop.getProperty("remarks_box")), assetsprop.getProperty("remarks"));
		Controls.fileattach(By.xpath(assetsprop.getProperty("attachment_box")), assetsprop.getProperty("attachment_file"));
		Controls.clickElement(By.xpath(assetsprop.getProperty("asset_save_btn")));
	}
	
	public void confirmasset() throws InterruptedException
	{
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
		w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(assetsprop.getProperty("add_asset_btn"))));
		Thread.sleep(5000);
		
		if(assetsprop.getProperty("asset_type").equals("Airframe"))
		{
			Controls.confirmdata(By.xpath(assetsprop.getProperty("msnesn_table")), "MSN : "+assetsprop.getProperty("msnesn"), "MSN/ESN");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("family_table2")), assetsprop.getProperty("family")+" / "+assetsprop.getProperty("type"), "Family/Type");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("dom_table")), assetsprop.getProperty("dom"), "Date of Manufacture");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("last_operator_table")), assetsprop.getProperty("last_operator"), "Last Operator");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("operating_region_table")), assetsprop.getProperty("operating_region"), "Operating Region");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("stage_table")), "Initiated", "Stage");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("loc_asset_table")), assetsprop.getProperty("loc_asset"), "Location of Asset (Facility)");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("condition_table")), assetsprop.getProperty("condition"), "Condition");
		}
		else
		{
			Controls.confirmdata(By.xpath(assetsprop.getProperty("msnesn_table")), "ESN : "+assetsprop.getProperty("msnesn"), "MSN/ESN");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("family_table2")), assetsprop.getProperty("family")+" / "+assetsprop.getProperty("type"), "Family/Type");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("dom_table")), assetsprop.getProperty("dom"), "Date of Manufacture");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("last_operator_table")), assetsprop.getProperty("last_operator"), "Last Operator");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("operating_region_table")), assetsprop.getProperty("operating_region"), "Operating Region");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("stage_table")), "Initiated", "Stage");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("loc_asset_table")), assetsprop.getProperty("loc_asset"), "Location of Asset (Facility)");
			Controls.confirmdata(By.xpath(assetsprop.getProperty("condition_table")), assetsprop.getProperty("condition"), "Condition");
		}
	}
	
	public void addharvestlist() throws EncryptedDocumentException, IOException, InterruptedException
	{ 
		//Step 1. Import XLS file
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement addharvestlistbtn = null;
		String filePath = null;
		String assetType = assetsprop.getProperty("asset_type");
		String engineType = harvestlistprop.getProperty("engine_type");
		
		switch (assetType.toLowerCase())
		{
		case "airframe": 
			addharvestlistbtn = w.until(ExpectedConditions.elementToBeClickable(By.xpath(harvestlistprop.getProperty("airframe_upload_btn"))));
			filePath = "C:\\Users\\DELL\\Downloads\\Sample Airframe Harvest List.xlsx";
			break;
		case "engine":
			switch(engineType.toLowerCase())
			{
			case "llp":
				addharvestlistbtn = w.until(ExpectedConditions.elementToBeClickable(By.xpath(harvestlistprop.getProperty("llp_upload_btn"))));
				filePath = "C:\\Users\\DELL\\Downloads\\Sample LLP Harvest List-"+assetsprop.getProperty("engine_type")+".xlsx";
				break;
				
			case "nonllp":
				addharvestlistbtn = w.until(ExpectedConditions.elementToBeClickable(By.xpath(harvestlistprop.getProperty("nonllp_upload_btn"))));
				filePath = "C:\\Users\\DELL\\Downloads\\Sample Non-LLP Harvest List.xlsx";
				break;
				
			case "lru":
				addharvestlistbtn = w.until(ExpectedConditions.elementToBeClickable(By.xpath(harvestlistprop.getProperty("lru_upload_btn"))));
				filePath = "C:\\Users\\DELL\\Downloads\\Sample LRU Harvest List.xlsx";
				break;
				
			default:
				System.out.println("Invalid engine type is selected.");
				break;
			}
			break;
			default:
				System.out.println("Invalid asset type is selected.");
				break;
		}
		addharvestlistbtn.click();
		driver.switchTo().activeElement();
		Controls.clickElement(By.xpath(harvestlistprop.getProperty("addharvest")));
		driver.switchTo().activeElement();
		//WebElement fileInput = driver.findElement(By.xpath("//div[contains(@class,'chakra-modal__body')]/div[contains(@role,'presentation')]/input"));
		WebElement fileInput = w.until(ExpectedConditions.presenceOfElementLocated(
			    By.xpath("//div[contains(@class,'chakra-modal__body')]/div[contains(@role,'presentation')]/input")
			));

		fileInput.sendKeys(filePath);
		Controls.clickElement(By.xpath(harvestlistprop.getProperty("next_btn")));
		
		//Step 2. Select header row - Header & Data confirmation
		// Read Excel data
		if(assetsprop.getProperty("asset_type").equals("Engine"))
		{
			Controls.clickElement(By.xpath("((//div[contains(@role, 'row')])[3]/div)[1]"));
		}
		
		List<List<String>> excelData = Controls.readExcel(filePath);
		
		// Fetch grid data
		String tableRowXpath = "//div[contains(@class, 'rdg-row')]";
		String tableCellXpath = ".//div[contains(@role, 'gridcell')]";
		List<List<String>> gridData = Controls.fetchWebTableData(driver, tableRowXpath, tableCellXpath);
		Controls.compareWebAndExcelRowByRow(excelData, gridData);
		Thread.sleep(2000);
		Controls.clickElement(By.xpath(harvestlistprop.getProperty("next_btn")));
		
		//Step 3-a. Match Columns - Verify column names are matching or not
		System.out.println("-----Now matching column names-----");
		List<WebElement> top_names = driver.findElements(By.xpath(harvestlistprop.getProperty("top_titles_list")));
		List<WebElement> bottom_names = driver.findElements(By.xpath(harvestlistprop.getProperty("bottom_titles_list")));
		
		if(top_names.size()!=bottom_names.size())
		{
			System.out.println("The number of columns are not matching!!");
			for(int i=0;i<top_names.size();i++)
			{
				String top_name = top_names.get(i).getText().trim();
				String bottom_name = bottom_names.get(i).getText().trim();
				System.out.println(top_name);
				System.out.println(bottom_name);
			}
		}
		else
		{
			for(int i=0;i<top_names.size();i++)
			{
				String top_name = top_names.get(i).getText().trim();
				String bottom_name = bottom_names.get(i).getText().trim();
				
				if(top_name.equals(bottom_name))
				{
					System.out.println("Match found at index " + i + ": " + top_name);
				}
				else
				{
					System.out.println("Mismatch at index " + i + ":");
					System.out.println("Top Name: " + top_name);
					System.out.println("Bottom Name: " + bottom_name);
				}
			}
		}
		
		//Step 3-b. Match Columns - Verify sub-column names in drop-downs are matching or not
		List<WebElement> subtitles_Buttons = driver.findElements(By.xpath(harvestlistprop.getProperty("subtitles_button")));
		
		for(WebElement button:subtitles_Buttons)
		{
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(button));
			button.click();
		}
		System.out.println("-----Now matching dropdown column names-----");
		List<WebElement> top_subtitles = driver.findElements(By.xpath(harvestlistprop.getProperty("top_subtitles_list")));
		List<WebElement> bottom_subtitles = driver.findElements(By.xpath(harvestlistprop.getProperty("bottom_subtitles_list")));
		try
		{
			if(top_subtitles.size()!=bottom_subtitles.size())
			{
				System.out.println("The number of sub-titles are not matching!!");
				for(int i=0;i<top_subtitles.size();i++)
				{
					String top_subtitle = top_subtitles.get(i).getText().trim();
					String bottom_subtitle = bottom_subtitles.get(i).getText().trim();
					System.out.println(top_subtitle);
					System.out.println(bottom_subtitle);
				}
			}
			else
			{
				for(int i=0;i<top_subtitles.size();i++)
				{
					String top_subtitle = top_subtitles.get(i).getText().trim();
					String bottom_subtitle = bottom_subtitles.get(i).getText().trim();
					
					if(top_subtitle.equals(bottom_subtitle))
					{
						System.out.println("Match found at index " + i + ": " + top_subtitle);
					}
					else
					{
						System.out.println("Mismatch at index " + i + ":");
						System.out.println("Top Name: " + top_subtitle);
						System.out.println("Bottom Name: " + bottom_subtitle);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Failed to click on a button: " + e.getMessage());
		}
		Controls.clickElement(By.xpath(harvestlistprop.getProperty("next_btn_matchcolumns")));
		Controls.compareWebAndExcelRowByRow(excelData, gridData);
		Thread.sleep(2000);
		Controls.clickElement(By.xpath(harvestlistprop.getProperty("confirm_btn")));
	}
	
	public void harvestlistpage() throws InterruptedException
	{
		Thread.sleep(5000);
	//	harvestlistfilters();
		Controls.waitforvisibility(driver, By.xpath(assetsprop.getProperty("inventory_check_btn")), 10);
	}
	
	public void harvestlistfilters() throws InterruptedException
	{
		By harvestfilterbtn;
		String asset=null;
		if(assetsprop.getProperty("asset_type").equalsIgnoreCase("Airframe"))
		{
			asset="airframe";
		}
		else
		{
			asset="engine";
		}
		harvestfilterbtn = By.xpath(harvestlistprop.getProperty(asset+"harvest_filter_btn"));
		
		Controls.waitforvisibility(driver, harvestfilterbtn, 50);
		String[] fields = {"pn","sn","desc","partclassdesc","ata","condition","category"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(harvestlistprop.getProperty("filter_"+field+"_title_locator")), harvestlistprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(harvestlistprop.getProperty(asset+"filter_"+field)), harvestlistprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);			
		}
		
		String[] attributes = {"partclassdesc","ata"};
		String[][] filterdatasets = {{"nacelle"},{"25"}};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = harvestlistprop.getProperty(asset+"filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(harvestlistprop.getProperty("col_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			  harvestfilterbtn,
				filterInputLocators,
			    filterdatasets,
			    new boolean[] {false,false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(harvestlistprop.getProperty(asset+"filter_apply")),
			    tableColumnLocators,
			    attributes
			    );
	}
	
	public void inventorycheckpage() throws InterruptedException, StaleElementReferenceException, ElementClickInterceptedException
	{
		// Continue to Pricing Check
		Thread.sleep(5000);
		if(assetsprop.getProperty("asset_type").equalsIgnoreCase("Airframe"))
		{
			Controls.waitforvisibility(driver, By.xpath(assetsprop.getProperty("pricing_check_btn")), 100);
		}
		else
		{
			Controls.waitforvisibility(driver, By.xpath(assetsprop.getProperty("engine_pricing_check_btn")), 10);
		}
		
		By popupButton = By.xpath("//div[contains(@class,'MuiDialogActions-root')]/button[2]");
		By popupOverlay = By.cssSelector("div.MuiDialog-container");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		// Wait briefly for popup (since it may or may not appear)
		List<WebElement> popup = driver.findElements(popupButton);
		
		if (!popup.isEmpty())
		{
			System.out.println("Popup appeared. Waiting for PROCEED button to be clickable...");	        
	        WebElement proceedBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(popupButton));
	        
	        List<WebElement> overlay = driver.findElements(popupOverlay);
	        if (!overlay.isEmpty())
	        {
	            System.out.println("Overlay detected, will perform JS click through overlay...");
	        }

	        try
	        {
	        	proceedBtn.click();
	        	System.out.println("Clicked PROCEED normally.");
	        }
	        catch(ElementClickInterceptedException e)
	        {
	        	System.out.println("Normal click intercepted. Performing JS click...");
	        	((JavascriptExecutor) driver).executeScript("arguments[0].click();", proceedBtn);
	        }
	    }
		else
		{
			System.out.println("Popup not displayed. Proceeding further...");
		}
	}
	
	public void pricingpage() throws InterruptedException
	{
		if(assetsprop.getProperty("asset_type").equalsIgnoreCase("Airframe"))
		{
			Controls.waitforvisibility(driver, By.xpath(assetsprop.getProperty("cashflow_run_btn")), 100);
		}
		else
		{
			Controls.waitforvisibility(driver, By.xpath(assetsprop.getProperty("engine_cashflow_run_btn")), 100);
		}
	}
	
	//HELPER METHODS
	private void waitForProgressBarToDisappear() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	    try {
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(
	            By.cssSelector("span.MuiLinearProgress-barColorPrimary")));
	    } catch (Exception e) {
	        System.out.println("Progress bar not present or already gone.");
	    }
	}

	public List<List<String>> getHarvestTableData()
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		By tableLocator = By.xpath("//table");
		wait.until(ExpectedConditions.presenceOfElementLocated(tableLocator));
		wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
		List<List<String>> tableData = new ArrayList<>();
		for (int attempt = 0; attempt < 3; attempt++)
		{
			try
			{
				WebElement harvestTable = driver.findElement(tableLocator);
				List<WebElement> rows = harvestTable.findElements(By.tagName("tr"));
				tableData.clear();
				
				for(WebElement row : rows)
				{
					List<WebElement> cells = row.findElements(By.tagName("td"));
					if (cells.isEmpty()) 
						continue; // Skip header/blank rows
					
					List<String> rowData = new ArrayList<>();
					for (WebElement cell : cells)
					{
						rowData.add(cell.getText().trim());
					}
					tableData.add(rowData);
				}
				System.out.println("----- Web Table Data (Row-wise) -----");
				for (List<String> row : tableData)
				{
					System.out.println(row);
				}
				return tableData;
			}
			catch(StaleElementReferenceException e)
			{
				try
				{
					Thread.sleep(1000); 
				}
				catch(InterruptedException ie)
				{
					Thread.currentThread().interrupt(); 
				}
			} 
		}
		throw new RuntimeException("Failed to fetch harvest table data after multiple retries.");
	}
	
	public void compareWebAndExcelData(List<List<String>> webData, List<List<String>> excelData)
	{
		// Define column mapping: Web column index → Excel column index 
		Map<Integer, Integer> columnMap = new HashMap<>();
		columnMap.put(0, 0);
		// Part Number(Web col 1 → Excel col 1)
		columnMap.put(2, 1);
		// Serial Number (Web col 3 → Excel col 2)
		columnMap.put(1, 2);
		// Description (Web col 2 → Excel col 3)
		columnMap.put(3, 3);
		// Part Class Description (Web col 4 → Excel col 4)
		columnMap.put(6, 5);
		// TSN (Web col 7 → Excel col 6)
		columnMap.put(7, 6);
		// CSN (Web col 8 → Excel col 7)
		System.out.println("\n===== 🔍 STARTING CELL-BY-CELL COMPARISON =====");
		// Skip header row from Excel (index 0)
		for (int i = 0; i < webData.size() && (i + 1) < excelData.size(); i++)
		{
			List<String> webRow = webData.get(i);
			List<String> excelRow = excelData.get(i + 1);
			
			// shift by 1 since row 0 is header
			System.out.println("\n🟦 Comparing Row " + (i + 1));
			for(Map.Entry<Integer, Integer> entry : columnMap.entrySet())
			{
				int webCol = entry.getKey();
				int excelCol = entry.getValue();
				String webValue = (webCol < webRow.size()) ? webRow.get(webCol).trim() : "";
				String excelValue = (excelCol < excelRow.size()) ? excelRow.get(excelCol).trim() : "";
				if(webValue.equalsIgnoreCase(excelValue))
				{
					System.out.printf("✅ Match at Row %d | Web Col %d ↔ Excel Col %d | Value: '%s'%n", (i + 1), webCol + 1, excelCol + 1, webValue);
				}
				else
				{
					System.out.printf("❌ Mismatch at Row %d | Web[%d]='%s' | Excel[%d]='%s'%n", (i + 1), webCol + 1, webValue, excelCol + 1, excelValue);
					}
				
			}
			System.out.println("\n===== ✅ COMPARISON COMPLETE =====");
		}
	}
}