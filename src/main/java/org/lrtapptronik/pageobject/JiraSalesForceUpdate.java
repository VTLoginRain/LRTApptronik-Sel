package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.lrtapptronik.Utility.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class JiraSalesForceUpdate {
    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest test;
    String vals;
    String statuss;


    public By openJira = By.xpath("//button[normalize-space()='Open JIRA']");
    public By editStatus = By.xpath("//div[@data-target-selection-name='sfdc:RecordField.JIRA_Ticket__c.Status__c']//button[@title='Edit Status']");
    public By statusC = By.xpath("//input[@name='Status__c']");
    public By save = By.xpath("//button[normalize-space()='Save']");
    public By  valToBeSel = By.xpath("//a[@role='option' and @data-tab-name='Waiting on Customer']");
    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark as Current Status']]");
    public JiraSalesForceUpdate(WebDriver driver, WebDriverWait wait, ExtentTest test) {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }


    public void JiratoSF() throws InterruptedException {
        //Jira ticket section
        ((JavascriptExecutor) driver).executeScript(
                "const banner = document.querySelector(\"[class*='slds-notify']\"); if (banner) banner.style.display = 'none';"
        );
        test.log(Status.INFO, "Navigation to Jira Section");
        WebElement openJi = wait.until(ExpectedConditions.visibilityOfElementLocated(openJira));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", openJi);
        Thread.sleep(5000);
        test.info("Clicked on Open Jira Section on Left top of Page");
        //Status Update
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //  driver.findElement(By.xpath("//div[contains(@class, 'slds-form-element__control')]//lightning-formatted-text[text()='To Do']"));
        driver.findElement(editStatus).click();
        WebElement statusInput = driver.findElement(statusC);
        statusInput.clear();
        test.info("Deleted the status in Jira ticket");
        statusInput.sendKeys(ConfigReader.get(("JiraTicketStatusDone")));
        test.info("The Jira ticket status is entered as Done");
        Thread.sleep(2000);
        driver.findElement(save).click();
        Thread.sleep(4000);
        String title= driver.getTitle();
        System.out.println("Title is " +title);
        Thread.sleep(2000);
//retest below to close the button | worked in standalone
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@title, 'Close') and contains(@title, 'JIRA')]")
        ));
        test.info("The Jira ticket tab is closed");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtn);
        WebElement statusElement = driver.findElement(By.xpath("//p[strong[text()='Status:']]")
        );
        String fullText = statusElement.getText(); // e.g., "Status: Blocked"
        String Jstatus = fullText.replace("Status:", "").trim();
        System.out.println("Jira Status: " + Jstatus);
        test.info("Jira ticket status is extracted  as:" +Jstatus);
        String status = "";
        switch (Jstatus) {
            case "Done":
                status = "Verification";
                break;

            case "Fix Validation":
                status = "Waiting on Customer";
                break;

        }
        System.out.println("Switch Status: " + status);

        String caseStatus = driver.findElement(
                By.xpath("//p[@title='Status']/following-sibling::p//lightning-formatted-text")
        ).getText();
        test.info("Salesforce Case status is extracted  as:" +caseStatus);
        System.out.println("Status value is: " + caseStatus);
        if (status.equalsIgnoreCase(caseStatus)){
            test.pass("Test Case is Passed When Jira Case status is :" +Jstatus + " and SF Ticket Status is Updated as :"+caseStatus);
            System.out.println("Passed");
        }


    }

    public void SFtoJira() throws InterruptedException {
        Thread.sleep(2000);
       // click(waitingOnCus,"Click on Waiting on Customer");
        WebElement elementVisible = wait.until(ExpectedConditions.presenceOfElementLocated(valToBeSel));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(valToBeSel));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        test.info("Clicked on the Waiting on Customer at SF Case");
        vals = element.getText();
        String[] lines = vals.split("\\n");
        String result = lines[lines.length - 1];
        System.out.println("Split Result Case Status " +result);
        test.info("SF Case status :" +result);
        Thread.sleep(5000);
        click(markStatusAsCom,"Click on Mark Status as Complete");
        test.info("Clicked on the Mark Status as Complete at SF Case");
        driver.navigate().refresh();
        Thread.sleep(5000);
        WebElement statusElement = driver.findElement(By.xpath("//p[strong[text()='Status:']]")
        );
        test.info("Clicked on the Jira Section to get the Jira Status.");
        String fullText = statusElement.getText(); // e.g., "Status: Blocked"
        statuss = fullText.replace("Status:", "").trim();
        System.out.println("Jira Status: " + statuss);
        test.info("The Jira Status extracted from Jira section is:"+statuss);
        String status = "";

        switch (result) {
            case "Waiting on Customer":
                status = "Blocked";
                break;

            case "Escalated", "New":
                status = "TO DO";
                break;

            case "In Progress":
                status = "In Progress";
                break;

        }

        System.out.println("Switch Status: " + status);

        if (status.equalsIgnoreCase(statuss)){
            test.pass("Test Case is Passed When SF Case status is :" +result + "Jira Ticket Status is Updated as :"+statuss);
            System.out.println("Passed");
        }


    }
    private void click(By locator, String logMessage) throws InterruptedException {

        WebElement elementVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

}


