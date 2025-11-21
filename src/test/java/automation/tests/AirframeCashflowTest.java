package automation.tests;

import org.testng.annotations.Test;
import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import automation.PageObject.PageObject_Assets;
import automation.PageObject.PageObject_AirframeCashflow;
import automation.PageObject.PageObject_Projects;

public class AirframeCashflowTest extends BaseTest
{
	private PageObject_AirframeCashflow pa;
	
	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pa = new PageObject_AirframeCashflow(driver);
	}
	
	@Test(priority = 1)
	public void verifylogin() throws IOException, InterruptedException
	{
		login();
	}
	@Test(priority = 2)
	public void projectsassets() throws Exception
	{
		pa.cashflowsetup();
		pa.cashflowview();
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