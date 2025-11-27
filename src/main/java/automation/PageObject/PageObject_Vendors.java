package automation.PageObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.bidi.browsingcontext.UserPromptClosed;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;

import automation.BaseMethods.Controls;

public class PageObject_Vendors extends Controls
{
	private Properties pv;
	PageObject_Analytics pa = new PageObject_Analytics(driver);
	public PageObject_Vendors(WebDriver driver)
	{
		this.driver = driver;
	}

	public void sortvendors() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/vendors");
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, false);
	}
	
	public int singlefilterforanalytics() throws InterruptedException
	{
		System.out.println("FILTER FROM ANALYTICS PART: "+pa.vendordata);
		
		Controls.get(vendorsprop.getProperty("vendors_url"));
		Thread.sleep(5000);
		Controls.clickElement(By.xpath(vendorsprop.getProperty("filter_btn")));
		
		String[] fields = {"class","region"};
		
		//NOW FILTERING
		String[] attributes = {pa.vendorfield};
		String[][] filterdatasets = {
				{pa.vendordata}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = vendorsprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(vendorsprop.getProperty("column_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(vendorsprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(vendorsprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			);
		
		WebElement rowCount = driver.findElement(By.xpath("(//main[contains(@class,'MuiBox-root')])[2]/div[2]/div/div[2]/div[3]/span"));
		String countText = rowCount.getText().replace(",", "").trim();  // remove commas
		return Integer.parseInt(countText);
	}
	
	public void multiplefilter() throws InterruptedException
	{
		Controls.get(vendorsprop.getProperty("vendors_url"));
		Thread.sleep(5000);
		Controls.clickElement(By.xpath(vendorsprop.getProperty("filter_btn")));
		
		String[] fields = {"fromdate","todate","class","country","region","createdby","company","code","email"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(vendorsprop.getProperty("filter_"+field+"_title_locator")), vendorsprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(vendorsprop.getProperty("filter_"+field)), vendorsprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}
		
		//NOW FILTERING
		String[] attributes = {"class", "country","region","createdby"};
		String[][] filterdatasets = {
				{"BRKR"},{"Mexico"},{"North America"},{"Nadya"}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = vendorsprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(vendorsprop.getProperty("column_"+attributes[i]));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(vendorsprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false, false, false, false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(vendorsprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			);
	}
	
	public List<List<String>> getWebTableData(By tablelocator)
	{
		WebElement table = driver.findElement(By.xpath(vendorsprop.getProperty("tablelocator")));
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
		Controls.clickElement(By.xpath(vendorsprop.getProperty("export_btn")));
		String exportPathFromProps = vendorsprop.getProperty("export_location");
		if (filepath == null || filepath.trim().isEmpty())
		{
			filepath = exportPathFromProps;   // Only fallback
        }
		
		System.out.println("File path: " + filepath);
		File file = new File(filepath);
		int attempts = 0;
		while (!file.exists() && attempts < 10)
		{
			Thread.sleep(1000);
			attempts++;
		}
		
		if(!file.exists())
		{
			System.out.println("File does not exist at the specified location.");
		}

		try(FileInputStream fis = new FileInputStream(file);
         XSSFWorkbook workbook = new XSSFWorkbook(fis))
		{
			XSSFSheet sheet = workbook.getSheetAt(0);

			for(Row row : sheet)
			{
				List<String> rowData = new ArrayList<>();
				
				for (org.apache.poi.ss.usermodel.Cell cell : row)
				{
					switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue().trim());
                        break;
                    case NUMERIC:
                        rowData.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case BOOLEAN:
                        rowData.add(String.valueOf(cell.getBooleanCellValue()));
                        break;
                    default:
                        rowData.add("");
                        break;
                }
            }
            xlsData.add(rowData);
        }
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
		        "Company Name", "Company Name",
		        "Code", "Code",
		        "Contact Name", "Contact Name",
		        "Email","Email",
		        "Region","Region",
		        "KYC","KYC",
		        "Class","Class",
		        "Country","Country",
		        "Created By","Created By",
		        "Created At","Created At"
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
	
	// Helper method to normalize strings
	private String normalizeString(String input)
	{
		return input == null ? "" : input.replaceAll("\\s+", " ").toLowerCase().trim();
	}

	public void columnconfiguration() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/vendors");
		Thread.sleep(5000);
		
		String colconfigbtn = vendorsprop.getProperty("vendor_colconfig_btn");
		String[] fields = {"KYC","Email","Class"};
		
		testColumnVisibilityConfiguration(colconfigbtn, fields, false);
	}
	
	public void columnpositionchange() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/vendors");
	    Thread.sleep(5000);
	    String colconfigbtn = vendorsprop.getProperty("vendors_colconfig_btn");
	    String columnconfigxpath = vendorsprop.getProperty("vendors_colconfig_xpath");
	    testcolumnpositionchange(colconfigbtn, columnconfigxpath, "Region","Email");
	}
}