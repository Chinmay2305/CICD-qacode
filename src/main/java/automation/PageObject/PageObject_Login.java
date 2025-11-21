package automation.PageObject;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import automation.BaseMethods.Controls;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PageObject_Login extends Controls
{
	private Properties pl;
	WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
	
	public PageObject_Login(WebDriver driver, Properties pl)
	{
		this.pl = pl;
	}
	
	public void login(String username, String password) throws IOException, InterruptedException
	{
		WebElement user = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loginprop.getProperty("login_user_path.xpath"))));
		user.clear();
		user.sendKeys(username);
		WebElement pass = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loginprop.getProperty("login_pwd_path.xpath"))));
		pass.clear();
		pass.sendKeys(password);
		Controls.clickElement(By.xpath(loginprop.getProperty("login_btn.xpath")));
	}
}