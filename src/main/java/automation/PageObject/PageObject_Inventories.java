package automation.PageObject;

import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.bidi.browsingcontext.UserPromptClosed;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;

import automation.BaseMethods.Controls;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class PageObject_Inventories extends Controls
{
	private Properties pi;
	public PageObject_Inventories(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public void hidehistoricparts() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/inventories");
		waitforvisibility(driver, By.xpath(inventoryprop.getProperty("column_status_title")), 10);
		filterinventories();
		Thread.sleep(20000);
		waitforvisibility(driver, By.xpath(inventoryprop.getProperty("column_status")), 10);
		WebElement row = driver.findElement(By.xpath("(//tbody/tr)[2]"));
		if(row.isDisplayed())
		{
			System.out.println("Not working correctly because I have hidden historic parts then also it is visible.");
		}
		else
		{
			System.out.println("It is working correctly.");
		}
	}
	
	public void unhidehistoricparts() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/inventories");
		waitforvisibility(driver, By.xpath(inventoryprop.getProperty("column_status_title")), 10);
		Thread.sleep(20000);
		WebElement color = driver.findElement(By.xpath(inventoryprop.getProperty("historic_color")));
		Controls.clickElement(By.xpath(inventoryprop.getProperty("historic_button")));
		Thread.sleep(5000);
		waitforvisibility(driver, By.xpath(inventoryprop.getProperty("column_status")), 10);
		List<WebElement> statusElements = driver.findElements(By.xpath(inventoryprop.getProperty("column_status")));
		for (WebElement element : statusElements)
		{
			if(element.getText().toString().contains("HISTORIC"))
			{
				System.out.println(element.getText().toString()+" found. This is right.");
			}
			else
			{
			    continue;
			}
		}
	}
	
	public void sortinventories() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/inventories");
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, true);
	}
	
	//BOTH ARE REQUIRED FOR FILTERS AND CHECKING PARENT-CHILD ROW COMBINATIONS
/*	public String[] attributes = {"condcode","appcode","status","sn","stockvisible"}; 
	public String[][] filterdatasets = {
			{"AR","BER"},{"A320 CEO FAM","A320 NEO FAM"},{"on repair"},{"620"},{"no"}
	};*/
	
	//BOTH ARE REQUIRED FOR FILTERS AND CHECKING QTY TOTAL IN PARENT-CHILD ROWS
/*	public String[] attributes = {"status","pntype","stockvisible","dynamic"};
	public String[][] filterdatasets = {
			{"In Stock"},{"Consumable","Stand"},{"Yes"},{"E"}
	};*/
	
	public void filterinventories() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/inventories");
		Controls.waitforvisibility(driver, By.xpath(inventoryprop.getProperty("filter_btn")), 50);
		
/*		String[] fields = {"fromdate","todate","pn","lotconsignmentcode","qty","recdate","dynamiccategory","conditioncode",
				"partclassdesc"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(inventoryprop.getProperty("filter_"+field+"_title_locator")), inventoryprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(inventoryprop.getProperty("filter_"+field)), inventoryprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}*/
		
		//NOW FILTERING
	/*	String[] attributes = {"pntype","stockvis","atachapter","dynamic","fulldynamic"};
		String[][] filterdatasets = {
				{"rotable"},{"No"},{"23"},{"O"},{"OO"}
		};*/
		
		String[] attributes = {"status","pntype","stockvisible","dynamic"};
		String[][] filterdatasets = {
				{"In Stock"},{"Consumable","Stand"},{"Yes"},{"E"}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = inventoryprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(inventoryprop.getProperty("main_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(inventoryprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false,false,false,false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(inventoryprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			    );
	}

	
	/*CHILD ROW FILTERING TEST CASE-1: 
	Verify filters in Parent and Child rows */
	public void ChildRowsExpanding_VerifyFiltersforParentAndChild(String[][] filterdatasets, String[] attributes) throws InterruptedException
	{
		int mainRowCount = 0;

	    while (true) {
	        List<WebElement> allRows = driver.findElements(By.xpath("//tbody/tr"));

	        // Break when we reach the end of parent rows
	        if (mainRowCount >= allRows.size()) break;

	        WebElement currentRow = allRows.get(mainRowCount);

	        // Check if this is a main row (has expand icon)
	        boolean isMainRow = !currentRow.findElements(By.xpath(inventoryprop.getProperty("expand_button"))).isEmpty();
	        if (!isMainRow) {
	            mainRowCount++;
	            continue;
	        }

	        System.out.println("➡️ Validating Parent Row #" + (mainRowCount + 1));

	        // ✅ Validate parent row attributes
	        Map<String, String> parentDataMap = new HashMap<>();
	        for (int j = 0; j < attributes.length; j++) {
	            String attr = attributes[j];
	            if (attr.equals("qtyinstock"))
	            	continue;
	            String xpath = inventoryprop.getProperty("main_" + attr);
	            String cellText = currentRow.findElement(By.xpath(xpath)).getText().trim();
	            parentDataMap.put(attr, cellText);
	        }

	        boolean parentMatch = true;
	        for (int j = 0; j < attributes.length; j++) {
	        	String attr = attributes[j];
	            if (attr.equals("qtyinstock"))
	            	continue;
	            String actualValue = parentDataMap.get(attr).toLowerCase();
	            boolean match = Arrays.stream(filterdatasets[j])
	                    .anyMatch(expected -> actualValue.contains(expected.toLowerCase()));
	            if (!match) {
	                System.out.println("❌ Parent mismatch in " + attr + ": Expected one of " +
	                        Arrays.toString(filterdatasets[j]) + " & Found: " + actualValue);
	                parentMatch = false;
	            }
	        }

	        if (parentMatch) {
	            System.out.println("✅ All parent row values match filters.");
	        }

	        // ✅ Expand the parent row
	        WebElement expandBtn = currentRow.findElement(By.xpath("./td[1]//*[name()='svg']"));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", expandBtn);
	        expandBtn.click();
	        Thread.sleep(800); // wait for child row to appear

	        // Re-fetch rows after expansion
	        List<WebElement> updatedRows = driver.findElements(By.xpath("//tbody/tr"));

	        // Ensure child exists
	        if (mainRowCount + 1 < updatedRows.size()) {
	            WebElement childRow = updatedRows.get(mainRowCount + 1);
	            List<WebElement> childCells = childRow.findElements(By.xpath(".//td//table//tbody/tr[1]/td"));

	            if (!childCells.isEmpty()) {
	                Map<String, String> childDataMap = new HashMap<>();
	                childDataMap.put("sn", getSafeText(childCells, 0));
	                childDataMap.put("qtyinstock", getSafeText(childCells, 1));
	                childDataMap.put("appcode", getSafeText(childCells, 4));
	                childDataMap.put("condcode", getSafeText(childCells, 5));
	                childDataMap.put("status", getSafeText(childCells, 6));
	                childDataMap.put("msnesn", getSafeText(childCells, 7));
	                childDataMap.put("stockvisible", getSafeText(childCells, 8));
	                
	                // Only these keys exist in the child row:
	                Set<String> childAvailableKeys = childDataMap.keySet();
	                
	                boolean childMatch = true;
	                for (int j = 0; j < attributes.length; j++) {
	                    String attr = attributes[j];
	                    
	                    if (!childAvailableKeys.contains(attr)) {
	                        System.out.println("⚠️ Skipping child check for '" + attr + "' (not available in child row)");
	                        continue;
	                    }
	                    
	                    String actualValue = childDataMap.getOrDefault(attr, "").toLowerCase();
	                    boolean match;
	                    if (attr.equals("qtyinstock")) {
	                        // Exact match
	                        match = Arrays.stream(filterdatasets[j])
	                            .anyMatch(expected -> actualValue.equalsIgnoreCase(expected));
	                    } else {
	                        // Contains match
	                        match = Arrays.stream(filterdatasets[j])
	                            .anyMatch(expected -> actualValue.contains(expected.toLowerCase()));
	                    }
	                    System.out.println("• " + attr + ": " + actualValue);
	                    if (!match) {
	                        System.out.println("❌ Child mismatch in " + attr + ": Expected one of " +
	                                Arrays.toString(filterdatasets[j]) + " & Found: " + actualValue);
	                        childMatch = false;
	                    }
	                }

	                if (childMatch) {
	                    System.out.println("✅ All child row values match filters.");
	                }
	            } else {
	                System.out.println("⚠️ Child row cells not found.");
	            }
	        } else {
	            System.out.println("⚠️ No child row found after expansion.");
	        }

	        System.out.println("------------------------------------------------");

	        // Move to next parent row (2 steps down: current + its child)
	        mainRowCount += 2;
	    }
	}
	
	/*CHILD ROW FILTERING TEST CASE-2: 
	QTY in Stock in Parent = Total of QTY Available in Child Rows */
	public void ChildRowsExpanding_VerifyTotalQTYforParentAndChild() throws InterruptedException
	{
		List<WebElement> parentRows = driver.findElements(By.xpath(inventoryprop.getProperty("main_row")));
		List<WebElement> qtyInStockCells  = driver.findElements(By.xpath(inventoryprop.getProperty("main_qtyinstock")));
		
		for(int i=0;i<parentRows.size();i++)
		{
			//Expand current parent
			WebElement expandIcon = parentRows.get(i).findElement(By.xpath(".//td[1]//*[name()='svg']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", expandIcon);
            expandIcon.click();
            Thread.sleep(800); // Wait for child row to render
			
			String mainQTYText = qtyInStockCells.get(i).getText().trim();
			int mainQTY=0;
			if (!mainQTYText.isEmpty() && !mainQTYText.equals("-")) {
		        try {
		            mainQTY = Integer.parseInt(mainQTYText);
		        } catch (NumberFormatException e) {
		            System.out.println("Invalid number in parent row: " + mainQTYText);
		        }
		    }
			
            //Fetch QTY IN STOCK from parent row
            //List<WebElement> childQTY = driver.findElements(By.xpath(inventoryprop.getProperty("child_qtyinstock")));
            //List<WebElement> visibleChildQty = new ArrayList<>();
			
			// Build a custom XPath to get **only the child rows** immediately after this parent
	        // Assuming child rows come directly after parent row in DOM
	        String childQtyXPath = "(" + inventoryprop.getProperty("main_row") + ")[" + (i + 1) + "]/following-sibling::tr[1]//td[2]";
	        List<WebElement> childQtyElements = driver.findElements(By.xpath(childQtyXPath));
            int totalchildQTY = 0;
            for(WebElement childqty:childQtyElements)
            {
            	String childText = childqty.getText().trim();
                System.out.println("CHILD QTY: " + childText);
                if (!childText.equals("-") && !childText.isEmpty()) {
                    try
                    {
                        totalchildQTY += Integer.parseInt(childText);
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Invalid number in child row: " + childText);
                    }
                }
            }
            if (mainQTY==totalchildQTY)
            {
                System.out.println("✅ QTY MATCH: Parent = " + mainQTY + ", Child Total = " + totalchildQTY);
            } else {
                System.out.println("❌ QTY MISMATCH: Parent = " + mainQTY + ", Child Total = " + totalchildQTY);
            }
		}
	}
	
	private String getSafeText(List<WebElement> cells, int index)
	{
	   // return index < list.size() ? list.get(index).getText().trim() : "(missing)";
		if (index >= 0 && index < cells.size()) {
	        return cells.get(index).getText().trim();
	    }
	    return "";
	}
	
	private String formatAttributeName(String attr) {
	    switch (attr.toLowerCase()) {
	        case "sn": return "Serial Number";
	        case "appcode": return "Application Code";
	        case "condcode": return "Condition Code";
	        case "status": return "Status";
	        case "msnesn": return "MSN/ESN";
	        case "stockvisible": return "Stock Visibility";
	        default: return attr;
	    }
	}

	public List<List<String>> getWebTableData(By tablelocator)
	{
		WebElement table = driver.findElement(By.xpath(inventoryprop.getProperty("tablelocator")));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		List<List<String>> tableData = new ArrayList<>();
		for(WebElement row:rows)
		{
			List<WebElement> cells = row.findElements(By.tagName("td"));
			List<String> rowData = new ArrayList<>();
			for(WebElement cell:cells)
			{
				rowData.add(cell.getText().trim());
			}
			tableData.add(rowData);
		}
		System.out.println("From the getWebTableData: "+tableData);
		return tableData;
	}
	
	public List<String> getWebTableHeaders(By tablelocator)
	{
		WebElement table = driver.findElement(tablelocator);
		List<WebElement> headerCells = table.findElements(By.tagName("th"));
        List<String> headers = new ArrayList<>();
        for (WebElement cell : headerCells)
        {
            headers.add(cell.getText().trim());
        }
        return headers;
	}
	
	public List<List<String>> getXLSdata(String filepath) throws FileNotFoundException, IOException, InterruptedException
	{
		System.out.println("NOW PRINTING FROM XLS");
		List<List<String>> xlsData = new ArrayList<>();
		Controls.clickElement(By.xpath(inventoryprop.getProperty("export_btn")));
		filepath = inventoryprop.getProperty("export_location");
		Thread.sleep(5000);
		System.out.println("File path: " + filepath);
		File file = new File(filepath);
		int attempts=0;
		while(!file.exists() && attempts<10)
		{
			Thread.sleep(1000);
			attempts++;
		}
		
		if (!file.exists()) {
		    System.out.println("File does not exist at the specified location.");
		}
		
		try(FileInputStream fis = new FileInputStream(new File(filepath));
		XSSFWorkbook workbook = new XSSFWorkbook(fis))
		{
			XSSFSheet sheet = workbook.getSheetAt(0);
			for(Row row:sheet)
			{
				List<String> rowData = new ArrayList<>();
				for(org.apache.poi.ss.usermodel.Cell cell:row)
				{
					switch(cell.getCellType())
					{
					case STRING:
						rowData.add(cell.getStringCellValue().trim());
						System.out.println("From the getXLSData: "+cell.getStringCellValue()+"\t");
						break;
					case NUMERIC:
						rowData.add(String.valueOf(cell.getNumericCellValue()));
						System.out.println("From the getXLSData: "+cell.getNumericCellValue()+"\t");
						break;
					case BOOLEAN:
						rowData.add(String.valueOf(cell.getBooleanCellValue()));
						System.out.println("From the getXLSData: "+cell.getBooleanCellValue()+"\t");
						break;
					default:
						rowData.add("");
						System.out.println("Unkown Value\t");
						break;
					}
				}
				xlsData.add(rowData);
				System.out.println();
			}
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return xlsData;
	}
	
	public List<String> getXLSHeaders(String filepath) throws FileNotFoundException, IOException
	{
		try (FileInputStream fis = new FileInputStream(filepath);
	             XSSFWorkbook workbook = new XSSFWorkbook(fis))
		{
			XSSFSheet sheet = workbook.getSheetAt(0);
	        Row headerRow = sheet.getRow(0);
	        List<String> headers = new ArrayList<>();
	        for (Cell cell : headerRow)
	        {
	        	headers.add(cell.getStringCellValue().trim());
	        }
	        return headers;
	    }		
	}

	public Map<String, Integer> generatecolumnMapping(List<String> webHeaders, List<String>xlsHeaders)
	{
		Map<String, Integer> mapping = new HashMap<>();
		Map<String, String> headerOverrides = Map.of(	//1st - table 2nd - xls
		        "Status", "Status",
		        "Part Number", "Part Number",
		        "Description", "Description"
		    );
		for (int i = 0; i < webHeaders.size(); i++)
        {
            String header = webHeaders.get(i);
            String xlsHeader = headerOverrides.getOrDefault(header, header);
            int xlsIndex = xlsHeaders.indexOf(xlsHeader);
            if (xlsIndex != -1)
            {
                mapping.put(header, xlsIndex);
            }
        }
        return mapping;
	}
	
	public void compareData(List<List<String>> webtableData, List<List<String>> xlsData, List<String> webHeaders ,Map<String,Integer> columnMapping)
	{
		for (int i = 0; i < webtableData.size() && i<xlsData.size(); i++)
		{
			List<String> webRow = webtableData.get(i);
	        List<String> xlsRow = xlsData.get(i+1);
	        
	        System.out.println("Comparing Row no. "+(i+1)+": ");
	        
	        for (int webindex = 0; webindex < webRow.size(); webindex++)
	        {
	        	String webCell = webRow.get(webindex).trim(); //Trim and normalize web cell

	            // Find the corresponding XLS index
	        	String header = webHeaders.get(webindex);
	            Integer xlsIndex = columnMapping.get(header);
	            
	            if (xlsIndex != null && xlsIndex < xlsRow.size())
	            {
	            	String xlsCell = xlsRow.get(xlsIndex).trim(); //Trim and normalize XLS cell
	                if (normalizeString(webCell).equals(normalizeString(xlsCell)))
	                {
	                	System.out.println("Match: " + webCell);
	                }
	                else
	                {
	                    System.out.println("Mismatch: Web Cell='" + webCell + "', XLS Cell='" + xlsCell + "'");
	                }
	            }
	            else
	            {
	                System.out.println("No matching column in XLS for Web Header: " + header);
	            }
	        }
	        if (webRow.size() > xlsRow.size())
	        {
	            System.out.println("Extra data in Web-table row: " + webRow.subList(xlsRow.size(), webRow.size()));
	        }
	        else if (xlsRow.size() > webRow.size())
	        {
	            System.out.println("Extra data in XLS row: " + xlsRow.subList(webRow.size(), xlsRow.size()));
	        }
	        System.out.println("-x-x-x-x-x-x");
	    }
		if (webtableData.size() > xlsData.size())
		{
	        System.out.println("Extra rows in Table Data: ");
	        for (int i = xlsData.size(); i < webtableData.size(); i++)
	        {
	            System.out.println(webtableData.get(i));
	        }
	    }
		else if (xlsData.size() > webtableData.size())
		{
	        System.out.println("Extra rows in XLS Data: ");
	        for (int i = webtableData.size(); i < xlsData.size(); i++)
	        {
	            System.out.println(xlsData.get(i));
	        }
		}
	 }
	
	public void validatetabledata(By tablelocator, String filepath) throws FileNotFoundException, IOException, InterruptedException
	{
		List<String> webHeaders = getWebTableHeaders(tablelocator);
		List<List<String>> webTableData = getWebTableData(tablelocator);
		
		List<String> xlsHeaders = getXLSHeaders(filepath);
		List<List<String>> xlsData = getXLSdata(filepath);
		
		Map<String, Integer> columnMapping = generatecolumnMapping(webHeaders, xlsHeaders);
		
		compareData(webTableData, xlsData, webHeaders, columnMapping);
	}
	
	public void columnconfiguration() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/inventories");
		Thread.sleep(5000);
		
		String colconfigbtn = inventoryprop.getProperty("inventories_colconfig_btn");
		String[] fields = {"Part Number","PN Type"};
		testColumnVisibilityConfiguration(colconfigbtn, fields, true);
	}
	
	public void columnpositionchange() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/inventories");
	    Thread.sleep(5000);
	    String colconfigbtn = inventoryprop.getProperty("inventories_colconfig_btn");
	    String columnconfigxpath = inventoryprop.getProperty("inventories_colconfig_xpath");
	    testcolumnpositionchange(colconfigbtn, columnconfigxpath, "6M Sales QTY","Condition Code");
	}
	
	// Helper method to normalize strings
	private String normalizeString(String input)
	{
		return input == null ? "" : input.replaceAll("\\s+", " ").toLowerCase().trim();
	}
}