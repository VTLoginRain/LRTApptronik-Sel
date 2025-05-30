package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ClosedReason  {

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest test;


    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark as Current Status']]");
    public By closureRsn = By.xpath("//button[@name='Closure_Reason__c' and @role='combobox']");
    public By closedTab = By.xpath("//a[@title='Closed' and contains(@class, 'slds-path__link')]");
    public By nextArrow = By.xpath("//button[.//span[text()='Next']]");
    public By doneBtn = By.xpath("  //button[normalize-space()='Done']");


    public ClosedReason(WebDriver driver, WebDriverWait wait, ExtentTest test) {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }


    public void closeBtnClick() throws InterruptedException {

        click(nextArrow,"Click on Next button");
        Thread.sleep(5000);
        click(closedTab,"Click on the Closed button");
        click(markStatusAsCom,"Click on Mark Status as Complete");
        Thread.sleep(5000);
        driver.findElement(closureRsn).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//span[@title='Issue Resolved']")).click(); //veena remove hardcoding here
        Thread.sleep(5000);
        driver.findElement(doneBtn).click();




    }
    private void click(By locator, String logMessage) throws InterruptedException {

        WebElement elementVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
