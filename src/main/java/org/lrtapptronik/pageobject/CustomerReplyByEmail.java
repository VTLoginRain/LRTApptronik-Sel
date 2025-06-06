package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import org.lrtapptronik.Utility.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;



public class CustomerReplyByEmail {

    public WebDriver driver;
    public WebDriverWait wait;
    public ExtentTest test;

    public By userName = By.xpath("//input[@type='email' or @name='loginfmt']");
    public By userPass = By.xpath("//input[@type='password' or @name='passwd']");
    public By submit = By.xpath("//input[@type='submit' and @value='Yes']");
    public By searchtxt = By.xpath("//input[@aria-label='Search' or @placeholder='Search']");
    public By selectEmail = By.xpath("(//div[@role='option' or @data-convid]) [1]");
    public By clickReply = By.xpath("//button[@aria-label='Reply' or @title='Reply' or @data-icon-name='Reply']");
    public By msgbody = By.xpath("//div[@contenteditable='true']");
    public By sendBtn = By.xpath("//button[@title='Send' or @aria-label='Send']");
    public By clickAcc = By.xpath("//button[contains(@aria-label, 'Account')]");
    public By clickSignOut = By.xpath("//a[text()='Sign out']");
    public By seeRefreshBtn = By.xpath("//span[text()='Refresh this feed']/preceding-sibling::lightning-primitive-icon");
    public By clickRefresh = By.xpath("//button[@title='Refresh this feed']");
    public By sentEmailContent = By.xpath("//span[contains(@class, 'emailMessageBody')]");



    public CustomerReplyByEmail(WebDriver driver, WebDriverWait wait, ExtentTest test) {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }

    public void custoReplyBack() throws InterruptedException {

        String originalWindow = driver.getWindowHandle();
        ((JavascriptExecutor) driver).executeScript("window.open()");
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        test.info("Outlook is Opened in new tab");
        driver.get( ConfigReader.get("outlook"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.visibilityOfElementLocated(userName)).sendKeys(ConfigReader.get("EmailID"),Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOfElementLocated(userPass)).sendKeys(ConfigReader.get("EmailPass"),Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(submit)).click();

        test.info("Entered Username and Password and clicked on submit.");
        Thread.sleep(9000);
        wait.until(ExpectedConditions.elementToBeClickable(searchtxt)).sendKeys("Sandbox",Keys.ENTER);
        Thread.sleep(9000);

        test.info("Recent mail with Subject as Sanbox is selected");
        wait.until(ExpectedConditions.elementToBeClickable(selectEmail)).click();
        test.info("Click Reply on recent selected email");
        wait.until(ExpectedConditions.elementToBeClickable(clickReply)).click();

        Thread.sleep(7000);
        WebElement messageBody = wait.until(ExpectedConditions.visibilityOfElementLocated(msgbody));
        messageBody.click();

        messageBody.sendKeys(ConfigReader.get("EmailReplyContent"));
        Thread.sleep(9000);
         test.info("Customer types the email body->> via Automation");
        wait.until(ExpectedConditions.elementToBeClickable(sendBtn)).click();
        test.info("Clicked on Send button ");
        //Signout Email
        test.info("Clicking on Account option to SignOut");
        driver.findElement(clickAcc).click();
        Thread.sleep(1000);
        test.info("Signing Out and closing the tab to switch to SF Application");
        driver.findElement(clickSignOut).click();
        //close the tab
        driver.close();
        driver.switchTo().window(originalWindow);
        Thread.sleep(1000);

        test.info("Refreshing the Feed to see the latest email communication");
        WebElement refreshIcon = wait.until(ExpectedConditions.elementToBeClickable(seeRefreshBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", refreshIcon);
        WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(clickRefresh));
        Thread.sleep(10000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", refreshButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", refreshButton);

        Thread.sleep(5000);
        WebElement emailBody = wait.until(ExpectedConditions.visibilityOfElementLocated(sentEmailContent));
        String message = emailBody.getText();
        String firstLine = message.split("\\n")[0].replace("Email Body: ", "").trim();
        System.out.println("$$$$$: " + firstLine);
        test.info("Extracting the email communication sent by the customer from the feed as :" +firstLine);
        if (firstLine.equalsIgnoreCase(ConfigReader.get("EmailReplyContent"))){
            test.pass("Email content at the SF case Feed is :" +firstLine+ " matched with expected message sent from outtlook as :"+ConfigReader.get("EmailReplyContent"));
            System.out.println("Pass");
        }
        driver.navigate().refresh();
        Thread.sleep(5000);
        String caseStatuss = driver.findElement(
                By.xpath("//p[@title='Status']/following-sibling::p//lightning-formatted-text")
        ).getText();
        test.pass("Salesforce Case status is extracted  as:" +caseStatuss);
       Thread.sleep(8000);
    }


}
