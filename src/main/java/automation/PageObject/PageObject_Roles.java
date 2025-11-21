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
import java.util.concurrent.TimeUnit;

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

public class PageObject_Roles extends Controls
{
	private Properties pu;
	public PageObject_Roles(WebDriver driver)
	{
		this.driver = driver;
	}

	public void openadduserform() throws InterruptedException, NoSuchElementException
	{
		Controls.clickElement((By.xpath(rolesprop.getProperty("settings_link"))));
		Thread.sleep(5000);
	    Controls.waitforvisibility(driver, By.xpath(rolesprop.getProperty("add_user_btn")), 5);
	}
	
	public void verifytitlesplaceholders() throws InterruptedException, NoSuchElementException
	{
	    driver.switchTo().activeElement();
		Controls.confirmdata(By.xpath(rolesprop.getProperty("user_title")), "Name *", "Name title");
		Controls.confirmplaceholders(By.xpath(rolesprop.getProperty("user_placeholder")), "Enter Name", "Name");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("email_title")), "Email *", "Email title");
		Controls.confirmplaceholders(By.xpath(rolesprop.getProperty("email_placeholder")), "Enter Email", "Email");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("mobile_title")), "Mobile", "Mobile title");
		Controls.confirmplaceholders(By.xpath(rolesprop.getProperty("mobile_placeholder")), "Enter Mobile", "Mobile");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("designation_title")), "Designation *", "Designation title");
		Controls.confirmplaceholders(By.xpath(rolesprop.getProperty("designation_placeholder")), "Enter Designation", "Designation");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("dept_title")), "Department", "Department title");
		Controls.confirmplaceholders(By.xpath(rolesprop.getProperty("dept_placeholder")), "Enter Department", "Department");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("access_title")), "Access *", "Access title");
		Controls.confirmplaceholders(By.xpath(rolesprop.getProperty("access_placeholder")), "Search Access", "Access");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("status_title")), "Status *", "Status title");
	}
	
	public void adduser() throws InterruptedException, NoSuchElementException
	{
	    Controls.type(By.xpath(rolesprop.getProperty("user_box")), rolesprop.getProperty("user_name"));
	    Controls.type(By.xpath(rolesprop.getProperty("email_box")), rolesprop.getProperty("user_email"));
	    Controls.type(By.xpath(rolesprop.getProperty("mobile_box")), rolesprop.getProperty("user_mobile"));
	    Controls.type(By.xpath(rolesprop.getProperty("designation_box")), rolesprop.getProperty("user_designation"));
	    Controls.type(By.xpath(rolesprop.getProperty("dept_box")), rolesprop.getProperty("user_department"));
	    Controls.dropdown(By.xpath(rolesprop.getProperty("access_box")), rolesprop.getProperty("user_access"));
	    Controls.confirmdata(By.xpath(rolesprop.getProperty("status_box")), rolesprop.getProperty("user_status"), "Default status is Pending");
	    Controls.clickElement(By.xpath(rolesprop.getProperty("add_user_save_btn")));
	}
	
	public void confirmuser() throws InterruptedException, StaleElementReferenceException
	{
		Controls.waitforvisibility(driver, By.xpath(rolesprop.getProperty("first_name")), 10);
		Controls.confirmdata(By.xpath(rolesprop.getProperty("first_name")), rolesprop.getProperty("user_name"), "Name");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("first_dept")), rolesprop.getProperty("user_department"), "Department");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("first_desig")), rolesprop.getProperty("user_designation"), "Designation");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("first_email")), rolesprop.getProperty("user_email"), "Email");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("first_mobile")), rolesprop.getProperty("user_mobile"), "Mobile");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("first_status")), rolesprop.getProperty("user_status"), "Status");
		Controls.confirmdata(By.xpath(rolesprop.getProperty("first_access")), rolesprop.getProperty("user_access"), "Access");
	}
	
	public void sortroles() throws InterruptedException
	{
		driver.get("https://qa.aerfinity.acumenaviation.in/settings/roles");
		List<WebElement> columns = driver.findElements(By.xpath("//table/thead/tr/th"));
		int columnCount = columns.size();
		Thread.sleep(5000);
		Sort(columnCount, true);
	}
	
	public void multiplefilter() throws InterruptedException
	{
		driver.get(rolesprop.getProperty("role_url"));
		Controls.clickElement(By.xpath(rolesprop.getProperty("filter_btn")));
		
		String[] fields = {"name","email","desig","department","access","status"};
		for(String field: fields)
		{
			Controls.confirmdata(By.xpath(rolesprop.getProperty("filter_"+field+"_title_locator")), rolesprop.getProperty("filter_"+field+"_title"), "Filter "+field+" title");
			Controls.confirmplaceholders(By.xpath(rolesprop.getProperty("filter_"+field)), rolesprop.getProperty("filter_"+field+"_placeholder"), "Filter "+field);
		}
		
		//NOW FILTERING
		String[] attributes = {"access", "status"};
		String[][] filterdatasets = {
				{"BAM Approver", "Test"},{"Active"}
		};
		
		String[] filterInputLocators = new String[attributes.length];
		By[] tableColumnLocators = new By[attributes.length];
		
		for(int i=0;i<attributes.length;i++)
		{
			filterInputLocators[i] = rolesprop.getProperty("filter_"+attributes[i]);
			tableColumnLocators[i] = By.xpath(rolesprop.getProperty(attributes[i]+"_column"));
		}
				
		applyVerifyMultipleFilters(
			    By.xpath(rolesprop.getProperty("filter_btn")),
			    filterInputLocators,
			    filterdatasets,
			    new boolean[] {false, false},	//for date fields - false for non-date fields & true for date fields
			    By.xpath(rolesprop.getProperty("filter_apply")),
			    tableColumnLocators,
			    attributes
			);
	}
	
	public List<List<String>> getWebTableData(By tablelocator)
	{
		WebElement table = driver.findElement(By.xpath(rolesprop.getProperty("tablelocator")));
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
		Controls.clickElement(By.xpath(rolesprop.getProperty("export_btn")));
		filepath = rolesprop.getProperty("export_location");
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
						double numericValue = cell.getNumericCellValue();
                        if (numericValue == (long) numericValue) {
                            rowData.add(String.valueOf((long) numericValue)); // Convert to long if no decimal part
                        } else {
                            rowData.add(String.valueOf(numericValue));
                        }
                        System.out.println("From the getXLSData: " + numericValue + "\t");
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
		Map<String, String> headerOverrides = Map.of(
		        "Name", "Role name",//"Table column name","XLS column name"
		        "Profile Type", "Profile type",
		        "Users", "User count",
		        "Created By","Created by",
		        "Updated By","Updated by"
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
}