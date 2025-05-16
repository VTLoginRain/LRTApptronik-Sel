package org.lrtapptronik.tests.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseTest {

    public WebDriver driver;
    public WebDriverWait wait;
    ExtentReports extent;
    public ExtentTest test;

    @BeforeClass
    public void setUp() {

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

        test = extent.createTest("Setup");
        test.log(Status.INFO, "Chrome launched and browser maximized.");
    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            test.log(Status.INFO, "Browser closed.");

        }
        extent.flush();
    }
}