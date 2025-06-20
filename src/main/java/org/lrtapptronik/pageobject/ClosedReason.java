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

    public By Edit = By.xpath("//span[h2[text()='Key Fields']]/a[@title='Edit Key Fields']//p[text()='Edit']");
    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark as Current Status']]");
    public By closureRsn = By.xpath("//span[text()='Closure Reason']/ancestor::div[contains(@class, 'slds-form-element')]//a[@role='combobox']");
    public By closedTab = By.xpath("//a[@title='Closed' and contains(@class, 'slds-path__link')]");
    public By nextArrow = By.xpath("//button[.//span[text()='Next']]");
    public By save = By.xpath("(//button[@title='Save'])[2]");
    public By collision = By.xpath("//a[@role='option' and normalize-space(text())='Collision']");


    public ClosedReason(WebDriver driver, WebDriverWait wait, ExtentTest test) {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }


    public void closeBtnClick() throws InterruptedException {

        test.info("Click on Next button to see the Closed tab");
        click(nextArrow,"Click on Next button to see the Closed tab");
        Thread.sleep(5000);
        click(closedTab,"Click on the Closed button");
        test.info("Click on the Closed button");
        click(Edit,"Click on Edit button to enter the closure Reason");
        WebElement comboBox = wait.until(ExpectedConditions.elementToBeClickable(closureRsn));
        comboBox.click();
        WebElement optionCollision = wait.until(ExpectedConditions.elementToBeClickable(collision));
        optionCollision.click();
        Thread.sleep(3000);
        test.info("Closure Reason is selected as : Collision");
        By saveButton = By.xpath("(//button[@title='Save'])[2]");
        driver.findElement(saveButton).click();
        click(markStatusAsCom,"Click on Mark Status as Complete");
        test.info("Click on Mark Status as Complete");

        System.out.println("Clicked on Mark Status as Complete ");
        Thread.sleep(10000);
        test.pass("Salesforce Case is closed");

        CaseCreationPage obj =new CaseCreationPage(driver, wait, test);
        test.pass("Salesforce Case : " +obj.caseSerialNum + "is closed with Closure Reason as : Collision.");

    }
    private void click(By locator, String logMessage) throws InterruptedException {

        WebElement elementVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);


    }
}
