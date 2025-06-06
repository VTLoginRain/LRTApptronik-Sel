package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.lrtapptronik.Utility.ConfigReader;
import org.lrtapptronik.Utility.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;



public class SLATimeAndStatus {

    public By save= By.xpath("//button[normalize-space()='Save']");
    public By medPriority = By.xpath("//span[@title='Medium']");
    public By priDropdown =By.xpath("//button[@aria-label='Internal Priority']");
    public By editPriority =By.xpath("//button[@title='Edit Internal Priority']");
    public By internalPri= By.xpath("//span[contains(@class, 'test-id__field-label') and normalize-space()='Internal Priority']");
    public By averageTime =By.xpath("//a[normalize-space()='Average Blocker Time']");
    public By nxtButton = By.xpath("//button[@part='button' and text()='Next']");
    public By escalationdtl = By.xpath("//textarea[@class='slds-textarea' and @part='textarea']");
    public By escalateBtn = By.xpath("//button[text()='Escalate']");
    public By escalHeading = By.xpath("//h2[text()='Escalate']");
    public By esclReason = By.xpath("//select[@name='Escalation_Reason']");
    public By radioBtn = By.xpath("//span[text()='Engineering Team']/ancestor::label/span");
    public By accept = By.xpath("//button[text()='Accept']");
    public By markStatusAsCom = By.xpath("//button[.//span[text()='Mark Status as Complete']]");
    public By SFCaseStatus = By.xpath("//p[@title='Status']/following-sibling::p//lightning-formatted-text");
    public By mileStones = By.xpath("//span[normalize-space(text())='Milestones']");
    public By timetoAct = By.xpath("//span[normalize-space(text())='Time to Take Action']");
    public By SLATime =By.xpath("//a[@title='Time to Take Action']/ancestor::div[contains(@class, 'slds-col')]/div[contains(@class,'milestoneTimerText')]");
    public By markStatus = By.xpath("//button[contains(@class,'slds-path__mark-complete') and .//span[normalize-space()='Mark as Current Status']]");
    public By caseOwnVal = By.xpath("//span[contains(@class, 'owner-name')]//span[normalize-space(text())='Service Team']");
    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest test;
    public String escalReasn ;
    public String actualValueOwner;

    public String caseStatus ;
    Utils clickBtn;
    public SLATimeAndStatus(WebDriver driver, WebDriverWait wait, ExtentTest test) {
        this.driver = driver;
        this.wait = wait;
        this.test = test;

        clickBtn=new Utils(driver,  wait,  test);
    }


    public void getStatus() throws InterruptedException {

        String fullTextNew  = driver.findElement(SFCaseStatus).getText();
         caseStatus = fullTextNew.replace("Status:", "").trim();
        System.out.println("SalesForce Case Status: " + caseStatus);
        test.log(Status.INFO, " A Salesforce ticket is  : " +caseStatus);
        switch (caseStatus) {
            case "New":
                caseStatusNew();
                break;
            case "In Progress":
                caseStatusInProgress();
                break;
            case "Escalated":
                caseIsInEscalated();
                break;
            case "Action Required":
                caseIsInActionRequire();
                break;
            case "Waiting on Customer":
                caseInWaitingonCustomer();
                break;
            case "Customer Responded":
                caseInCustomerResponded();
                break;
            case "Verification":
                caseInVerification();
                break;
            default:
                System.out.println("Invalid Case Status");
        }

    }

    public void caseStatusNew() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript(
                "const banner = document.querySelector(\"[class*='slds-notify']\"); if (banner) banner.style.display = 'none';"
        );
        test.info("A Salesforce ticket is created and its Status is New");
        WebElement milestonesView =  wait.until(ExpectedConditions.visibilityOfElementLocated(mileStones));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", milestonesView);
        Thread.sleep(3000);

        String text=driver.findElement(timetoAct).getText();
        System.out.println(text);
        Thread.sleep(3000);
        String timerText  = driver.findElement(SLATime).getText();
        String result = timerText.replace(" remaining", "").trim();
    //  System.out.println("Check SLA Remaining Time for :" + text+ " is "+ result);
        test.pass("SLA Remaining Time for :" +text+ "is "+ timerText);
        String fisrtSLA= ConfigReader.get("fisrtSLA");
        int slaSeconds =clickBtn.convertToSeconds(fisrtSLA) ;
        int triggeredSeconds = clickBtn.convertToSeconds(result);
        if (triggeredSeconds < slaSeconds) {
          //  System.out.println("SLA triggered time is within than assigned time");
            test.pass("SLA triggered time is within the assigned time i.e. Remaining SLA time is  :" +timerText +"and assigned SLA time is " +fisrtSLA);
        } else {
            System.out.println("SLATriggeredtime is NOT less than SLAtime");
        }

        //  Thread.sleep(20000);
        //verify Case Owner as Service Team
        WebElement detailsTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@role='tab' and @data-label='Details']")
        ));
        detailsTab.click();
        Thread.sleep(5000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Read Case Owner name
        WebElement caseOwnerName = wait.until(ExpectedConditions.visibilityOfElementLocated(caseOwnVal ));
        System.out.println("Case Owner: " + caseOwnerName.getText());
        test.pass("Case Owner for the New Salesforce case  is : " + caseOwnerName.getText());

    }
    public void changeAnyFieldNoChangeInSLA() throws InterruptedException {

        WebElement milestonesView =  wait.until(ExpectedConditions.visibilityOfElementLocated(mileStones));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", milestonesView);
        Thread.sleep(3000);

        String text=driver.findElement(timetoAct).getText();
        System.out.println("SLA Time " +text );
        String timerText  = driver.findElement(SLATime).getText();
        String result = timerText.replaceAll(", \\d+ sec.*", "");
        System.out.println("SLA Remaining Time for :" + text+ " is "+ result);
        int slaBefore=clickBtn.convertToSeconds(result);
        WebElement internalPriorityLabel = wait.until(ExpectedConditions.presenceOfElementLocated(internalPri));
// Scroll to it using JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", internalPriorityLabel);
        wait.until(ExpectedConditions.elementToBeClickable(editPriority)).click();
        wait.until(ExpectedConditions.elementToBeClickable(priDropdown)).click();

         wait.until(ExpectedConditions.elementToBeClickable(medPriority)).click();

         wait.until(ExpectedConditions.elementToBeClickable(save)).click();

        test.pass("Internal Priority is changes to Medium and Saved");
        String text1=driver.findElement(timetoAct).getText();
        System.out.println("SLA Time for " +text+ "is");
        String timerText1  = driver.findElement(SLATime).getText();
        String result1 = timerText1.replaceAll(", \\d+ sec.*", "");
        int slaAfter=clickBtn.convertToSeconds(result1);
        if (slaBefore == slaAfter) {
            System.out.println("Both time strings are equal.");
            test.pass("SLA Assigned do not change when priority is changed. SLA (In Seconds) before Change in Priority is :"+slaBefore+ " after change in Priority is:" +slaBefore+" are equal.");
        } else {
            System.out.println("Time strings are NOT equal.");
        }
        System.out.println("After SLA Remaining Time for :" + text+ " is "+ result1);

    }

    public void acceptCaseTomarkInProgress() throws InterruptedException {
        clickBtn.click(accept, "Clicked on Accept Button.");
        WebElement btnStatus = wait.until(ExpectedConditions.elementToBeClickable(markStatusAsCom));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnStatus);
        Thread.sleep(3000);
        test.info("A Salesforce ticket is accepted  and marked as status as completed.");
    }
    public void escalateCase() throws InterruptedException {

        WebElement recClick = wait.until(ExpectedConditions.visibilityOfElementLocated(escalateBtn));
        WebElement refreshButton = wait.until(ExpectedConditions.elementToBeClickable(escalateBtn));
        Thread.sleep(5000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", refreshButton);
        Thread.sleep(5000);

        // Wait for Escalate Page heading to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(escalHeading));
        test.log(Status.INFO, "Click On the Escalate Page");
        clickBtn.click(radioBtn, "Selected Engineering Team for Escalation");

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
        clickBtn.click(nxtButton, "Proceeded to JIRA Details by Clicking on Next button.");
        EscalationPage markEscalation =new EscalationPage(driver,  wait,  test);;
        markEscalation.fillJiraDetails();
        Thread.sleep(10000);

        test.info("Salesforce case is escalated.");


    }
    public void caseStatusInProgress() throws InterruptedException {
        milestoneRemainingTime("Initial Diagnosis");

    }
    public void caseIsInEscalated() throws InterruptedException {
        milestoneRemainingTime("Average Blocker Time");

    }

    public void markAsActionRequire() throws InterruptedException {
        markStatusAsPerLabel("Action Required");

    }

public void caseIsInActionRequire() throws InterruptedException {
        milestoneRemainingTime("Action Required");

}
  public void markWaitingOnCustomer() throws InterruptedException {
        markStatusAsPerLabel("Waiting on Customer");

    }

public void caseInWaitingonCustomer() throws InterruptedException {
    milestoneRemainingTime("Take Follow-up from Customer");


}
    public void markCustomerResponded() throws InterruptedException {

        markStatusAsPerLabel("Customer Responded");

    }
    public void caseInCustomerResponded() throws InterruptedException {
        milestoneRemainingTime("Customer Response Follow-Up");
    }
    public void markStatusAsVerification() throws InterruptedException {
        markStatusAsPerLabel("Verification");

    }
    public void caseInVerification() throws InterruptedException {
        milestoneRemainingTime("Turnaround Time");

    }

    public void milestoneRemainingTime(String label) throws InterruptedException {

        WebElement milestonesView =  wait.until(ExpectedConditions.visibilityOfElementLocated(mileStones));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", milestonesView);
        Thread.sleep(3000);
        WebElement actionRequiredText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@title='"+label+"']/slot/span")
        ));
        String text = actionRequiredText.getText();
        System.out.println("Text is: " + text);
        WebElement valueElement = driver.findElement(By.xpath("//a[@title='"+label+"']/ancestor::div[contains(@class, 'slds-col')]/div[contains(@class, 'milestoneTimerText')]"));
        String remainingTime = valueElement.getText();
        String result = remainingTime.replace(" remaining", "").trim();
        int triggeredSeconds = clickBtn.convertToSeconds(result);
        System.out.println("Remaining Time at GUI is : " + result);
      test.pass("Milestone Remaining time for " + text + "is: " + remainingTime);

    }

   public void markStatusAsPerLabel(String label) throws InterruptedException {

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    WebElement actionRequireTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//a[contains(@class,'slds-path__link')]//span[normalize-space()='"+label+"']")
    ));
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", actionRequireTab);
    WebElement markAsCurrentStatusButton = wait.until(ExpectedConditions.elementToBeClickable(markStatus));

    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", markAsCurrentStatusButton);
    Thread.sleep(5000);
     test.info("Status is marked as Complete as :" +label);

}


}
