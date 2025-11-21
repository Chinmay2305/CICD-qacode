package automation.tests;

import org.testng.annotations.Test;
import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import automation.PageObject.PageObject_Analytics;
import automation.PageObject.PageObject_Login;

public class AnalyticsTest extends BaseTest
{
	private PageObject_Analytics pa;

	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pa = new PageObject_Analytics(driver);
	}
	
	@Test(priority = 0)
	public void verifylogin() throws Exception
	{
		login();
	}

	@Test(priority = 1, dependsOnMethods = {"verifylogin"})
	public void graphverification() throws Exception
	{
		pa.homevendors();
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