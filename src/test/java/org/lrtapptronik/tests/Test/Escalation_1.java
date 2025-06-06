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
import java.util.List;

public class Escalation_1 {


    public WebDriver driver;
    public WebDriverWait wait;
    ExtentReports extent;
    public ExtentTest test;
    String vals;
    public By openJira = By.xpath("//button[normalize-space()='Open JIRA']");
    public By editStatus = By.xpath("//div[@data-target-selection-name='sfdc:RecordField.JIRA_Ticket__c.Status__c']//button[@title='Edit Status']");
    public By status = By.xpath("//input[@name='Status__c']");
    public By save = By.xpath("//button[normalize-space()='Save']");
    public By valToBeSel = By.xpath("//a[@role='option' and @data-tab-name='Waiting on Customer']");
    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark as Current Status']]");
    public By caseOwnVal = By.xpath("//span[contains(@class, 'owner-name')]//span[normalize-space(text())='Service Team']");

    @Test
    public void escalationTest1() throws InterruptedException {
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
        test.info("Selecting the Case cases which are Escalated.");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement inescl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='Case-search-input' and @aria-label='Search this list...']")));

        inescl.click();
        inescl.sendKeys("New", Keys.ENTER);

        Thread.sleep(3000);

        //  Failure here : This is to click on the Next arrow to select status as Closed
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement recordClick = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/lightning/r/500D1000008f6hHIAQ/view' and @title='00001873']")


        ));
        recordClick.click();
        Thread.sleep(5000);

        ((JavascriptExecutor) driver).executeScript(
                "const banner = document.querySelector(\"[class*='slds-notify']\"); if (banner) banner.style.display = 'none';"
        );

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Read Case Owner name
        WebElement caseOwnerName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                caseOwnVal ));
        System.out.println("Case Owner: " + caseOwnerName.getText());

          WebElement caseOwnerName1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'owner-name')]")));
      // ((JavascriptExecutor) driver).executeScript("arguments[0].click();", caseOwnerName);
        System.out.println("Case Owner: " + caseOwnerName1.getText());


// Locate the "Internal Priority" label
        WebElement internalPriorityLabel = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[contains(@class, 'test-id__field-label') and normalize-space()='Internal Priority']")
        ));

// Scroll to it using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", internalPriorityLabel);



        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@title='Edit Internal Priority']")
        ));
        editButton.click();
        WebElement dropdownTrigger = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='Internal Priority']")
        ));
        dropdownTrigger.click();

// Step 2: Select "Medium" from the dropdown
        WebElement optionMedium = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@title='Medium']")
        ));
        optionMedium.click();

        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Save']")
        ));
        save.click();
    }

}

