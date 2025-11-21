package automation.PageObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import automation.BaseMethods.Controls;

public class PageObject_Inventories_RepairRecommended extends Controls
{
	private Properties ps;
	public PageObject_Inventories_RepairRecommended(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public void sortinventories() throws InterruptedException
	{
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, true);
	}
	
	public void filterinventories() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/recommended-for-repairs");
		
		Controls.waitforvisibility(driver, By.xpath(repairrecommendedinventoryprop.getProperty("filter_btn")), 50);
//		Controls.clickElement(By.xpath(repairrecommendedinventoryprop.getProperty("filter_btn")));
		
		String[] fields = {"fromdate","todate","pn","lotconsignmentcode","qty","recdate","dynamiccategory","conditioncode",
				"partclassdesc"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(repairrecommendedinventoryprop.getProperty("filter_"+field+"_title_locator")), repairrecommendedinventoryprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(repairrecommendedinventoryprop.getProperty("filter_"+field)), repairrecommendedinventoryprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}
		
		//NOW FILTERING
	/*	String[] attributes = {"pntype","stockvis","atachapter","dynamic","fulldynamic"};
		String[][] filterdatasets = {
				{"rotable"},{"No"},{"23"},{"O"},{"OO"}
		};*/
		
		String[] attributes = {"partclassdesc","lotconsignmentcode"};
		String[][] filterdatasets = {
				{"airframe","fly controls"},{"MSN947 (A330 CONS)"}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = repairrecommendedinventoryprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(repairrecommendedinventoryprop.getProperty("column_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(repairrecommendedinventoryprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false,false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(repairrecommendedinventoryprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			    );

	}
	
	public List<List<String>> getWebTableData(By tablelocator)
	{
		WebElement table = driver.findElement(By.xpath(repairrecommendedinventoryprop.getProperty("tablelocator")));
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
		Controls.clickElement(By.xpath(repairrecommendedinventoryprop.getProperty("export_btn")));
		filepath = repairrecommendedinventoryprop.getProperty("export_location");
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
	        List<String> xlsRow = xlsData.get(i);
	        
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
		driver.get("https://qa.aerfinity.acumenaviation.in/inventory/slow-moving");
		Thread.sleep(5000);
		
		String colconfigbtn = repairrecommendedinventoryprop.getProperty("inventories_colconfig_btn");
		String[] fields = {"Application Master","Lot Consignment Code","Part Number","Part Class Description", "Condition Code","Dynamic Category","Rec Date","12M PN QTY"};
		testColumnVisibilityConfiguration(colconfigbtn, fields, true);
	}
	
	// Helper method to normalize strings
	private String normalizeString(String input)
	{
		return input == null ? "" : input.replaceAll("\\s+", " ").toLowerCase().trim();
	}
}