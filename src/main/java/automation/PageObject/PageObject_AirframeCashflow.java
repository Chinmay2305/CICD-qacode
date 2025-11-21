package automation.PageObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.Box;
import java.io.File;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import automation.BaseMethods.Controls;

public class PageObject_AirframeCashflow extends Controls
{
	private WebDriver driver;
	private PageObject_Projects po;
	private static final DataFormatter formatter = new DataFormatter();
	//String filename = "C:\\Users\\DELL\\Downloads\\Acumen\\IMP XLS Files\\XLS files by Girish for testing - Slack\\COST MODEL FILE - MSN 35086 - B737-700 - Airframe Model.xls";
	String filename = "C:\\Users\\DELL\\Downloads\\Acumen\\IMP XLS Files\\CASHFLOW_AIRFRAME.xls";
	
	public PageObject_AirframeCashflow(WebDriver driver)
	{
		this.driver=driver;
		this.po = new PageObject_Projects(driver);
	}
	
	//CASHFLOW SETUP METHOD
	public void cashflowsetup() throws InterruptedException, IOException
	{
	//	driver.get("https://qa.aerfinity.acumenaviation.in/projects/ewt7iiNQigRKhOx");
	//	Controls.waitforvisibility(driver,By.xpath(cashflowprop.getProperty("firstasset_view_icon")),10);
	//	Controls.waitforvisibility(driver,By.xpath(cashflowprop.getProperty("cashflow_btn")),10);
		Thread.sleep(2000);
		
		FileInputStream fis = new FileInputStream(filename);
		HSSFWorkbook w = new HSSFWorkbook(fis);
		HSSFSheet s = w.getSheet("Cashflow");
		HSSFFormulaEvaluator evaluator = w.getCreationHelper().createFormulaEvaluator();
		DecimalFormat df = new DecimalFormat("#,##0.00"); // Format like website
		Date date = s.getRow(5).getCell(3).getDateCellValue();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Controls.select_date_in_calendar(By.xpath(cashflowprop.getProperty("purchase_date")), sdf.format(date));
		
		//FIRST - THE SALES AND REPAIRS TABLE
		int boxindex1=1;
		for(int i=8;i<=14;i++)
		{
			HSSFRow r = s.getRow(i);
			for(int j=3;j<=8;j++)
			{
				HSSFCell c = r.getCell(j);
				String input="";
				
				if (c != null) {
				    CellValue cellValue = evaluator.evaluate(c);
				    if (cellValue != null && cellValue.getCellType() == CellType.NUMERIC) {
		                double num = cellValue.getNumberValue();
		                if (num == Math.floor(num)) {
		                    input = String.valueOf((int) num); // whole number → int
		                } else {
		                    input = String.valueOf(num);       // decimal allowed
		                }
		            }
				}
				Controls.editText(By.xpath(cashflowprop.getProperty("box"+boxindex1)), input);
				boxindex1++;
			}
		}
		
		//SECOND - THE AIRFRAME ANNUAL PROFILE TABLE
		int boxindex2=1;
		for(int i=17;i<=19;i++)
		{
			HSSFRow r = s.getRow(i);
			for(int j=3;j<=4;j++)
			{
				HSSFCell c = r.getCell(j);
				String input="";
						
				if (c != null)
				{
					CellValue cellValue = evaluator.evaluate(c);
					if (cellValue != null && cellValue.getCellType() == CellType.NUMERIC)
					{
						double num = cellValue.getNumberValue();
						String format = c.getCellStyle().getDataFormatString();
				        if (format.contains("%"))
				        {
				            // Excel stores 71% as 0.71 → scale back
				            input = String.valueOf((int) Math.round(num * 100));
				        }
				        else
				        {
				        	// Normal number
				            if (num == Math.floor(num))
				            {
				                input = String.valueOf((int) num);
				            }
				            else
				            {
				                input = String.valueOf(num);
				            }
				        }
				    }
				}
				Controls.editText(By.xpath(cashflowprop.getProperty("airframebox"+boxindex2)), input);
				boxindex2++;
			}
		}
		
		//THIRD - THE SALES TABLE
		int boxindex3=1;
		for(int i=23;i<=29;i++)
		{
			HSSFRow r = s.getRow(i);
			for(int j=4;j<=5;j++)
			{
				HSSFCell c = r.getCell(j);
				String input="";
								
				if (c != null)
				{
					CellValue cellValue = evaluator.evaluate(c);
				    if (cellValue != null && cellValue.getCellType() == CellType.NUMERIC)
				    {
		                double num = cellValue.getNumberValue();
		                DecimalFormat df1 = new DecimalFormat("0.00");
		                input = df1.format(num);
		            }
				}
				Controls.editText(By.xpath(cashflowprop.getProperty("salesbox"+boxindex3)), input);
				boxindex3++;
			}
		}
		
		//FOURTH - THE COSTS TABLE
		for (int i = 35, rowIndex = 1; i <= 47; i++, rowIndex++) {  
		    HSSFRow r = s.getRow(i);

		    // Read Excel columns in the order they appear
		    String startMonth = getCellValueAsString(r.getCell(3));  // START MONTH is Excel col D (index 3)
		    String numMonths  = getCellValueAsString(r.getCell(4));  // #MONTHS is Excel col E (index 4)
		    String gap        = getCellValueAsString(r.getCell(5));  // GAP is Excel col F (index 5)
		    String cost       = getCostCellValue(r.getCell(6), evaluator);  // COST is Excel col G (index 6)

		    // Now map to Web table (notice reversed order for first 2 cols)
		    String monthsXpath     = cashflowprop.getProperty("monthsbox" + (34 + rowIndex));     // Web col1 → Excel #MONTHS
		    String startMonthXpath = cashflowprop.getProperty("startmonthbox" + (34 + rowIndex)); // Web col2 → Excel START MONTH
		    String gapXpath        = cashflowprop.getProperty("gapbox" + (34 + rowIndex));
		    String costXpath       = cashflowprop.getProperty("costsbox" + (34 + rowIndex));

		    // Fill into UI
		    Controls.editText(By.xpath(monthsXpath), numMonths);      
		    Controls.editText(By.xpath(startMonthXpath), startMonth); 
		    Controls.editText(By.xpath(gapXpath), gap);
		    Controls.editText(By.xpath(costXpath), cost);
		}
		
		//MOVING AHEAD TO GENERATING FORECAST
		WebElement forecastBtn = driver.findElement(By.xpath(cashflowprop.getProperty("generateforecast_btn")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", forecastBtn);
		Thread.sleep(1000); // small wait for UI animations
		forecastBtn.click();
	}
	
	//CASHFLOW VIEW METHOD
	public void cashflowview() throws IOException, InterruptedException
	{
		FileInputStream fis = new FileInputStream(filename);
		HSSFWorkbook w = new HSSFWorkbook(fis);
		HSSFSheet s = w.getSheet("Cashflow");
		HSSFFormulaEvaluator evaluator = w.getCreationHelper().createFormulaEvaluator();
		DecimalFormat df = new DecimalFormat("#,##0.00"); // Format like website

		// Excel columns P=15, Q=16, R=17, S=18 (0-based index)
		int startCol = 15; // P
		int monthstocheck = 1;	//have to change here only
		int endCol = startCol + monthstocheck - 1;
		int revenuestartRow = 13; // Excel row index for first revenue row of data
		int revenueendRow = 20;   // Excel row index for last revenue row of data
		int repairsstartRow = 22; // Excel row index for first repairs row of data
		int repairsendRow = 28;   // Excel row index for last repairs row of data

		/**********REVENUE**************/
		//REVENUE-1st column
		for (int i = 14, rowIndex = 1; i <= 20; i++, rowIndex++)
		{
		    HSSFRow r = s.getRow(i);
		    HSSFCell c = r.getCell(13);

		    String excelValue = "";
	        if (c != null) {
	            CellValue cellValue = evaluator.evaluate(c);
	            if (cellValue != null) {
	                switch (cellValue.getCellType()) {
	                    case STRING:
	                        excelValue = cellValue.getStringValue().trim();
	                        break;
	                    case NUMERIC:
	                        excelValue = df.format(cellValue.getNumberValue());
	                        break;
	                    case BOOLEAN:
	                        excelValue = String.valueOf(cellValue.getBooleanValue()).trim();
	                        break;
	                    default:
	                        excelValue = "";
	                }
	            }
	        }
	        
	        Thread.sleep(5000);

	     // Clean web text
	        String webText = driver.findElement(By.xpath(cashflowprop.getProperty("revenue_col1-" + rowIndex))).getText().trim().replace("$", "").replace(",", "").trim();
	     // Clean excel text
	        String cleanExcel = excelValue.replace("$", "").replace(",", "").trim();

	     // Compare numerically
	        boolean match = false;
	        try {
	            double webVal = Double.parseDouble(webText);
	            double excelVal = Double.parseDouble(cleanExcel);
	            match = Math.abs(webVal - excelVal) < 0.01; // allow rounding tolerance
	        } catch (NumberFormatException e) {
	            match = webText.equals(cleanExcel);
	        }

	        if (match) {
	            System.out.println("Revenue Column " + rowIndex + " is correct. Excel: " + excelValue + " | Web: " + webText);
	        } else {
	            System.out.println("Revenue Column " + rowIndex + " is not correct. Excel: " + excelValue + " | Web: " + webText);
	        }
		}
		
		//REVENUE- Loop over 'n' months [n is taken from monthstocheck]		
		for (int colIndex = startCol, monthIndex = 1; colIndex <= endCol; colIndex++, monthIndex++)
		{
		    // Loop over rows (Excel 14 → 21, tr[1] → tr[8])
		    for (int rowIndex = 0; rowIndex <= (revenueendRow - revenuestartRow); rowIndex++)
		    {
		        HSSFRow r = s.getRow(revenuestartRow + rowIndex);
		        HSSFCell c = r.getCell(colIndex);
		        String excelValue = "";
		        Double excelNumeric = null;

		        if (c != null) {
		            CellValue cellValue = evaluator.evaluate(c);
		            if (cellValue != null) {
		                switch (cellValue.getCellType()) {
		                    case STRING:
		                        excelValue = cellValue.getStringValue().trim();
		                        break;
		                    case NUMERIC:
		                        excelNumeric = cellValue.getNumberValue();  // keep numeric for accurate compare
		                        excelValue = df.format(excelNumeric);       // formatted for logs
		                        break;
		                    case BOOLEAN:
		                        excelValue = String.valueOf(cellValue.getBooleanValue()).trim();
		                        break;
		                    default:
		                        excelValue = "";
		                }
		            }
		        }

		        String cleanExcel = excelValue.replace("$", "").replace(",", "").replace(" ", "").trim();
		        if (cleanExcel.isEmpty()) cleanExcel = "0";

		        Thread.sleep(2000);

		        // --- Get Web Value ---
		        int webCol = monthIndex + 2;      // Because M1=td[3], M2=td[4]...
		        int webRow = rowIndex + 1;        // Excel row14 → tr[1], etc.

		        String xpath = "//tr[" + webRow + "]/td[" + webCol + "]";
		        String webText = driver.findElement(By.xpath(xpath)).getText().trim();

		        // Normalize web text
		        webText = webText.replace("$", "")
		                         .replace(",", "")
		                         .replace("\u00A0", "")   // remove non-breaking space
		                         .replace(" ", "")
		                         .replaceAll("^-(\\s+)", "-") // fix "- 123" → "-123"
		                         .trim();
		        if (webText.isEmpty()) webText = "0";

		        // --- Compare ---
		        boolean match = false;
		        try {
		            double webVal = Double.parseDouble(webText);
		            double excelVal = (excelNumeric != null) ? excelNumeric : Double.parseDouble(cleanExcel);
		            match = Math.abs(webVal - excelVal) < 0.01; // allow rounding tolerance
		        } catch (NumberFormatException e) {
		            match = webText.equals(cleanExcel);
		        }

		        if (match) {
		            System.out.println("Revenue Month " + monthIndex + " Row " + webRow
		                    + " ✅ Match | Excel: " + excelValue + " | Web: " + webText);
		        } else {
		            System.out.println("Revenue Month " + monthIndex + " Row " + webRow
		                    + " ❌ Mismatch | Excel: " + excelValue + " | Web: " + webText);
		        }
		    }
		}
		
		//REVENUE total check
		double revenueexcelTotal = 0.0;
		for (int rowIndex = revenuestartRow; rowIndex <= revenueendRow; rowIndex++) {
		    HSSFRow row = s.getRow(rowIndex);
		    HSSFCell cell = row.getCell(13);

		    if (cell != null) {
		        CellValue cellValue = evaluator.evaluate(cell);
		        if (cellValue != null) {
		            switch (cellValue.getCellType()) {
		                case NUMERIC:
		                	revenueexcelTotal += cellValue.getNumberValue();
		                    break;
		                case STRING:
		                    try {
		                        String clean = cellValue.getStringValue()
		                                .replace("$", "")
		                                .replace(",", "")
		                                .replace(" ", "")
		                                .trim();
		                        revenueexcelTotal += Double.parseDouble(clean);
		                    } catch (Exception e) {
		                        // ignore non-numeric string
		                    }
		                    break;
		                default:
		                    // ignore blanks / other types
		            }
		        }
		    }
		}

		// --- Get REVENUE Web Value ---
		String webRevenuetotal = driver.findElement(By.xpath("//tr[9]/td[2]"))
		        .getText().trim()
		        .replace("$", "")
		        .replace(",", "")
		        .replace("\u00A0", "")
		        .replace(" ", "")
		        .replaceAll("^-(\\s+)", "-");

		if (webRevenuetotal.isEmpty()) webRevenuetotal = "0";
		double webTotal = 0.0;
		try {
		    webTotal = Double.parseDouble(webRevenuetotal);
		} catch (NumberFormatException e) {
		    System.out.println("⚠️ Revenue Web total not numeric: " + webRevenuetotal);
		}

		// --- Compare ---
		boolean match = Math.abs(revenueexcelTotal - webTotal) < 0.01;

		if (match) {
		    System.out.println("✅ Revenue Total Match | Excel: " + df.format(revenueexcelTotal) + " | Web: " + webRevenuetotal);
		} else {
		    System.out.println("❌ Revenue Total Mismatch | Excel: " + df.format(revenueexcelTotal) + " | Web: " + webRevenuetotal);
		}
		
		/**********REPAIRS**************/
		//REPAIRS-1st column
		boolean match1=false;
		for (int i = 22, rowIndex = 1; i <= 28; i++, rowIndex++)
		{
			HSSFRow r = s.getRow(i);
			HSSFCell c = r.getCell(13);

			String excelValue = "";
			if (c != null)
			{
				CellValue cellValue = evaluator.evaluate(c);
			    if (cellValue != null)
			    {
			    	switch (cellValue.getCellType())
			    	{
			    	case STRING:
			    		excelValue = cellValue.getStringValue().trim();
			            break;
			        case NUMERIC:
			        	excelValue = df.format(cellValue.getNumberValue());
			            break;
			        case BOOLEAN:
			        	excelValue = String.valueOf(cellValue.getBooleanValue()).trim();
			        	break;
			        default:
			        	excelValue = "";
			        	}
			    	}
			    }
			
			// Clean web text
			String webText = driver.findElement(By.xpath(cashflowprop.getProperty("repairs_col1-" + rowIndex)))
                    .getText().trim()
                    .replace("$", "")
                    .replace(",", "")
                    .replace(" ", "");
			// Clean excel text
			String cleanExcel = excelValue.replace("$", "")
                    .replace(",", "")
                    .replace(" ", "")
                    .trim();
			// Handle empty Excel cells as 0
			if (cleanExcel.isEmpty()) {
			    cleanExcel = "0";
			}

			// Compare numerically
			try {
				double webVal = Double.parseDouble(webText);
			    double excelVal = Double.parseDouble(cleanExcel);
			    match1 = Math.abs(webVal - excelVal) < 0.01; // allow rounding tolerance
			    }
			catch (NumberFormatException e)
			{
				match1 = webText.equals(cleanExcel);
			}

			if (match1) {
				System.out.println("Repairs Column " + rowIndex + " is correct. Excel: " + excelValue + " | Web: " + webText);
			} else {
				System.out.println("Repairs Column " + rowIndex + " is not correct. Excel: " + excelValue + " | Web: " + webText);
			}
		}
		
		//REPAIRS- Loop over 'n' months [n is taken from monthstocheck]
		int repairsWebStartRow = 12; // web table repairs start from tr[12]
		
		for (int colIndex = startCol, monthIndex = 1; colIndex <= endCol; colIndex++, monthIndex++) {
		    for (int rowIndex = 0; rowIndex <= (repairsendRow - repairsstartRow); rowIndex++) {
		        HSSFRow r = s.getRow(repairsstartRow + rowIndex);
		        HSSFCell c = r.getCell(colIndex);

		        String excelValue = "";
		        Double excelNumeric = null;

		        if (c != null) {
		            CellValue cellValue = evaluator.evaluate(c);
		            if (cellValue != null) {
		                switch (cellValue.getCellType()) {
		                    case STRING:
		                        excelValue = cellValue.getStringValue().trim();
		                        break;
		                    case NUMERIC:
		                        excelNumeric = cellValue.getNumberValue();
		                        excelValue = df.format(excelNumeric);
		                        break;
		                    case BOOLEAN:
		                        excelValue = String.valueOf(cellValue.getBooleanValue()).trim();
		                        break;
		                    default:
		                        excelValue = "";
		                }
		            }
		        }

		        String cleanExcel = excelValue.replace("$", "").replace(",", "").replace(" ", "").trim();
		        if (cleanExcel.isEmpty()) cleanExcel = "0";

		        Thread.sleep(2000);

		        // --- Get Web Value ---
		        int webCol = monthIndex + 2;              // M1=td[3], M2=td[4]...
		        int webRow = repairsWebStartRow + rowIndex; // offset to start from tr[12]

		        String xpath = "//tr[" + webRow + "]/td[" + webCol + "]";
		        String webText = driver.findElement(By.xpath(xpath)).getText().trim();

		        // Normalize web text
		        webText = webText.replace("$", "")
		                         .replace(",", "")
		                         .replace("\u00A0", "")
		                         .replace(" ", "")
		                         .replaceAll("^-(\\s+)", "-")
		                         .trim();
		        if (webText.isEmpty()) webText = "0";

		        // --- Compare ---
		        try {
		            double webVal = Double.parseDouble(webText);
		            double excelVal = (excelNumeric != null) ? excelNumeric : Double.parseDouble(cleanExcel);
		            match1 = Math.abs(webVal - excelVal) < 0.01;
		        } catch (NumberFormatException e) {
		            match1 = webText.equals(cleanExcel);
		        }

		        if (match1) {
		            System.out.println("Repairs Month " + monthIndex + " Row " + (rowIndex+1)
		                    + " ✅ Match | Excel: " + excelValue + " | Web: " + webText);
		        } else {
		            System.out.println("Repairs Month " + monthIndex + " Row " + (rowIndex+1)
		                    + " ❌ Mismatch | Excel: " + excelValue + " | Web: " + webText);
		        }
		    }
		}
		
		//REPAIRS total check
		double repairsexcelTotal = 0.0;
		for (int rowIndex = repairsstartRow; rowIndex <= repairsendRow; rowIndex++)
		{
			HSSFRow row = s.getRow(rowIndex);
			HSSFCell cell = row.getCell(13);

			if (cell != null)
			{
				CellValue cellValue = evaluator.evaluate(cell);
				if (cellValue != null)
				{
					switch (cellValue.getCellType())
					{
					case NUMERIC:
						repairsexcelTotal += cellValue.getNumberValue();
				        break;
				    case STRING:
				    	try
				    	{
				    		String clean = cellValue.getStringValue().replace("$", "").replace(",", "").replace(" ", "").trim();
				    		repairsexcelTotal += Double.parseDouble(clean);
				    	}
				    	catch(Exception e)
				    	{
				    		// ignore non-numeric string
				        }
				    	break;
				        default:
				        // ignore blanks / other types
				     }
				}
			}
		}

		// --- Get REPAIRS Web Value ---
		String webRepairstotal = driver.findElement(By.xpath("//tr[19]/td[2]")).getText().trim().replace("$", "").replace(",", "").replace("\u00A0", "").replace(" ", "").replaceAll("^-(\\s+)", "-");

		if (webRepairstotal.isEmpty())
			webRepairstotal = "0";
		double webTotal1 = 0.0;
		try
		{
			webTotal1 = Double.parseDouble(webRepairstotal);
		}
		catch (NumberFormatException e)
		{
			System.out.println("⚠️ Repairs Web total not numeric: " + webRepairstotal);
		}

		// --- Compare ---
		boolean match2 = Math.abs(repairsexcelTotal - webTotal1) < 0.01;
		if (match2)
		{
			System.out.println("✅ Repairs Total Match | Excel: " + df.format(repairsexcelTotal) + " | Web: " + webRepairstotal);
		}
		else
		{
			System.out.println("❌ Repairs Total Mismatch | Excel: " + df.format(repairsexcelTotal) + " | Web: " + webRepairstotal);
		}
		
		/*NOW WE WILL DO SOME ACTUAL CALCULATION FOR FURTHER NUMBERS BECAUSE THEY ARE TOTALS, CUMULATIVE DIFFERENCES, ETC*/
		/*SO, THIS WILL VERIFY 3 THINGS - EXCEL VALUE, WEB VALUE AND ACTUAL TOTAL*/
		/**********TOTAL SALES-REPAIRS DIFFERENCE**************/
		double calcDiff = revenueexcelTotal + repairsexcelTotal;

		// Excel Diff (N30)
		HSSFCell diffCell = s.getRow(29).getCell(13); // col N
		double excelDiff = 0.0;
		// Safely evaluate Excel cell
		if (diffCell != null) {
		    CellValue diffValue = evaluator.evaluate(diffCell);
		    if (diffValue != null && diffValue.getCellType() == CellType.NUMERIC) {
		        excelDiff = diffValue.getNumberValue();
		    } else {
		        // fallback to cached value
		        try {
		            excelDiff = diffCell.getNumericCellValue();
		        } catch (Exception e) {
		            System.out.println("⚠️ Could not fetch Excel Diff, cell type = " + diffCell.getCellType());
		        }
		    }
		}
		
		// Web Diff (//tr[20]/td[2])
		String webDiffText = driver.findElement(By.xpath("//tr[20]/td[2]"))
		        .getText().trim()
		        .replace("$", "")
		        .replace(",", "")
		        .replace("\u00A0", "")
		        .replace(" ", "")
		        .replaceAll("^-(\\s+)", "-");
		if (webDiffText.isEmpty()) webDiffText = "0";
		double webDiff = 0.0;
		try {
		    webDiff = Double.parseDouble(webDiffText);
		} catch (NumberFormatException e) {
		    System.out.println("⚠️ Web diff not numeric: " + webDiffText);
		}

		// Compare all three
		boolean matchExcelVsCalc = Math.abs(excelDiff - calcDiff) < 0.01;
		boolean matchExcelVsWeb  = Math.abs(excelDiff - webDiff) < 0.01;
		boolean matchCalcVsWeb   = Math.abs(calcDiff - webDiff) < 0.01;

		System.out.println("Excel Diff (N30)          = " + df.format(excelDiff));	//THIS FETCHES THE DIFFERENCE FROM XLS
		System.out.println("Web Diff (tr20 td2)       = " + webDiffText);			//THIS FETCHES THE DIFFERENCE FROM WEBSITE
		System.out.println("Calc Diff (Sales-Repairs) = " + df.format(calcDiff));	//THIS CALCULATES THE DIFFERENCE TO VERIFY ABOVE 2

		if (matchExcelVsCalc && matchExcelVsWeb && matchCalcVsWeb) {
		    System.out.println("✅ Sales - Repairs Difference matches in Excel, Web, and Calc");
		} else {
		    System.out.println("❌ Mismatch in Sales - Repairs Difference across Excel/Web/Calc");
		}
		
		Controls.clickElement(By.xpath(cashflowprop.getProperty("viewebitda_btn")));
	}	
		/**********MONTHLY TOTAL & CUMULATIVE MONTHLY TOTAL**************/
		// Assuming: Q = col 16 (0-based index = 16), row 29 (Monthly Total), row 30 (Cumulative)
		// Adjust indices depending on your sheet
/*		int cumulativestartCol = startCol+1; // Q
		int cumulativeendCol = cumulativestartCol+monthstocheck-1;
		int monthlyRowIdx = 29; // Row 30 in Excel (0-based = 29)
		int cumulRowIdx   = 30; // Row 31 in Excel (0-based = 30)

		for (int col = startCol; col <= endCol; col++) {

		    // --- Excel Monthly Total ---
			HSSFCell excelMonthlyCell = s.getRow(monthlyRowIdx).getCell(col);
			HSSFCell excelCumulCell   = s.getRow(cumulRowIdx).getCell(col);

			double excelMonthly = getNumericValue(excelMonthlyCell, evaluator);
			double excelCumul   = getNumericValue(excelCumulCell, evaluator);

//			double excelMonthly = getNumericValue(excelMonthlyCell, evaluator);

//			HSSFCell excelCumulCell = s.getRow(cumulRowIdx).getCell(col);
//			double excelCumul = getNumericValue(excelCumulCell, evaluator);

//			double excelMonthly = getNumericValue(excelMonthlyCell, evaluator);
//			double excelCumul = getNumericValue(excelCumulCell, evaluator);


		    // --- Excel Cumulative ---
		    if (excelCumulCell != null) {
		        CellValue cv = evaluator.evaluate(excelCumulCell);
		        if (cv != null && cv.getCellType() == CellType.NUMERIC) {
		            excelCumul = cv.getNumberValue();
		        } else {
		            excelCumul = excelCumulCell.getNumericCellValue();
		        }
		    }

		    // --- Web Monthly Total ---
		    String webMonthlyXpath = "//tr[22]/td[" + (col - startCol + 3) + "]";
		    String webMonthlyText = driver.findElement(By.xpath(webMonthlyXpath)).getText()
		            .replace("$", "").replace(",", "").trim();
		    double webMonthly = webMonthlyText.isEmpty() ? 0.0 : Double.parseDouble(webMonthlyText);

		    // --- Web Cumulative ---
		    String webCumulXpath = "//tr[23]/td[" + (col - startCol + 3) + "]";
		    String webCumulText = driver.findElement(By.xpath(webCumulXpath)).getText()
		            .replace("$", "").replace(",", "").trim();
		    double webCumul = webCumulText.isEmpty() ? 0.0 : Double.parseDouble(webCumulText);

		    // --- Calc Monthly (Revenue + Repairs) ---
		    // If you already have revenueexcel[col] and repairsexcel[col] arrays
		 // Arrays to store monthly totals
		    double[] revenueexcel = new double[endCol - startCol + 1];
		    double[] repairsexcel = new double[endCol - startCol + 1];
		    double calcMonthly = revenueexcel[col - startCol] + repairsexcel[col - startCol];

		    // --- Calc Cumulative ---
		    double calcCumul = (col == startCol) ? calcMonthly : calcMonthly + /* previous calcCumul
		    // TODO: keep track in an array or variable per iteration

		    // --- Compare ---
		    boolean matchMonthlyExcelWeb  = Math.abs(excelMonthly - webMonthly) < 0.01;
		    boolean matchMonthlyExcelCalc = Math.abs(excelMonthly - calcMonthly) < 0.01;
		    boolean matchCumulExcelWeb    = Math.abs(excelCumul - webCumul) < 0.01;
		    boolean matchCumulExcelCalc   = Math.abs(excelCumul - calcCumul) < 0.01;

		    System.out.println("Month col " + col);
		    System.out.println("  Excel Monthly = " + df.format(excelMonthly));
		    System.out.println("  Web Monthly   = " + df.format(webMonthly));
		    System.out.println("  Calc Monthly  = " + df.format(calcMonthly));
		    System.out.println("  Excel Cumul   = " + df.format(excelCumul));
		    System.out.println("  Web Cumul     = " + df.format(webCumul));
		    System.out.println("  Calc Cumul    = " + df.format(calcCumul));

		    if (matchMonthlyExcelWeb && matchMonthlyExcelCalc && matchCumulExcelWeb && matchCumulExcelCalc) {
		        System.out.println("✅ Match across Excel/Web/Calc");
		    } else {
		        System.out.println("❌ Mismatch found");
		    }
		}
	}*/
	
	//HELPER METHODS
	private String getCostCellValue(HSSFCell cell, FormulaEvaluator evaluator)
	{
		if (cell == null) return "";

	    String value = "";

	    switch (cell.getCellType()) {
	        case STRING:
	            value = cell.getStringCellValue().trim();
	            break;

	        case NUMERIC:
	            double num = cell.getNumericCellValue();
	            // force integer if it's a whole number
	            if (num == Math.floor(num)) {
	                value = String.valueOf((long) num);
	            } else {
	                value = String.valueOf(num);
	            }
	            break;

	        case FORMULA:
	            CellValue cellValue = evaluator.evaluate(cell);
	            if (cellValue != null) {
	                switch (cellValue.getCellType()) {
	                    case STRING:
	                        value = cellValue.getStringValue().trim();
	                        break;
	                    case NUMERIC:
	                        double fnum = cellValue.getNumberValue();
	                        if (fnum == Math.floor(fnum)) {
	                            value = String.valueOf((long) fnum);
	                        } else {
	                            value = String.valueOf(fnum);
	                        }
	                        break;
	                    default:
	                        value = "";
	                }
	            }
	            break;

	        default:
	            value = "";
	    }

	    // cleanup
	    value = value.replace("$", "")
	                 .replace(",", "")
	                 .trim();

	    // treat "-" or empty as no value
	    if (value.equals("-") || value.isEmpty()) {
	        return "";
	    }

	    // remove leading negative (UI adds automatically)
	    if (value.startsWith("-")) {
	        value = value.substring(1);
	    }

	    return value;
	}	
	private String getCellValueAsString(HSSFCell cell) {
	    if (cell == null) return "";
	    switch (cell.getCellType()) {
	        case STRING: return cell.getStringCellValue();
	        case NUMERIC:
	            double num = cell.getNumericCellValue();
	            if (num == Math.floor(num)) {
	                return String.valueOf((int) num); // remove .0
	            } else {
	                return String.valueOf(num);
	            }
	        case FORMULA:
	            return cell.getCellFormula();
	        default:
	            return "";
	    }
	}
	private double getNumericValue(HSSFCell cell, HSSFFormulaEvaluator evaluator) {
	    if (cell == null) return 0.0;

	    CellValue cv = evaluator.evaluate(cell);
	    if (cv != null) {
	        switch (cv.getCellType()) {
	            case NUMERIC:
	                return cv.getNumberValue();
	            case STRING:
	                try {
	                    String clean = cv.getStringValue()
	                            .replace("$", "")
	                            .replace(",", "")
	                            .replace(" ", "")
	                            .trim();
	                    return clean.isEmpty() ? 0.0 : Double.parseDouble(clean);
	                } catch (Exception e) {
	                    return 0.0;
	                }
	            case BOOLEAN:
	                return cv.getBooleanValue() ? 1.0 : 0.0;
	            default:
	                return 0.0;
	        }
	    }

	    // fallback: direct type check
	    switch (cell.getCellType()) {
	        case NUMERIC:
	            return cell.getNumericCellValue();
	        case STRING:
	            try {
	                String clean = cell.getStringCellValue()
	                        .replace("$", "")
	                        .replace(",", "")
	                        .replace(" ", "")
	                        .trim();
	                return clean.isEmpty() ? 0.0 : Double.parseDouble(clean);
	            } catch (Exception e) {
	                return 0.0;
	            }
	        default:
	            return 0.0;
	    }
	}
}