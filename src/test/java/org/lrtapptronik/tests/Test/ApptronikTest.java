package org.lrtapptronik.tests.Test;


import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.lrtapptronik.pageobject.*;
import org.lrtapptronik.Utility.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;


public class ApptronikTest extends BaseTest {

    @Test
    public void loginTest() {
        test = extent.createTest("Login Test");
        driver.get(ConfigReader.get("URL"));
        test.log(Status.INFO, "Navigated to login page: " + driver.getTitle());
        LoginPage loginPage = new LoginPage(driver, wait, test);
        test.log(Status.INFO, "Calling loginValidUser() to login.");
        loginPage.loginValidUser(ConfigReader.get("UName"), ConfigReader.get("Pwd"));
        test.log(Status.INFO, "Login is successful.");

    }


    @Test(dependsOnMethods = "loginTest")
    public void caseCreation() throws InterruptedException {

        test = extent.createTest("Automated Email Verification on Case Creation");
        CaseCreationPage caseCre = new CaseCreationPage(driver, wait, test);
        test.log(Status.INFO, "Calling caseCreation().openAerviceCaseNextBtn() method. ");
        caseCre.openAerviceCaseNextBtn();
        test.log(Status.INFO, "Calling caseCreation().caseDetails() method. ");
        caseCre.caseDetails();

    }

    @Test(dependsOnMethods = "caseCreation")
    public void caseEscalation() throws InterruptedException {
        test = extent.createTest("Case Escalation");
        CaseCreationPage caseObj = new CaseCreationPage(driver, wait, test);
        test.log(Status.INFO, "Escalating Case Number: " + caseObj.caseSerialNum);
        test.log(Status.INFO, "Calling caseEscalation().caseEscalations() method. ");
        EscalationPage escPage = new EscalationPage(driver, wait, test);
        escPage.caseEscalations();
        test.log(Status.INFO, "Calling caseEscalation().fillJiraDetails() method. ");
        escPage.fillJiraDetails();
        // Verification
        escPage.verifyEscalationFieldVal();

    }

    @Test (dependsOnMethods = "caseEscalation")
    public void ClosureReason() throws InterruptedException
    {

        test = extent.createTest("Closing the Salesforce Ticket with Closure Reason");
        ClosedReason closeRsn = new ClosedReason(driver, wait, test);
        closeRsn.closeBtnClick();
        test.log(Status.INFO, "Closure Reason is mandatory to be selected. ");
    }


    @Test(dependsOnMethods = "caseCreation" )
    public void custoReply() throws InterruptedException {
        test = extent.createTest("Customer Reply By an Email present at Contact");
        CustomerReplyByEmail custoReplyEmail = new CustomerReplyByEmail(driver, wait, test);
       custoReplyEmail.custoReplyBack();


    }


}
