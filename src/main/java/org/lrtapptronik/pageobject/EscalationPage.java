package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.lrtapptronik.Utility.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EscalationPage {

    public By accept = By.xpath("//button[text()='Accept']");
    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark Status as Complete']]");
    public By escalateBtn = By.xpath("//button[text()='Escalate']");
    public By escalHeading = By.xpath("//h2[text()='Escalate']");
    public By esclReason = By.xpath("//select[@name='Escalation_Reason']");
    public By escalationdtl = By.xpath("//textarea[@class='slds-textarea' and @part='textarea']");
    public By nxtButton = By.xpath("//button[@part='button' and text()='Next']");
    public By jiraNextBtn = By.xpath("//button[@part='button' and text()='Next']");
    public By radioBtn = By.xpath("//span[text()='Engineering Team']/ancestor::label/span");

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest test;

    public EscalationPage(WebDriver driver, WebDriverWait wait, ExtentTest test)
    {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }

    public void caseEscalations(){

        try {
            click(accept, "Clicked on Accept Button.");

            // Wait for Mark Status as Complete button to become clickable
            WebElement btnStatus = wait.until(ExpectedConditions.elementToBeClickable(markStatusAsCom));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnStatus);

            // Wait for Escalate button to be visible
            wait.until(ExpectedConditions.elementToBeClickable(escalateBtn)); //veena remove this
            click(escalateBtn, "Clicked on Escalate Button.");

            // Wait for Escalate Page heading to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(escalHeading));
            test.log(Status.INFO, "On the Escalate Page");

            click(radioBtn, "Selected Engineering Team for Escalation");

            // Handle Escalation Reason dropdown
            wait.until(ExpectedConditions.presenceOfElementLocated(esclReason));
            Select dropdown = new Select(driver.findElement(esclReason));

            boolean reasonFound = dropdown.getOptions().stream()
                    .anyMatch(option -> option.getText().trim().equals(ConfigReader.get("EscalationReasonDD")));
    
            if (reasonFound) {
                dropdown.selectByVisibleText(ConfigReader.get("EscalationReasonDD"));
                test.log(Status.INFO, "Selected Reason is : " +ConfigReader.get("EscalationReasonDD")+ ".");
            } else {
                throw new NoSuchElementException("Reason " +ConfigReader.get("EscalationReasonDD") + " not found in dropdown.");
            }
            // Enter escalation details
            WebElement escDetail = wait.until(ExpectedConditions.elementToBeClickable(escalationdtl));
            escDetail.click();
            escDetail.sendKeys(ConfigReader.get("EscalationDetail"));
            test.log(Status.INFO, "Case Details Entered.");

            // Click Next
            click(nxtButton, "Proceeded to JIRA Details");

        } catch (NoSuchElementException e) {
            Assert.fail("Failed due to missing element or timeout.");
        }
    }

    public void fillJiraDetails(){

        try{
            selectDropdownValue("Blocking Status", ConfigReader.get("BlockingStatusDD"));
            selectDropdownValue("Alpha System Number", ConfigReader.get("AlphaSysNum"));
            selectDropdownValue("Operation", ConfigReader.get("OperationDD"));

            click(jiraNextBtn, "JIRA Related Details Entered");

            // Wait for Submit button and click
            WebElement btnSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@part='button' and text()='Submit']")));
            btnSubmit.click();
            test.log(Status.INFO, "Clicked on Submit Button.");

        } catch (NoSuchElementException e) {
            Assert.fail("Failed due to missing element or timeout.");
        }
    }

    /*public void verifyEscalationFieldVal(){

    }*/





    private void click(By locator, String logMessage) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
        test.log(Status.INFO, logMessage);
    }


    private void selectDropdownValue(String label, String visibleText) {//veena
        String dropdownButtonXpath = String.format("//button[@aria-label='%s']", label); //veena change it
        String valueXpath = String.format("//span[text()='%s']", visibleText);//veena change it

        click(By.xpath(dropdownButtonXpath), "Clicked on " + label + " Dropdown");
        click(By.xpath(valueXpath), "Selected value for " + label + ": " + visibleText);
    }

}
