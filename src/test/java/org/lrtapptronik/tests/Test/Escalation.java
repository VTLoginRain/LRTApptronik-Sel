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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Escalation {

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
        sparkReporter.config().setReportName("Apptronik Report -" +timeStamp);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //
        test = extent.createTest("Login Test");
        driver.get(ConfigReader.get("URL"));
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
        inescl.sendKeys("Escalated", Keys.ENTER);

        Thread.sleep(3000);

        //  Failure here : This is to click on the Next arrow to select status as Closed
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement recordClick = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@href='/lightning/r/500D1000008ew15IAA/view' and @title='00001468']")


        ));
        recordClick.click();


        WebElement recClick = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[.//span[text()='Next']]")));
        WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Next']]")));
        Thread.sleep(5000);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", refreshButton);
        Thread.sleep(5000);

    //click on close button
        WebElement closedTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Closed' and contains(@class, 'slds-path__link')]")));
        WebElement closedTabclick = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@title='Closed' and contains(@class, 'slds-path__link')]")));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closedTabclick);

        //Click on Mark as Current status

        WebElement markAsCurrentBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[.//span[text()='Mark as Current Status']]")));
        WebElement markAsCurrentBtnclick = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Mark as Current Status']]")));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", markAsCurrentBtnclick);

        // Closure Reason drop down
        Thread.sleep(5000);
        WebElement closureReasonDropdown = driver.findElement(By.xpath("//button[@name='Closure_Reason__c' and @role='combobox']"));
        closureReasonDropdown.click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//span[@title='Issue Resolved']")).click();

        // Click on Done button
        Thread.sleep(5000);
        WebElement doneButton = driver.findElement(By.xpath("//button[normalize-space()='Done']"));
        doneButton.click();


    }
}
