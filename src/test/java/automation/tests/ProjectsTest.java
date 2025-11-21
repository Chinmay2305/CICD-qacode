package automation.tests;

import org.testng.annotations.Test;
import java.io.IOException;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import automation.PageObject.PageObject_Assets;
import automation.PageObject.PageObject_Projects;

public class ProjectsTest extends BaseTest
{
    private PageObject_Projects pp;
    private boolean aborttest = false;

    @BeforeClass
    public void setup() throws IOException, InterruptedException
    {
    	super.setup();
        pp = new PageObject_Projects(driver);
    }

    @Test(priority = 0)
    public void verifylogin() throws Exception
    {
        login();
    }

    @Test(priority = 1, dependsOnMethods = {"verifylogin"})
    public void verifyProjects() throws Exception
    {
    	reports.startTest("Projects Page Test");
        if(aborttest)
        {
            reports.log(com.aventstack.extentreports.Status.SKIP, "Skipped due to login failure");
            throw new SkipException("Skipped due to login failure");
        }

        try
        {
            pp.openprojects();
            //pp.sortprojects();
            pp.filterprojects();
            reports.log(com.aventstack.extentreports.Status.PASS, "Opened Projects page");
        }
        catch(Exception e)
        {
            reports.log(com.aventstack.extentreports.Status.FAIL, "Error opening Projects: " + e.getMessage());
            aborttest = true;
            throw e;
        }
    }

    @Test(priority = 2, dependsOnMethods = {"verifyProjects"})
    public void firstProject()
    {
        try
        {
            reports.startTest("Open First Project");
            pp.open1stproject();
            reports.log(com.aventstack.extentreports.Status.PASS, "Opened 1st project");
        } catch (Exception e) {
            reports.log(com.aventstack.extentreports.Status.FAIL, e.getMessage());
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