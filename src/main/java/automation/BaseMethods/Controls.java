package automation.BaseMethods;

import java.io.File;
import org.apache.logging.log4j.Logger;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.logging.log4j.LogManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Controls
{
	public static WebDriver driver;
	public static Logger Logger = null;
	public static Properties loginprop = new Properties();
	public static Properties setupprop = new Properties();
	public static Properties configprop = new Properties();
	
	public static FileInputStream flogin, fsetup, fconfig;
	
	//INITAL SETUP
		@BeforeTest
		public static void setpropertyfile()
		{
			if (driver == null)
			{
				try
				{
					flogin = new FileInputStream(System.getProperty("user.dir") + "//Resources//login.properties");
					loginprop.load(flogin);
					fsetup = new FileInputStream(System.getProperty("user.dir") + "//Resources//setup.properties");
					setupprop.load(fsetup);
					fconfig = new FileInputStream(System.getProperty("user.dir") + "//Resources//config.properties");
					configprop.load(fconfig);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	
		public static void setup(String url)
		{
			driver = new ChromeDriver();
			driver.get(url);
			driver.manage().window().maximize();
		}
		
		public static void get(String url)
		{
			driver.get(url);
		}
		
	//BROWSER STARTUP
	/*	public static void startUP() throws IOException, InterruptedException
		{
			Logger= LogManager.getLogger("devpinoyLogger");
			System.out.println(configprop.getProperty("Browser"));
			if(configprop.getProperty("Browser").equalsIgnoreCase("Firefox"))
			{
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
			}
			
			if(configprop.getProperty("Browser").equalsIgnoreCase("Chrome"))
			{
				WebDriverManager.chromedriver().setup();
		        driver = new ChromeDriver();
			}
			
			if(configprop.getProperty("Browser").equalsIgnoreCase("Edge"))
			{
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
			}
			
			Controls.wait(2000);
			driver.manage().window().maximize();

			if(loginprop.getProperty("Environment").equalsIgnoreCase("Production"))driver.get(configprop.getProperty("ProductionURL"));
			if(loginprop.getProperty("Environment").equalsIgnoreCase("dev"))driver.get(configprop.getProperty("devurl"));
			if(loginprop.getProperty("Environment").equalsIgnoreCase("QA"))driver.get(configprop.getProperty("QAURL"));
			
			driver.manage().deleteAllCookies();
			driver.navigate().refresh();
			
			System.out.println("browser started");
				
		}*/
		
		public static void startUP() throws IOException, InterruptedException
		{
		    Logger = LogManager.getLogger("devpinoyLogger");
		    System.out.println(configprop.getProperty("Browser"));

		    // ⭐ STEP 1 — Create custom download folder inside project
		    String downloadPath = System.getProperty("user.dir") + File.separator + "Downloads";
		    File folder = new File(downloadPath);
		    if (!folder.exists()) {
		        folder.mkdirs();
		    }

		    // ⭐ STEP 2 — Preference settings for download (Chrome only)
		    HashMap<String, Object> prefs = new HashMap<>();
		    prefs.put("download.default_directory", downloadPath);
		    prefs.put("download.prompt_for_download", false);
		    prefs.put("download.directory_upgrade", true);
		    prefs.put("safebrowsing.enabled", true);

		    // ⭐ Browser setup
		    if (configprop.getProperty("Browser").equalsIgnoreCase("Firefox"))
		    {
		        WebDriverManager.firefoxdriver().setup();
		        driver = new FirefoxDriver();  // Firefox has separate handling, so leave as is
		    }

		    if (configprop.getProperty("Browser").equalsIgnoreCase("Chrome"))
		    {
		        WebDriverManager.chromedriver().setup();
		        ChromeOptions chromeOptions = new ChromeOptions();
		        chromeOptions.setExperimentalOption("prefs", prefs);
		        
		        chromeOptions.addArguments("--no-sandbox");
		        chromeOptions.addArguments("--disable-dev-shm-usage");
		        chromeOptions.addArguments("--start-maximized");
		        chromeOptions.addArguments("--window-size=1920,1080");
		        chromeOptions.addArguments("--disable-gpu");
		        chromeOptions.addArguments("--force-device-scale-factor=1");
		        chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
		        
		        boolean isJenkins = System.getenv("JENKINS_HOME") != null;

		        if (isJenkins) {
		            chromeOptions.addArguments("--headless=new");
		            chromeOptions.addArguments("--window-size=1920,1080");
		            System.out.println("Running in Jenkins → Headless Mode Enabled");
		        } else {
		            System.out.println("Running Locally → Headless Mode Disabled");
		        }

		        driver = new ChromeDriver(chromeOptions);
		    }

		    // ⭐ Continue with your existing code exactly as before
		    Controls.wait(2000);
		    driver.manage().window().maximize();

		    if(loginprop.getProperty("Environment").equalsIgnoreCase("Production"))
		        driver.get(configprop.getProperty("ProductionURL"));

		    if(loginprop.getProperty("Environment").equalsIgnoreCase("dev"))
		        driver.get(configprop.getProperty("devurl"));

		    if(loginprop.getProperty("Environment").equalsIgnoreCase("QA"))
		        driver.get(configprop.getProperty("QAURL"));
		    
		    if(loginprop.getProperty("Environment").equalsIgnoreCase("deployed"))
		        driver.get(configprop.getProperty("urldeployed"));

		    driver.manage().deleteAllCookies();
		    driver.navigate().refresh();

		    System.out.println("browser started");
		}

		
	//BASIC FUNCTIONS
		public static void wait(int x) throws InterruptedException
		{
	        Thread.sleep(x);
	    }
		
		
	//SORTING SECTION
		public static void SortOneColumn(int columnIndex, boolean ascending) throws InterruptedException, StaleElementReferenceException
		{
			String columnHeaderXPath = "//thead/tr/th[" + columnIndex + "]";
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement columnHeader = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(columnHeaderXPath)));
	        
	        columnHeader.click();
	        waitforvisibility(driver, By.xpath(columnHeaderXPath), 10);
	        boolean isAscending = columnHeader.getAttribute("class").contains("MuiTableSortLabel-root Mui-active");
	        
	        if((ascending && !isAscending) || (!ascending && isAscending))
	        {
	        	columnHeader.click();
	        	waitforvisibility(driver, By.xpath(columnHeaderXPath), 10);
	        }
		}
		
		public void Sort(int columncount, boolean ascending) throws InterruptedException
		{
		    By loaderLocator = By.xpath("//img[contains(@class,'MuiBox-root css-7f50zt')]");
			
			for(int columnindex = 1; columnindex<=columncount; columnindex++)
			{
				WebElement columnHeader;
				WebElement nextbtn = null;
				try
				{
					columnHeader = driver.findElement(By.xpath("//table/thead/tr/th[" + columnindex + "]"));
				}
				catch(Exception e)
				{
					System.out.println("Column " + columnindex + " does not exist. Skipping.");
					continue;
				}
/*				try
				{
					nextbtn = driver.findElement(By.xpath("//button[contains(@aria-label, 'next page')]"));
				}
				catch (NoSuchElementException e)
				{
		            System.out.println("Next page button not found. Pagination may not be enabled.");
		        }*/
				
				if(!columnHeader.isDisplayed() && !columnHeader.getAttribute("class").contains("MuiTableSortLabel"))
				{
					System.out.println("Column " + columnindex + " is not sortable. Skipping.");
		            continue;
				}
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		        String startTime = LocalTime.now().format(formatter);
				
				columnHeader.click();
				if(!ascending)
				{
					columnHeader.click();
				}
				
				String endTime = LocalTime.now().format(formatter);
				System.out.println((Duration.between(LocalTime.parse(startTime, formatter), LocalTime.parse(endTime, formatter))).getSeconds()+" seconds taken to sort column "+columnindex);
				List<String> sortedTableData = new ArrayList<>();
				boolean nextpg = true;
				while(nextpg)
				{
					List<WebElement> columnData = driver.findElements(By.xpath("//table/tbody/tr/td[" + columnindex + "]"));
					//Extracting data from the column in a list
					for(WebElement cell:columnData)
					{
						sortedTableData.add(cell.getText().trim());
					}
					if (nextbtn != null && nextbtn.isEnabled())
					{
		                nextbtn.click();
		                Thread.sleep(2000);
		            }
					else
					{
		                nextpg = false;
		            }
				}
				System.out.println("Actual table sorted data for column: "+columnindex+": "+sortedTableData);
							
				List<String> expectedSortedData = new ArrayList<>(sortedTableData);
				if(ascending)
				{
					Collections.sort(expectedSortedData);
				}
				else
				{
					Collections.sort(expectedSortedData, Collections.reverseOrder());
				}
				System.out.println("Expected sorted data: " + expectedSortedData);
				
				//Compare the sorted data
				boolean isSortedCorrectly = expectedSortedData.equals(sortedTableData);
				if(isSortedCorrectly)
				{
					System.out.println("Column "+columnindex+" is sorted correctly in "+(ascending?"ascending":"descending")+" order");
					System.out.println("-------------------------------");
				}
				else
				{
					System.out.println("Column "+columnindex+" sorting failed in "+(ascending?"ascending":"descending")+" order");
					System.out.println("Expected: "+expectedSortedData);
					System.out.println("Actual: "+sortedTableData);
					System.out.println("-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-");
				}
				driver.navigate().refresh();
				Thread.sleep(2000);
			}
		}

	//EXPORT AS XLS FILE
		private String waitForFileToDownload(String downloadDir, String extension) throws InterruptedException
		{
			File dir = new File(downloadDir);
	        File[] files;
	        int attempts = 0;

	        do
	        {
	            Thread.sleep(1000); // Wait 1 second
	            files = dir.listFiles((d, name) -> name.endsWith(extension));
	            attempts++;
	        }
	        while((files == null || files.length == 0) && attempts < 30); // Timeout after 30 seconds
	        if (files != null && files.length > 0)
	        {
	        	return files[0].getAbsolutePath();
	        }
	        else
	        {
	            throw new RuntimeException("File not downloaded within the timeout period.");
	        }
	    }
		
		private boolean verifyXLSData(String filePath, String[][] expectedData)
		{
			try (FileInputStream fis = new FileInputStream(filePath);
	        XSSFWorkbook workbook = new XSSFWorkbook(fis))
			{
				XSSFSheet sheet = workbook.getSheetAt(0);
				for (int i = 0; i < expectedData.length; i++)
				{
					XSSFRow row = sheet.getRow(i);
	                for (int j = 0; j < expectedData[i].length; j++)
	                {
	                	XSSFCell cell = row.getCell(j);
	                    String actualValue = cell.getStringCellValue();
	                    if (!actualValue.equals(expectedData[i][j]))
	                    {
	                        return false;
	                    }
	                }
	            }
	            return true;
	        }
			catch (IOException e)
			{
	            e.printStackTrace();
	            return false;
	        }
	    }
		
				
	//COLUMN CONFIGURATION
		public void testcolumnpositionchange(String columnConfigButtonXpath, String columnconfigxpath, String columnToMove, String columnToSwapWith) throws InterruptedException
		{
			WebElement columnConfigButton = driver.findElement(By.xpath(columnConfigButtonXpath));
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		    wait.until(ExpectedConditions.elementToBeClickable(columnConfigButton)).click();
		    
		    //Locate the draggable items in column configuration
		    List<WebElement> configItems = driver.findElements(By.xpath(columnconfigxpath));
		    
		    //Find the positions of the columns to move and swap
		    WebElement source = null;
		    WebElement target = null;
		    for (WebElement item : configItems)
		    {
		    	String columnText = item.getText().trim();
		        if (columnText.equals(columnToMove))
		        {
		            source = item;
		        }
		        else if (columnText.equals(columnToSwapWith))
		        {
		            target = item;
		        }
		        if (source != null && target != null)
		        {
		            break; // Exit loop once both elements are found
		        }
		    }
		    
		    if (source == null || target == null)
		    {
		        throw new RuntimeException("Columns to move or swap not found in the configuration panel.");
		    }
		    
		    //Drag and drop to rearrange order (example: swap first two items)
		    Actions actions = new Actions(driver);
		    actions.clickAndHold(source).moveToElement(target).release().perform();
		    
		    // Apply changes (if applicable)
		    WebElement savebtn = driver.findElement(By.xpath("//div[contains(@class,'MuiBox-root')]/div[4]/button[1]"));
		    wait.until(ExpectedConditions.elementToBeClickable(savebtn)).click();
		    Thread.sleep(3000);
		    
		    // Verify the table header order
		    List<WebElement> tableHeaders = driver.findElements(By.xpath("//table/thead/tr/th"));
		    int indexToMove = -1, indexToSwapWith = -1;
		    for (int i = 0; i < tableHeaders.size(); i++)
		    {
		    	String headerText = tableHeaders.get(i).getText().trim();
		        if (headerText.equals(columnToMove))
		        {
		            indexToMove = i;
		        }
		        else if (headerText.equals(columnToSwapWith))
		        {
		            indexToSwapWith = i;
		        }
		    }
		    
		    if (indexToMove == -1 || indexToSwapWith == -1)
		    {
		        throw new RuntimeException("Specified columns not found in the table after reordering.");
		    }

		    // Check if actual order matches expected order
		    Assert.assertTrue(indexToMove < indexToSwapWith, 
		            "Swap failed: Column '" + columnToMove + "' should now be after '" + columnToSwapWith + "' in the table.");
		}
		
		public void testColumnVisibilityConfiguration(String columnConfigButtonXpath, String[] fields, boolean enable) throws InterruptedException
		{
			WebElement columnConfigButton = driver.findElement(By.xpath(columnConfigButtonXpath));
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(columnConfigButton)).click();

	        for(String field:fields)
        	{
	        	List<WebElement> toggleNames = driver.findElements(By.xpath("//div[contains(@class,'MuiBox-root')]/ul/li"));
	        	boolean found=false;
	        	
        		for(WebElement item:toggleNames)
        		{
        			String label = item.findElement(By.xpath(".//div[2]/span")).getText().trim();
        			if (label.equalsIgnoreCase(field))
        			{
        				WebElement toggle = item.findElement(By.xpath(".//span/span/input[@type='checkbox']"));
        				try
        				{
        					toggleColumn(label, toggle, enable);
        				}
        				catch(Exception e)
        				{
        					System.out.println("Failed to toggle column: " + label + ". Error: " + e.getMessage());
        				}
        				found=true;
                        break;
        			}
        		}
        		if(!found)
        		{
        			System.out.println("Column "+field+" not found!");
        		}
        	}
	        WebElement savebtn = driver.findElement(By.xpath("//div[contains(@class,'MuiBox-root')]/div[4]/button[1]"));
	        wait.until(ExpectedConditions.elementToBeClickable(savebtn)).click();
		}
		
	//PIE-GRAPH VERIFICATION
		//PERFECTLY DONE - WITHOUT NUMBER IN INPUT
		public static int validatePieChartECharts2(int chartIndex, String targetClass) throws Exception
		{
		    Actions actions = new Actions(driver);

		    // Dynamic canvas locator based on chart index
		    WebElement canvas = driver.findElement(By.xpath("(//div[contains(@class,'echarts-for-react')])[" + chartIndex + "]//canvas"));

		    // Scroll into view
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior:'instant', block:'center'});",canvas);

		    // Tooltip specific to this chart only
		    By tooltipLocator = By.xpath("(//div[contains(@class,'echarts-for-react')])[" + chartIndex + "]");

		    int width = canvas.getSize().width;
		    int height = canvas.getSize().height;

		    int centerX = width / 2;
		    int centerY = height / 2;
		    int radius = (int)(Math.min(width, height) * 0.35);

		    // Sweep through all slices
		    for (int angle = 0; angle < 360; angle += 5)
		    {
		        double rad = Math.toRadians(angle);
		        int x = centerX + (int)(radius * Math.cos(rad));
		        int y = centerY + (int)(radius * Math.sin(rad));

		        actions.moveToElement(canvas, x - centerX, y - centerY).perform();
		        Thread.sleep(70);

		        List<WebElement> tips = driver.findElements(tooltipLocator);
		        if (tips.isEmpty()) continue;

		        WebElement tip = tips.get(0);
		        String tooltipText = tip.getText().trim();

		        // Match case-insensitive
		        if (tooltipText.toLowerCase().contains(targetClass.toLowerCase()))
		        {
		            // Extract number from tooltip
		            String num = tooltipText.replaceAll("[^0-9]", " ").trim().split(" ")[0];
		            int actualCount = Integer.parseInt(num);

		            System.out.println("Tooltip → " + tooltipText);
		            System.out.println("Extracted Count → " + actualCount);

		            // YOU WILL VERIFY THIS VALUE WITH TABLE
		            return actualCount;
		        }
		    }

		    throw new RuntimeException("Slice not found for class: " + targetClass);
		}
		
		
		//PERFECT WORKING METHOD
		public static void validatePieChartECharts(int chartIndex,String targetClass,int expectedCount) throws Exception
		{
		    Actions actions = new Actions(driver);

		    // Dynamic canvas locator based on chart index
		    WebElement canvas = driver.findElement(By.xpath("(//div[contains(@class,'echarts-for-react')])[" + chartIndex + "]//canvas"));
		    
		    ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({behavior:'instant', block:'center'});",canvas);

		    // Tooltip specific to this chart only
		    By tooltipLocator = By.xpath("(//div[contains(@class,'echarts-for-react')])[" + chartIndex + "]");

		    // Canvas dimensions
		    int width = canvas.getSize().width;
		    int height = canvas.getSize().height;

		    int centerX = width / 2;
		    int centerY = height / 2;
		    int radius = (int) (Math.min(width, height) * 0.35);

		    for (int angle = 0; angle < 360; angle += 5) {

		        double rad = Math.toRadians(angle);
		        int x = centerX + (int) (radius * Math.cos(rad));
		        int y = centerY + (int) (radius * Math.sin(rad));

		        actions.moveToElement(canvas, x - centerX, y - centerY).perform();
		        Thread.sleep(70);

		        List<WebElement> tipList = driver.findElements(tooltipLocator);
		        if (tipList.isEmpty()) continue;

		        WebElement tip = tipList.get(0);
		        String tooltipText = tip.getText().trim();

		        // Tooltip matched slice
		        if (tooltipText.toLowerCase().contains(targetClass)) {

		            // Extract numbers
		            String num = tooltipText.replaceAll("[^0-9]", " ").trim().split(" ")[0];
		            int actualCount = Integer.parseInt(num);

		            System.out.println("Tooltip → " + tooltipText);  // NEW OUTPUT

		            if (actualCount == expectedCount) {
		                System.out.println("PASS → Slice matched for: " + targetClass);
		            } else {
		                System.out.println("FAIL → Expected: " + expectedCount + ", Found: " + actualCount);
		            }
		            return;
		        }
		    }

		    throw new RuntimeException("Slice not found for class: " + targetClass);
		}
		
	//DICTIONARY CHECKING SECTION
		public List<String> checkSpellings()
		{
	        List<String> misspelledWords = new ArrayList<>();
	        List<WebElement> elements = driver.findElements(By.xpath("//*"));

	        for (WebElement element : elements)
	        {
	            String text = element.getText();
	            if (text != null && !text.isEmpty())
	            {
	                misspelledWords.addAll(findMisspelledWords(text));
	            }
	        }
	        return misspelledWords;
	    }
		
		private List<String> findMisspelledWords(String text)
		{
	        List<String> misspelledWords = new ArrayList<>();
	        String[] words = text.split("\\s+"); // Split text into words
	        
	        for (String word : words)
	        {
	            if (!isWordCorrect(word))
	            {
	                misspelledWords.add(word);
	            }
	        }
	        return misspelledWords;
	    }
		
		private boolean isWordCorrect(String word)
		{
	        // Example dictionary (replace with a more comprehensive one or use an API)
	        String[] dictionary = {"example", "word", "your", "username", "password", "login", "dashboard"};
	        
	        LevenshteinDistance distance = new LevenshteinDistance();
	        for (String dictWord : dictionary) {
	            if (distance.apply(word.toLowerCase(), dictWord.toLowerCase()) <= 1) {
	                return true; // Word is considered correct if the edit distance is <= 1
	            }
	        }
	        return false; // Word is misspelled
	    }
		
		public static void dynamicwait(By locator)
		{
			try
			{
				WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(100));
				WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			}
			catch(NoSuchElementException e)
			{
				e.printStackTrace();
			}
		}
		
		public static void waitforvisibility(WebDriver driver, By locator, int x) throws InterruptedException
		{
			WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(x));
		    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		    element.click();
	    }

		public static void clickElement(By locator)
		{
			driver.findElement(locator).click();
		}
		
		public String getAttributeValue(WebElement element, String attribute)
		{
			return element.getAttribute(attribute);
		}
				
	//TEXT ADDING & EDITING
		public static void type(By locator, String text)
		{
			driver.findElement(locator).sendKeys(text);
			try
			{
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		public static void fileattach(By locator, String text)
		{
			File f = new File(text);
			if(f.exists() && f.isFile())
			{
				driver.findElement(locator).sendKeys(text);
				System.out.println("File attached: "+text);
			}
			else
			{
				System.out.println("File not attached: "+text);
			}
			
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		public static void editText(By locator, String text) throws InterruptedException
		{
			driver.findElement(locator).sendKeys(Keys.CONTROL+"A", Keys.DELETE);
			Thread.sleep(1000);
			driver.findElement(locator).sendKeys(text, Keys.DOWN, Keys.ENTER);
		}
		
		public static void confirmStringdata(String text1, String text2, String attribute)
		{
			String text = driver.findElement(By.xpath(text1)).getText().toString();
			if(text.equals(text2))
			{
				System.out.println(attribute + " is correct.");
			}
			else
			{
				System.out.println(attribute + " is not correct.");
			}
		}

		public static void confirmdata(By locator, String text, String attribute)
		{
			String actualtext = driver.findElement(locator).getText().trim();
			if(actualtext.equals(text))
			{
				System.out.println(attribute+" is correct.");
			}
			else
			{
				System.out.println(attribute+ " is not correct.");
			}
		}
		
		public static void confirmplaceholders(By locator, String text, String attribute)
		{
			String placeholder = driver.findElement(locator).getAttribute("placeholder");
			if(placeholder.equals(text))
			{
				System.out.println(attribute+" placeholder is correct.");
			}
			else
			{
				System.out.println(attribute+" placeholder is not correct.");
			}
		}
		
	//COMPARING XLS & WEB TABLE DATA
		public static List<List<String>> readExcel(String filePath) throws EncryptedDocumentException, IOException
		{
			List<List<String>> excelData = new ArrayList<>();
	        FileInputStream fis = new FileInputStream(new File(filePath));
	        Workbook workbook = WorkbookFactory.create(fis);
	        Sheet sheet = workbook.getSheetAt(0);
	        for (Row row : sheet) {
	            List<String> rowData = new ArrayList<>();
	            for (int i = 0; i < row.getLastCellNum(); i++)
	            {
	            	Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	                cell.setCellType(CellType.STRING);
	                rowData.add(cell.getStringCellValue().trim());
	            }
	            excelData.add(rowData);
	        }
	        workbook.close();
	        fis.close();
	        return excelData;
		}
		
		public static List<List<String>> fetchWebTableData(WebDriver driver, String tableRowXpath, String tableCellXpath)
		{
			List<List<String>> tableData = new ArrayList<>();
	        List<WebElement> rows = driver.findElements(By.xpath(tableRowXpath));
	        for (WebElement row : rows)
	        {
	            List<WebElement> cells = row.findElements(By.xpath(tableCellXpath));
	            List<String> rowData = new ArrayList<>();
	            for (WebElement cell : cells) // Include all cells starting from the first
	            {
	            	String cellText = cell.getText().trim();
	                rowData.add(cellText.isEmpty() ? "" : cellText); // Add empty string for blank cells
	            }
	            tableData.add(rowData);
	            /*for (int i = 1; i < cells.size(); i++) // Start from the second cell (index 1)
	            {
	                rowData.add(cells.get(i).getText().trim());
	            }
	            tableData.add(rowData);*/
	        }
	        return tableData;
	    }

		
		public static void compareWebAndExcelRowByRow(List<List<String>> excelData, List<List<String>> gridData)
		{
			System.out.println(excelData.size());
			System.out.println(excelData);
			System.out.println(gridData.size());
			System.out.println(gridData);
			
			boolean mismatchFound = false;
			for (int i = 0; i < Math.min(excelData.size(), gridData.size()); i++)
			{
				List<String> excelRow = excelData.get(i);
		        List<String> gridRow = gridData.get(i);
		        
		        // Skip the first column of the gridRow
		        List<String> adjustedGridRow = gridRow.subList(1, gridRow.size());

		        // Compare each cell in the current row
		        for (int j = 0; j < Math.min(excelRow.size(), adjustedGridRow.size()); j++)
		        {
		        	String excelValue = excelRow.get(j).trim();
		            String gridValue = adjustedGridRow.get(j).trim();

		            if (excelValue.isEmpty() && gridValue.isEmpty())
		            {
		                continue; // Skip comparison for empty cells
		            }
		            
		            if (!excelValue.equals(gridValue))
		            {
		                mismatchFound = true;
		                // Print mismatch details for the cell
		                System.out.println("Mismatch found at Cell:");
		                System.out.println("Row: " + (i + 1) + ", Column: " + (j + 1));
		                System.out.println("Excel Value: '" + excelValue + "'");
		                System.out.println("Grid Value: '" + gridValue + "'");
		            }
		        }
		    }

		    // Handle extra rows in Excel or Grid data
		    if (excelData.size() != gridData.size())
		    {
		        mismatchFound = true;
		        System.out.println("Row count mismatch between Excel and Grid Data.");
		        System.out.println("Excel Rows: " + excelData.size() + ", Grid Rows: " + gridData.size());
		    }

		    // Print final result
		    if (!mismatchFound)
		    {
		        System.out.println("All cells match perfectly between Excel and Grid.");
		    } else
		    {
		        System.out.println("Comparison completed with mismatches.");
		    }			
	    }

	//DROP DOWN SECTION
		public static void dropdown(By locator, String text)
		{
			driver.findElement(locator).sendKeys(text);
			driver.findElement(locator).sendKeys(Keys.DOWN, Keys.ENTER);
		}
		
		/*public static void dropdownwaitforlist(By locator, By locator_list, String text)
		{
			driver.findElement(locator).sendKeys(text);
			waitForElement(locator_list, 10);
			driver.findElement(locator).sendKeys(Keys.DOWN, Keys.ENTER);
		}*/
		
	//DATES SECTION
		public String dateconvert(String date) throws ParseException
		{
			SimpleDateFormat input = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
			
			Date d = input.parse(date);
			String outputdate = output.format(d);
			return outputdate;
		}
		
		public static void select_year_in_calendar(By locator, String year) throws InterruptedException
		{
			Controls.clickElement(locator);
			driver.switchTo().activeElement();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//div[contains(@class,'MuiPickersYear-root')]/button[text()='"+year+"']")).click();
		}

	//EXPORT AS XLS FILE
		public void exportfile(By exportbtn, String downloadfolder, String[][] tablelocator) throws EncryptedDocumentException, FileNotFoundException, IOException
		{
			try
			{
				ChromeOptions options = new ChromeOptions();
				Map<String, Object> prefs = new HashMap<>();
				prefs.put("download.default_directory", downloadfolder);
	            prefs.put("download.prompt_for_download", false);
	            prefs.put("safebrowsing.enabled", true);
	            options.setExperimentalOption("prefs", prefs);
	            
	            WebElement downloadButton = driver.findElement(exportbtn);
	            downloadButton.click();
	            
	            String downloadedFilePath = waitForFileToDownload(downloadfolder, ".xlsx");
	            boolean isDataValid = verifyXLSData(downloadedFilePath, tablelocator);
	            if (isDataValid)
	            {
	                System.out.println("Table data verified successfully.");
	            }
	            else
	            {
	                System.err.println("Table data verification failed.");
	            }
			}
			catch (Exception e)
			{
	            e.printStackTrace();
			}
		}
		
				
	//COLUMN CONFIGURATION
		private void toggleColumn(String columnName, WebElement toggle, boolean show)
		{
			System.out.println("Found toggle for column: " + columnName);
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", toggle);

		    boolean isSelected = toggle.isSelected();
		    System.out.println("Toggle before: isSelected=" + isSelected);
		    if ((show && !isSelected) || (!show && isSelected)) {
		        try {
		            toggle.click();
		        } catch (Exception e) {
		            System.out.println("Native click failed, trying JS click.");
		            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
		        }
		        System.out.println((show ? "Enabled " : "Disabled ") + columnName);
		    }
		    // Give UI some time to update
		    try {
		        Thread.sleep(500);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    
		    // Wait for an attribute change if any (replace with the actual attribute you observe)
		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		    wait.until(driver -> {
		        // Check for attribute change or class change instead of isSelected
		        String ariaChecked = toggle.getAttribute("aria-checked");
		        return (show && "true".equals(ariaChecked)) || (!show && ("false".equals(ariaChecked) || ariaChecked == null));
		    });
		    System.out.println("Final toggle state for " + columnName + ": " + toggle.isSelected());
		}
		
		private boolean isColumnVisible(List<WebElement> tableHeaders, String columnName)
		{
			for (WebElement header : tableHeaders)
			{
				if (header.getText().equalsIgnoreCase(columnName))
				{
					return header.isDisplayed();
	            }
	        }
	        return false;
	    }
}