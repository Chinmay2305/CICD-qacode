package automation.PageObject;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.openqa.selenium.By;
import automation.BaseMethods.Controls;

public class PageObject_Login extends Controls
{
	private Properties pl;
	WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
	
	public PageObject_Login(WebDriver driver, Properties pl)
	{
		this.pl = pl;
	}
	
	public void login() throws IOException, InterruptedException
	{
		String expectedtext = "A place to share your knowledge.";
		
		WebElement texttest = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loginprop.getProperty("text"))));
		System.out.println(texttest.getText().toString());
		
		if(expectedtext.equals(texttest.getText().toString()))
		{
			System.out.println("ITS THE SAME");
			Reporter.log("Reporter log visible in Jenkins - SAME", true);
		}
		else
		{
			System.out.println("ITS DIFFERENT");
			Reporter.log("Reporter log visible in Jenkins - DIFFERENT", true);
		}
	}
}