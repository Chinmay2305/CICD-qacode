package automation.ExtentReport;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ReportsOriginal {
    private static ReportsOriginal instance;
    private static ExtentReports extent = null;
    private static ExtentTest logger;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");
    private static LocalDateTime currentdatetime = LocalDateTime.now();

    private ReportsOriginal() {
        // Private constructor to prevent external instantiation
        String reportPath = System.getProperty("user.dir") + "\\Reports\\Aerfin_Report_" + dtf.format(currentdatetime) + ".html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(reporter);

        extent.setSystemInfo("Host Name", "AerFin Automation");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("User name", "Chinmay");

        reporter.config().setDocumentTitle("AERFIN AUTOMATION REPORT");
        reporter.config().setReportName("AERFIN AUTOMATION TEST REPORT");
        reporter.config().setTheme(Theme.STANDARD);
    }

    public static synchronized ReportsOriginal getInstance() {
        if (instance == null) {
            instance = new ReportsOriginal();
        }
        return instance;
    }

    public void startTest(String text) {
        if (extent == null) {
            throw new IllegalStateException("ExtentReports is not initialized.");
        }
        logger = extent.createTest(text);
    }

    public void log(Status status, String details) {
        if (logger == null) {
            throw new IllegalStateException("ExtentTest is not initialized. Call startTest() first.");
        }
        logger.log(status, details);
    }
    
    public void logSkip(String testName, String skipReason) {
        if (extent == null) {
            throw new IllegalStateException("ExtentReports is not initialized.");
        }
        if (logger == null) {
            startTest(testName); // Start a new test entry for skipped tests
        }
        logger.log(Status.SKIP, skipReason != null ? skipReason : "Test was skipped due to failure of previous tests.");
    }

    public void openReport(WebDriver driver)
    {
    	try
    	{
    		String reportPath = System.getProperty("user.dir") + "\\Reports\\Aerfin_Report_" + dtf.format(currentdatetime) + ".html";
            File reportFile = new File(reportPath);
            String reportUrl = reportFile.toURI().toString();
            if (driver != null)
            {
            	JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("window.open();");
                for (String handle : driver.getWindowHandles())
                {
                    driver.switchTo().window(handle);
                }
            	driver.get(reportUrl);
                System.out.println("Report opened in browser: " + reportUrl);
            }
            else
            {
                System.out.println("WebDriver instance is null. Cannot open report.");
            }
        }
    	catch(Exception e)
    	{
            System.out.println("Error opening report in browser: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void endTest() {
        if (extent != null) {
            extent.flush();
        }
    }
}