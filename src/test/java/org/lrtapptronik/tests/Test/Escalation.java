package org.lrtapptronik.tests.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.lrtapptronik.Utility.ConfigReader;
import org.lrtapptronik.pageobject.LoginPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Escalation {
    public String escalReasn;

    public By userName = By.xpath("//input[@type='email' or @name='loginfmt']");
    public By userPass = By.xpath("//input[@type='password' or @name='passwd']");
    public By submit = By.xpath("//input[@type='submit' and @value='Yes']");
    public By nextBtn = By.xpath("//button[.//span[text()='Next']]");
    public WebDriver driver;
    public WebDriverWait wait;
    ExtentReports extent;
    public ExtentTest test;

    @Test
    public void escalationTest() throws InterruptedException {
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss").format(new Date());
        String reportPath = "test-output/LRT_AutomationReport_" + timeStamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("LRT-Automation Report");
        sparkReporter.config().setReportName("Apptronik Report -" + timeStamp);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //
        test = extent.createTest("Email to Case Creation Test");
        /*driver.get(ConfigReader.get("URL"));
        test.log(Status.INFO, "Navigated to login page: " + driver.getTitle());
        LoginPage loginPage = new LoginPage(driver, wait, test);
        test.log(Status.INFO, "Calling loginValidUser() to login.");
        loginPage.loginValidUser(ConfigReader.get("UName"), ConfigReader.get("Pwd"));
        test.log(Status.INFO, "Login is successful.");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement record = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@title='Cases']")
        ));
        record.click();
        test.pass("Case tab is selected");

// Search In Escalated Cases
        test.info("Selecting the Case cases which are In Progress");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement inescl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='Case-search-input' and @aria-label='Search this list...']")));

        inescl.click();
        inescl.sendKeys("In Progress", Keys.ENTER);

        Thread.sleep(3000);

        //  Failure here : This is to click on the Next arrow to select status as Closed
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement recordClick = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/lightning/r/500D1000008f4vtIAA/view' and @title='00001784']")


        ));
        recordClick.click();
        Thread.sleep(5000);*/


        driver.get( ConfigReader.get("gmailLogin"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.visibilityOfElementLocated(userName)).sendKeys(ConfigReader.get("EmailToCase"),Keys.ENTER);
        driver.findElement(nextBtn).click();
      //  wait.until(ExpectedConditions.visibilityOfElementLocated(userPass)).sendKeys(ConfigReader.get("EmailPassCase"),Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(submit)).click();

        test.info("Entered Username and Password and clicked on submit.");
        Thread.sleep(9000);
        Thread.sleep(9000);
    }
}
