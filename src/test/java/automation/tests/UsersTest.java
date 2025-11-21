package automation.tests;

import org.testng.annotations.Test;
import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import automation.PageObject.PageObject_Users;

public class UsersTest extends BaseTest
{
	private PageObject_Users pu;

	@BeforeTest
	public void setup() throws IOException, InterruptedException
	{
		super.setup();
		pu = new PageObject_Users(driver);
	}
	
	@Test(priority = 0)
	public void verifylogin() throws Exception
	{
		login();
	}

	@Test(priority = 1, dependsOnMethods = {"verifylogin"})
	public void users() throws Exception
	{
//		pu.openadduserform();
//		pu.verifytitlesplaceholders();
//		pu.adduser();
//		pu.confirmuser();
		pu.multiplefilter();
//		pu.sortusers();
		driver.get("https://qa.aerfinity.acumenaviation.in/settings/users");
		Thread.sleep(5000);
		
		By tablelocator = By.xpath("//table[contains(@class,'MuiTable-root')]");
		String filepath = "C:\\Users\\DELL\\Downloads\\Users.xlsx";
		pu.getXLSdata(filepath);
		pu.validatetabledata(tablelocator, filepath);
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