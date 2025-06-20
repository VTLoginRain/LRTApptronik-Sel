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
    public By closureRsn = By.xpath("//span[text()='Closure Reason']/ancestor::div[contains(@class, 'slds-form-element')]//a[@role='combobox']");
    public By collision = By.xpath("//a[@role='option' and normalize-space(text())='Collision']");
    public By issueResolvedStatus = By.xpath("//span[@title='Thermal Fault']");
    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark as Current Status']]");
    public By Edit = By.xpath("//span[h2[text()='Key Fields']]/a[@title='Edit Key Fields']//p[text()='Edit']");
    public By closedTab = By.xpath("//a[@title='Closed' and contains(@class, 'slds-path__link')]");
    public By nextArrow = By.xpath("//button[.//span[text()='Next']]");
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
        test.info("Selecting the Case cases which are in Escalated");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement inescl = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='Case-search-input' and @aria-label='Search this list...']")));

        inescl.click();
        inescl.sendKeys("Closed", Keys.ENTER);

        Thread.sleep(3000);

        //  Failure here : This is to click on the Next arrow to select status as Closed
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement recordClick = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/lightning/r/500Dv00000NLRFvIAP/view' and @title='00001132']")


        ));
        recordClick.click();
        Thread.sleep(10000);
        click(Edit,"Click on Edit");
        WebElement comboBox = wait.until(ExpectedConditions.elementToBeClickable(closureRsn));
        comboBox.click();
        WebElement optionCollision = wait.until(ExpectedConditions.elementToBeClickable(collision));
        optionCollision.click();

        Thread.sleep(3000);
        By saveButton = By.xpath("(//button[@title='Save'])[2]");
        driver.findElement(saveButton).click();






        //yha se correct flow hai
       /* WebElement editClosureReasonBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[@title='Edit Closure Reason']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", editClosureReasonBtn);
        Thread.sleep(500);
        editClosureReasonBtn.click();
        WebElement comboBoxButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'slds-combobox__input') and @aria-label='Closure Reason']")));
        comboBoxButton.click();
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//lightning-base-combobox-item//span[text()='Collision']")));
        option.click();
        WebElement saveButton = driver.findElement(By.xpath("//button[@name='SaveEdit' and contains(@class, 'slds-button_brand')]"));
        saveButton.click();*/




    }
    private void click(By locator, String logMessage) throws InterruptedException {

        WebElement elementVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);



    }
}
