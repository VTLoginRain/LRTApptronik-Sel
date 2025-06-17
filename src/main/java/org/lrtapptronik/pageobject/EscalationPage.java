package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.lrtapptronik.Utility.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EscalationPage {

    public By submitBtn=By.xpath("//div[contains(@class, 'slds-float_right')]//button[text()='Submit']");
    public By radioBtnField = By.xpath("//span[text()='Field Service Team']/ancestor::label/span");
    public By accept = By.xpath("//button[text()='Accept']");
    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark Status as Complete']]");
    public By escalateBtn = By.xpath("//button[text()='Escalate']");
    public By escalHeading = By.xpath("//h2[text()='Escalate']");
    public By esclReason = By.xpath("//select[@name='Escalation_Reason']");
    public By escalationdtl = By.xpath("//textarea[@class='slds-textarea' and @part='textarea']");
    public By nxtButton = By.xpath("//button[@part='button' and text()='Next']");
    public By jiraNextBtn = By.xpath("//button[@part='button' and text()='Next']");
    public By radioBtn = By.xpath("//span[text()='Engineering Team']/ancestor::label/span");
    public By caseOwnVal = By.xpath("//span[contains(@class, 'owner-name')]//span[normalize-space(text())='Team LogicRain']");
    public By detailTab =By.xpath("//a[@id='detailTab__item' and text()='Details']");
    public By caseStatus =By.xpath("//div[@data-target-selection-name='sfdc:RecordField.Case.Status']//lightning-formatted-text");
    public By caseDetails= By.xpath("//span[@class='test-id__field-label' and normalize-space(text())='Escalation Details']");
    public By caseDetVal=By.xpath("//lightning-formatted-text[normalize-space()='Robot Inoperable in Customer Environment']");
    public By caseDetailBan =By.xpath("//button[.//span[normalize-space()='Escalation Details']]");
    public By caseReasonVal=By.xpath("//lightning-formatted-text[normalize-space()='Hardware Malfunction']");
    public By escalRsn = By.xpath("//span[@class='test-id__field-label' and text()='Escalation Reason']");
    public By nextto = By.xpath("//button[normalize-space()='Submit']");
    public By RunIdVal= By.xpath("//*[contains(text(),'RUN_ID')]/ancestor::div[contains(@class,'flowruntime-input')]//input");



    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest test;

    public String caseStaus;
    public String escDetailVal ;
    public String escRsnVal ;
    public String escalReasn ;
    public  String actualValueOwner ;

    public EscalationPage(WebDriver driver, WebDriverWait wait, ExtentTest test)
    {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }

    public void caseEscalations(){

        try {
            CaseCreationPage obj =new CaseCreationPage(driver, wait, test);
            test.info("Escalating Case Number:" +obj.caseSerialNum);
            click(accept, "Clicked on Accept Button.");
            // Wait for Mark Status as Complete button to become clickable
            WebElement btnStatus = wait.until(ExpectedConditions.elementToBeClickable(markStatusAsCom));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnStatus);

            // Wait for Escalate button to be visible
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
                test.log(Status.INFO, "Selected Reason is : " +ConfigReader.get("EscalationReasonDD")+ ".");
                escalReasn =ConfigReader.get("EscalationReasonDD");
            } else {
                throw new NoSuchElementException("Reason " +ConfigReader.get("EscalationReasonDD") + " not found in dropdown.");
            }
            // Enter escalation details
            WebElement escDetail = wait.until(ExpectedConditions.elementToBeClickable(escalationdtl));
            escDetail.click();
            escDetail.sendKeys(ConfigReader.get("EscalationDetail"));
            test.log(Status.INFO, "Case Details Entered.");

            // Click Next
            click(nxtButton, "Proceeded to JIRA Details by Clicking on Next button.");

        } catch (NoSuchElementException | InterruptedException e) {
            Assert.fail("Failed due to missing element or timeout.");
        }
    }

    public void fillJiraDetails(){

try {
    selectDropdownValue("Priority", ConfigReader.get("Priority"));
    selectDropdownValue("Blocking Status", ConfigReader.get("BlockingStatusDD"));
    selectDropdownValue("Apollo Number", ConfigReader.get("AlphaSysNum"));
    WebElement runIdInput = wait.until(ExpectedConditions.visibilityOfElementLocated(RunIdVal));
    runIdInput.sendKeys(ConfigReader.get("RUN_ID"));
    selectDropdownValue("Operation", ConfigReader.get("OperationDD"));
    click(jiraNextBtn, "JIRA Related Details Entered");
    // Wait for Submit button and click veena
    WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(nextto));
    submitButton.click();
    test.log(Status.INFO, "Clicked on Submit Button.");
}
         catch (NoSuchElementException e) {
            Assert.fail("Failed due to missing element or timeout.");
        }
    }

    public void verifyEscalationFieldVal() throws InterruptedException {


        //Verifying Escalation Owner
        Thread.sleep(4000);
        driver.findElement(By.xpath("//a[@id='detailTab__item' and text()='Details']")).click();
       // click(detailTab, "Clicked on Details Tab to verify the data");
        Thread.sleep(4000);

        WebElement scrollToCaseOwnVal= wait.until(ExpectedConditions.visibilityOfElementLocated(caseOwnVal));


        System.out.println("Case Owner value is :" +scrollToCaseOwnVal.getText());
        actualValueOwner = scrollToCaseOwnVal.getText();
        String expectedValueOwner = ConfigReader.get("EscalatedTo");

        if (areStringsEqualIgnoreCase(actualValueOwner, expectedValueOwner)) {
            test.log(Status.PASS, "Values match > Actual value of case Owner selected was :" +actualValueOwner+ " matched with Expected values as :" +expectedValueOwner);
            System.out.println("Case Owner value is matched");
        } else {
            test.log(Status.FAIL, "Mismatch: Expected '" + expectedValueOwner + "', but got '" + actualValueOwner + "'");
        }

        //Verifying the status
        WebElement statusVal = wait.until(ExpectedConditions.visibilityOfElementLocated(caseStatus));
        statusVal.click();
        caseStaus=statusVal.getText();
        System.out.println("Status Value "+caseStaus);
        String expectedCaseStatus = ConfigReader.get("CaseStatus");
        if (areStringsEqualIgnoreCase(caseStaus, expectedCaseStatus)) {
            test.log(Status.PASS, "Values match > Actual value of Case status selected was :" +caseStaus+ " matched with Expected value as :" +expectedCaseStatus);
            System.out.println("Case status value is matched");
        } else {
            test.log(Status.FAIL, "Mismatch: Expected '" + expectedValueOwner + "', but got '" + actualValueOwner + "'");
        }
        //Verifying Escalation Details
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(caseDetailBan));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
        element.click();
        Thread.sleep(4000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(caseDetails)).click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement escalationDetVals = wait.until(ExpectedConditions.visibilityOfElementLocated(caseDetVal));
        escDetailVal=escalationDetVals.getText();
        System.out.println("Escalation Details"+escDetailVal);
        String expectedCaseDetailVal = ConfigReader.get("EscalationDetail");
        if (areStringsEqualIgnoreCase(escDetailVal, expectedCaseDetailVal)) {
            test.log(Status.PASS, " Actual value of Case details selected was:" +escDetailVal+ " matched with Expected value as :" +expectedCaseDetailVal);
            System.out.println("Case Detail is matched");
        } else {
            test.log(Status.FAIL, "Mismatch: Expected '" + expectedCaseDetailVal + "', but got '" + escDetailVal + "'");
        }

    //Verifying Escalation Reason
        Thread.sleep(4000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(escalRsn)).click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement escalationRsn = wait.until(ExpectedConditions.visibilityOfElementLocated(caseReasonVal));
        escRsnVal=escalationRsn.getText();
        System.out.println("Escalation Reason value"+escRsnVal);
        String expectedCaseRsnVal = ConfigReader.get("EscalationReasonDD");
        if (areStringsEqualIgnoreCase(escRsnVal, expectedCaseRsnVal)) {
            test.log(Status.PASS, "Actual value of Case reason selected was::" +escRsnVal+ " matched with Expected value as :" +expectedCaseRsnVal);
            System.out.println("Case Reason is matched");
        } else {
            test.log(Status.FAIL, "Mismatch: Expected '" + expectedCaseRsnVal + "', but got '" + escRsnVal + "'");
        }
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

    public boolean areStringsEqualIgnoreCase(String actual, String expected) {//veena
        if (actual == null || expected == null) return false;
        return actual.trim().equalsIgnoreCase(expected.trim());
    }

    public void escalationTOFieldService() throws InterruptedException {
        CaseCreationPage obj =new CaseCreationPage(driver, wait, test);
        test.info("Escalating Case Number:" +obj.caseSerialNum);
        click(accept, "Clicked on Accept Button.");
        // Wait for Mark Status as Complete button to become clickable
        WebElement btnStatus = wait.until(ExpectedConditions.elementToBeClickable(markStatusAsCom));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnStatus);

        // Wait for Escalate button to be visible
        WebElement recClick = wait.until(ExpectedConditions.visibilityOfElementLocated(escalateBtn));
        WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(escalateBtn));
        Thread.sleep(5000);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", refreshButton);
        Thread.sleep(5000);


        // Wait for Escalate Page heading to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(escalHeading));
        test.log(Status.INFO, "Click On the Escalate Page");

        click(radioBtnField, "Selected Field Service Team for Escalation");
        wait.until(ExpectedConditions.presenceOfElementLocated(esclReason));
        Select dropdown = new Select(driver.findElement(esclReason));

        boolean reasonFound = dropdown.getOptions().stream()
                .anyMatch(option -> option.getText().trim().equals(ConfigReader.get("EscalationReasonDD")));

        if (reasonFound) {
            dropdown.selectByVisibleText(ConfigReader.get("EscalationReasonDD"));
            test.log(Status.INFO, "Selected Reason is : " +ConfigReader.get("EscalationReasonDD")+ ".");
            escalReasn =ConfigReader.get("EscalationReasonDD");
        } else {
            throw new NoSuchElementException("Reason " +ConfigReader.get("EscalationReasonDD") + " not found in dropdown.");
        }
        // Enter escalation details
        WebElement escDetail = wait.until(ExpectedConditions.elementToBeClickable(escalationdtl));
        escDetail.click();
        escDetail.sendKeys(ConfigReader.get("EscalationDetail"));
        test.log(Status.INFO, "Case Details Entered.");
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        submitButton.click();

    }


}
