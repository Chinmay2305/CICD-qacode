package automation.PageObject;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
		WebElement texttest = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loginprop.getProperty("text"))));
		
		System.out.println(texttest.getText().toString());
	}
}