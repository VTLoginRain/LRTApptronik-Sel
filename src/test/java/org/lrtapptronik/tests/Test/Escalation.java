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

    public By averageTime = By.xpath("//a[normalize-space()='Average Blocker Time']");
    public By nxtButton = By.xpath("//button[@part='button' and text()='Next']");
    public By escalationdtl = By.xpath("//textarea[@class='slds-textarea' and @part='textarea']");
    public By escalateBtn = By.xpath("//button[text()='Escalate']");
    public By escalHeading = By.xpath("//h2[text()='Escalate']");
    public By esclReason = By.xpath("//select[@name='Escalation_Reason']");
    public By radioBtn = By.xpath("//span[text()='Engineering Team']/ancestor::label/span");

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
        inescl.sendKeys("In Progress", Keys.ENTER);

        Thread.sleep(3000);

        //  Failure here : This is to click on the Next arrow to select status as Closed
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement recordClick = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/lightning/r/500D1000008f4vtIAA/view' and @title='00001784']")


        ));
        recordClick.click();
        Thread.sleep(5000);
        WebElement recClick = wait.until(ExpectedConditions.visibilityOfElementLocated(escalateBtn));
        WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(escalateBtn));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", refreshButton);
        Thread.sleep(5000);

        // Wait for Escalate Page heading to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(escalHeading));
        test.log(Status.INFO, "Click On the Escalate Page");
        click(radioBtn, "Selected Engineering Team for Escalation");

        // Handle Escalation Reason dropdown
        wait.until(ExpectedConditions.presenceOfElementLocated(esclReason));
        Select dropdown = new Select(driver.findElement(esclReason));

        boolean reasonFound = dropdown.getOptions().stream()
                .anyMatch(option -> option.getText().trim().equals(ConfigReader.get("EscalationReasonDD")));
        if (reasonFound) {
            dropdown.selectByVisibleText(ConfigReader.get("EscalationReasonDD"));
            test.log(Status.INFO, "Selected Reason is : " + ConfigReader.get("EscalationReasonDD") + ".");
            escalReasn = ConfigReader.get("EscalationReasonDD");
        } else {
            throw new NoSuchElementException("Reason " + ConfigReader.get("EscalationReasonDD") + " not found in dropdown.");
        }
        // Enter escalation details
        WebElement escDetail = wait.until(ExpectedConditions.elementToBeClickable(escalationdtl));
        escDetail.click();
        escDetail.sendKeys(ConfigReader.get("EscalationDetail"));
        test.log(Status.INFO, "Case Details Entered.");
        click(nxtButton, "Proceeded to JIRA Details by Clicking on Next button.");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement runIdInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'RUN_ID')]/ancestor::div[contains(@class,'flowruntime-input')]//input")
        ));
        runIdInput.sendKeys("12345");
        selectDropdownValue("Priority", ConfigReader.get("Priority"));
        // Locate the select dropdown
      /*  WebElement priorityDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='Priority']/ancestor::div[contains(@class, 'flowruntime-input')]//select")
        ));

// Use Select class to choose a value
        Select select = new Select(priorityDropdown);
        select.selectByVisibleText("High"); // or "Medium", "Low", etc.*/


// Locate the dropdown
        WebElement blockingStatusDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='Blocking Status']/ancestor::div[contains(@class, 'flowruntime-input')]//select")
        ));

// Interact using Selenium's Select class
        Select select1 = new Select(blockingStatusDropdown);
        select1.selectByVisibleText("Blocking"); // or "Non-Blocking", "Limited", etc.


        WebElement alphaSystemDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='Alpha System Number']/ancestor::div[contains(@class, 'flowruntime-input')]//select")
        ));

        Select select3 = new Select(alphaSystemDropdown);
        select3.selectByVisibleText("A-05");  // Replace with the desired option


        WebElement operationDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='Operation']/ancestor::div[contains(@class, 'flowruntime-input')]//select")
        ));

        Select select4 = new Select(operationDropdown);
        select4.selectByVisibleText("System Functional Test");  // Replace with desired option

        test.info("Click on Submit button to escalate");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//lightning-button[contains(@class, 'flow-button__NEXT')]//button[normalize-space()='Next']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();


        //Click on Mark as Current status

      /*  WebElement markAsCurrentBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[.//span[text()='Mark as Current Status']]")));
        WebElement markAsCurrentBtnclick = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Mark as Current Status']]")));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", markAsCurrentBtnclick);*/


    }

    private void click(By locator, String logMessage) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
        test.log(Status.INFO, logMessage);
    }

    private void selectDropdownValue(String label, String visibleText) {//veena
        WebElement blockingStatusDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='"+label+"']/ancestor::div[contains(@class, 'flowruntime-input')]//select")
        ));
// Interact using Selenium's Select class
        Select select1 = new Select(blockingStatusDropdown);
        select1.selectByVisibleText(visibleText);
    }
}
