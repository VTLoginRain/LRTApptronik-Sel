package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import org.junit.Test;
import org.lrtapptronik.Utility.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EmailToCaseCreation {

    public By userName = By.xpath("//input[@type='email' or @name='loginfmt']");

    public By nextBtn = By.xpath("//button[.//span[text()='Next']]");
    public By submit = By.xpath("//input[@type='submit' and @value='Yes']");
    public WebDriver driver;
    public WebDriverWait wait;
    public ExtentTest test;

    public EmailToCaseCreation(WebDriver driver, WebDriverWait wait, ExtentTest test) {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }
//

    public void OpenOutlookAndSentEmail() throws InterruptedException {
        driver.get( ConfigReader.get("gmailLogin"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.visibilityOfElementLocated(userName)).sendKeys(ConfigReader.get("EmailToCase"),Keys.ENTER);
        driver.findElement(nextBtn).click();
       // wait.until(ExpectedConditions.visibilityOfElementLocated(userPass)).sendKeys(ConfigReader.get("EmailPassCase"),Keys.ENTER);

       // wait.until(ExpectedConditions.elementToBeClickable(submit)).click();

        test.info("Entered Username and Password and clicked on submit.");
        Thread.sleep(9000);
        Thread.sleep(9000);
    }


}
