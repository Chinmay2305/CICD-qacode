package automation.tests;

import org.testng.annotations.Test;
import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import automation.PageObject.PageObject_Analytics;
import automation.PageObject.PageObject_Login;
import automation.PageObject.PageObject_Projects;
import automation.PageObject.PageObject_Vendors;

public class AnalyticsTest extends BaseTest
{
	private PageObject_Analytics pa;
	private PageObject_Vendors pv;
	private PageObject_Projects pp;

	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pa = new PageObject_Analytics(driver);
		pv = new PageObject_Vendors(driver);
		pp = new PageObject_Projects(driver);
	}
	
	@Test(priority = 0)
	public void verifylogin() throws Exception
	{
		login();
	}

	@Test(priority = 1, dependsOnMethods = {"verifylogin"})
	public void vendorgraphverification() throws Exception
	{
		int piecount = pa.homevendors_vendorsregiongraph();
		int tablecount = pv.singlefilterforanalytics();
		if (piecount == tablecount) {
	        System.out.println("✔ PASS — Both counts match!");
	    } else {
	        System.out.println("✘ FAIL — Mismatch! Pie: " + piecount + " | Table: " + tablecount);
	    }
	}
	
	@Test(priority = 2, dependsOnMethods = {"verifylogin"})
	public void projectstatusgraphverification() throws Exception
	{
		int piecount = pa.projectstatusgraph();
		int tablecount = pp.singlefilterforanalytics();
		if (piecount == tablecount) {
	        System.out.println("✔ PASS — Both counts match!");
	    } else {
	        System.out.println("✘ FAIL — Mismatch! Pie: " + piecount + " | Table: " + tablecount);
	    }
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result)
	{
		super.afterMethod(result);
	}
	
	@AfterTest
	public void afterTest()
	{
		super.afterTest();
	}
}