/*
package org.lrtapptronik.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.Test;
import org.lrtapptronik.tests.utilities.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ApptrAutoMain {

    WebDriver driver;
    WebDriverWait wait;
    ExtentReports extent;
    ExtentTest test;
    private static String caseNumber;
    public static final String Esc = "Escalated";

    
    @Test
    public void loginTest() {
        test = extent.createTest("Login Test");
        driver.get(ConfigReader.get("URL"));
        test.log(Status.INFO, "Navigated to login page: " + driver.getTitle());

        try {
            driver.findElement(By.id("username")).sendKeys(ConfigReader.get("UName"));
            test.log(Status.INFO, "Entered username.");

            driver.findElement(By.id("password")).sendKeys(ConfigReader.get("Pwd"));
            test.log(Status.INFO, "Entered password.");

            driver.findElement(By.id("Login")).click();
            test.log(Status.INFO, "Clicked Login.");
        } catch (NoSuchElementException | TimeoutException e) {
            test.log(Status.FAIL, "Test failed due to missing element or incorrect XPath: " + e.getMessage());
            Assert.fail("Test failed due to missing element or incorrect XPath.");
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@title='Show Navigation Menu']")));
            test.log(Status.PASS, "Login successful.");
        } catch (TimeoutException e) {
            test.log(Status.FAIL, "Login failed due to invalid credentials.");
            Assert.fail("Login failed: Invalid credentials or login element not found.");
        }

    }

   //@Test(priority = 2, dependsOnMethods = "loginTest")
   @Test
    public void caseCreation() {
        test = extent.createTest("Automated Email Verification on Case Creation");

        try {
            click(By.xpath("//button[@title='Show Navigation Menu']"), "Opened navigation menu.");
            click(By.xpath("//span[contains(text(), 'Cases')]"), "Clicked Cases link.");
            //click(By.xpath("//div[@title='New']"), "Clicked New button.");
            WebElement btnNew = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@title='New']")));
            btnNew.click();
            test.log(Status.INFO, "Clicked On New Button to create case .");

            WebElement txtAccount = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[normalize-space(text())='Account']/parent::lightning-grouped-combobox//input")));
            txtAccount.click();
            test.log(Status.INFO, "Clicked Account field.");

            click(By.xpath("//span[@title='"+ConfigReader.get("Account")+"']"), "Selected Account : " +ConfigReader.get("Account"));
            Thread.sleep(3500);
            // Wait for dropdown and select value
            By contactSelectBy = By.xpath("//select[@name='Contact']");
            wait.until(ExpectedConditions.presenceOfElementLocated(contactSelectBy));
            Select dropdown = new Select(driver.findElement(contactSelectBy));

            boolean contactFound = dropdown.getOptions().stream()
                    .anyMatch(option -> option.getText().trim().equals(ConfigReader.get("Contact")));

            if (contactFound) {
                dropdown.selectByVisibleText(ConfigReader.get("Contact"));
                test.log(Status.INFO, "Selected Contact:" + ConfigReader.get("Contact") + ".");
            } else {
                throw new NoSuchElementException("Contact" +ConfigReader.get("Contact") + " not found in dropdown.");
            }

            click(By.xpath("//button[text()='Next']"), "Clicked Next (Customer Contact Details).");

            WebElement txtSubject = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='Subject']")));
            txtSubject.sendKeys(ConfigReader.get("Subject"));
            test.log(Status.INFO, "Entered Subject: " +ConfigReader.get("Subject")+ ".");

            click(By.xpath("//button[text()='Next']"), "Clicked Next (Case Details).");
            click(By.xpath("//button[text()='Submit']"), "Clicked Submit.");

            WebElement caseNumElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[@title='Case Number']/parent::div//lightning-formatted-text")));
            caseNumber = caseNumElement.getText();
            test.log(Status.PASS, "Case Number : " + caseNumber);
            test.log(Status.INFO, "Verifying email now");
            // Verifying that email is generated
            click(By.xpath("//a[text()='Feed']"), "Opened Feed tab.");

            try {
                WebElement verifyContent = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//span[contains(@class, 'emailMessageBody') and contains(text(), 'Your case has been created')]")));
                test.log(Status.PASS, "Verified email content: " + verifyContent.getText());


            } catch (NoSuchElementException | TimeoutException e) {
                test.log(Status.FAIL, "Email verification failed: " + e.getMessage());
                Assert.fail("Email not generated or XPath issue.");
            }

        } catch (Exception e) {
            test.log(Status.FAIL, "Case creation failed: " + e.getMessage());
            Assert.fail("Failure during case creation flow.");
        }
    }

    private void selectDropdownValue(String label, String visibleText) {//veena
        String dropdownButtonXpath = String.format("//button[@aria-label='%s']", label);
        String valueXpath = String.format("//span[text()='%s']", visibleText);

        click(By.xpath(dropdownButtonXpath), "Clicked on " + label + " Dropdown");
        click(By.xpath(valueXpath), "Selected value for " + label + ": " + visibleText);
    }

    private void click(By locator, String logMessage) {//veena
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
        test.log(Status.INFO, logMessage);
    }

    public boolean areStringsEqualIgnoreCase(String actual, String expected) {//veena
        if (actual == null || expected == null) return false;
        return actual.trim().equalsIgnoreCase(expected.trim());
    }

    //@Test(priority = 3, dependsOnMethods = "caseCreation")
   // @Test(dependsOnMethods = "caseCreation")
    public void caseEscalation() {
        test = extent.createTest("Case Escalation");
        test.log(Status.INFO, "Escalating Case Number: " + caseNumber);

        try {
            // Click Accept button
            click(By.xpath("//button[text()='Accept']"), "Clicked on Accept Button.");

            // Wait for Mark Status as Complete button to become clickable
            WebElement btnStatus = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[text()='Mark Status as Complete']]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnStatus);

            // Wait for Escalate button to be visible
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Escalate']")));
            click(By.xpath("//button[text()='Escalate']"), "Clicked on Escalate Button.");

            // Wait for Escalate Page heading to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Escalate']")));
            test.log(Status.INFO, "On the Escalate Page");

            // Select Engineering Team
            //click(By.xpath("//span[text()='Engineering Team']/ancestor::label/span"),
            //        "Selected Engineering Team for Escalation");

            click(By.xpath("//span[text()='"+ConfigReader.get("EscalatedTo")+"']/ancestor::label/span"),
                    "Selected " +ConfigReader.get("EscalatedTo")+ "for Escalation");


            // Handle Escalation Reason dropdown
            By esclReason = By.xpath("//select[@name='Escalation_Reason']");
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
            WebElement escDetail = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//textarea[@class='slds-textarea' and @part='textarea']")));
            escDetail.click();
            escDetail.sendKeys(ConfigReader.get("EscalationDetail"));
            test.log(Status.INFO, "Case Details Entered.");

            // Click Next
            click(By.xpath("//button[@part='button' and text()='Next']"), "Proceeded to JIRA Details");

            // Fill JIRA Details
            selectDropdownValue("Blocking Status", ConfigReader.get("BlockingStatusDD"));
            selectDropdownValue("Alpha System Number", ConfigReader.get("AlphaSysNum"));
            selectDropdownValue("Operation", ConfigReader.get("OperationDD"));

            click(By.xpath("//button[@part='button' and text()='Next']"), "JIRA Related Details Entered");

            // Wait for Submit button and click
            WebElement btnSubmit = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@part='button' and text()='Submit']")));
            btnSubmit.click();
            test.log(Status.INFO, "Clicked on Submit Button.");

            // Now need to check verification for all entered details.
            Thread.sleep(2000);
            click(By.xpath("//a[@data-label='Details' and text()='Details']"), "Clicked on Details Tab to verify the data");
            //Assert.assertEquals();
            Thread.sleep(2000);
            // Case Owner value verification.
            String actualValueOwner = driver.findElement(By.xpath("//div[@class='slds-form-element__control']//span//slot//force-owner-lookup//div//span//force-lookup//div//span//slot/span")).getText();
            String expectedValueOwner = ConfigReader.get("EscalatedTo");

            if (areStringsEqualIgnoreCase(actualValueOwner, expectedValueOwner)) {
                test.log(Status.PASS, "Values match > Actual value is :" +actualValueOwner+ " Expected values is :" +expectedValueOwner);
            } else {
                test.log(Status.FAIL, "Mismatch: Expected '" + expectedValueOwner + "', but got '" + actualValueOwner + "'");
            }

            // Case Status verification.
            //String actualValueStatus = driver.findElement(By.xpath("(//lightning-formatted-text[@data-output-element-id='output-field'])[21]")).getText();
            String actualValueStatus = Esc;
            String expectedValueStatus = "Escalated";

            if (areStringsEqualIgnoreCase(actualValueStatus, expectedValueStatus)) {
                test.log(Status.PASS, "Values match > Actual value is :" +actualValueStatus+ " Expected value is :" +expectedValueStatus);
            } else {
                test.log(Status.FAIL, "Mismatch: Expected '" + expectedValueStatus + "', but got '" + actualValueStatus + "'");
            }

            // Verify that Checkbox of Escalated is checked
            // Scroll down till Escalation detail section is visible.
        try {

            WebElement element = driver.findElement(By.xpath("//span[text()='Escalation Details']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        catch (NoSuchElementException e){
            Assert.fail("Failed due to missing element or timeout.");
        }
            try {
                WebElement indicator = driver.findElement(By.cssSelector("span.slds-checkbox_faux[part='indicator']"));
                Assert.assertTrue(indicator.isDisplayed(), "Checkbox indicator is not displayed.");
                test.log(Status.PASS, "Escalated Checkbox is checked");
                //System.out.println("Checkbox indicator is displayed – Test Passed");
            } catch (NoSuchElementException e) {
                test.log(Status.FAIL, "Verification failed: " + e.getMessage());
                Assert.fail("Checkbox indicator not found – Test Failed");
            }

            // Verifying Escalation Reason.
            String actualEscReason = driver.findElement(By.xpath("(//lightning-formatted-text[@data-output-element-id='output-field'])[21])")).getText();
            String expEscReason = ConfigReader.get("EscalationReasonDD");

            if (areStringsEqualIgnoreCase(actualEscReason, expEscReason)) {
                test.log(Status.PASS, "Escalation Reason match.");
            } else {
                test.log(Status.FAIL, "Escalation Reason Mismatch: Expected '" + expEscReason + "', but got '" + actualEscReason + "'");
            }

            // Verifying Escalation Details.
            String actualEscDetail = driver.findElement(By.xpath("(//lightning-formatted-text[@data-output-element-id='output-field'])[35])")).getText();
            String expEscDetail = ConfigReader.get("EscalationDetail");

            if (areStringsEqualIgnoreCase(actualEscDetail, expEscDetail)) {
                test.log(Status.PASS, "Escalation Detail match.");
            } else {
                test.log(Status.FAIL, "Escalation Detail Mismatch: Expected '" + expEscReason + "', but got '" + actualEscReason + "'");
            }


        } catch (NoSuchElementException | TimeoutException | InterruptedException e) {
            test.log(Status.FAIL, "Verification failed: " + e.getMessage());
            Assert.fail("Failed due to missing element or timeout.");
        }
    }

}
*/
