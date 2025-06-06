package org.lrtapptronik.tests.Test;


import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.lrtapptronik.Utility.Utils;
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
        loginPage.loginValidUser(ConfigReader.get("UName"), ConfigReader.get("Pwd"));
        test.log(Status.INFO, "Login is successful.");


    }


    @Test(dependsOnMethods = "loginTest")
    public void caseCreation() throws InterruptedException {
        test = extent.createTest("Case Created verified with Automated Email->customer.");
        CaseCreationPage caseCre = new CaseCreationPage(driver, wait, test);
        caseCre.openAerviceCaseNextBtn();
        caseCre.caseDetails();
    }

    @Test(dependsOnMethods = "caseCreation")
    public void caseEscalation() throws InterruptedException {
        test = extent.createTest("Case Escalation");
        CaseCreationPage caseObj = new CaseCreationPage(driver, wait, test);
        EscalationPage escPage = new EscalationPage(driver, wait, test);
        escPage.caseEscalations();
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
        test = extent.createTest("Customer Reply(Email)->CaseStatus:Customer Responded");
        CustomerReplyByEmail custoReplyEmail = new CustomerReplyByEmail(driver, wait, test);
       custoReplyEmail.custoReplyBack();

    }

   @Test(dependsOnMethods = "caseEscalation")
    public void SFtoJiraStatusUpdate() throws InterruptedException {
        test = extent.createTest("SF case status update is reflected in Jira status ");
        JiraSalesForceUpdate casetoJira = new JiraSalesForceUpdate(driver, wait, test);
        casetoJira.SFtoJira();
        test.log(Status.INFO, " Salesforce to Jira  (SF->JIRA) status is completed ");
    }

    @Test(dependsOnMethods = "caseEscalation")
    public void JiratoSFStatusUpdate() throws InterruptedException {
        test = extent.createTest("Jira ticket status update is reflected in SF Case status ");
        JiraSalesForceUpdate jiratoSF = new JiraSalesForceUpdate(driver, wait, test);
        jiratoSF.JiratoSF();
        test.log(Status.INFO, "Jira to Salesforce (JIRA-SF) status update is completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void SLATeamCheck_Test1() throws InterruptedException {
        test = extent.createTest("SLA Milestones When Case status='New' and Case Owner = 'Service Team'");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
         caseStatus.getStatus();
         /*caseStatus.acceptCaseTomarkInProgress();
          caseStatus.getStatus();->2
        caseStatus.escalateCase();
        caseStatus.getStatus();->3
        caseStatus.markAsActionRequire();->4
      caseStatus.getStatus();
        caseStatus.markWaitingOnCustomer();->5
        caseStatus.getStatus();
        caseStatus.markCustomerResponded();
        caseStatus.getStatus();->6
        caseStatus.markStatusAsVerification();
        caseStatus.getStatus();*/
        test.log(Status.INFO, " SLA test for SF Case in New status completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void SLATeamCheck_Test2() throws InterruptedException {
        test = extent.createTest("SLA Milestones When Case status='In Progress'");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
        caseStatus.acceptCaseTomarkInProgress();
        caseStatus.getStatus();
        test.log(Status.INFO, " SLA test for SF Case In Progress status completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void SLATeamCheck_Test3() throws InterruptedException {
        test = extent.createTest("SLA Milestones When Case status='Escalated'");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
        caseStatus.escalateCase();
        caseStatus.getStatus();
        test.log(Status.INFO, " SLA test for SF Case in Escalated status completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void SLATeamCheck_Test4() throws InterruptedException {
        test = extent.createTest("SLA Milestones When Case status=' Action Required'");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
        caseStatus.markAsActionRequire();
        caseStatus.getStatus();
        test.log(Status.INFO, " SLA test for SF Case in Action Required status completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void SLATeamCheck_Test5() throws InterruptedException {
        test = extent.createTest("SLA Milestones When Case status='Waiting on Customer'");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
        caseStatus.markWaitingOnCustomer();
        caseStatus.getStatus();
        test.log(Status.INFO, " SLA test for SF Case in Waiting on Customer status completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void SLATeamCheck_Test6() throws InterruptedException {
        test = extent.createTest("SLA Milestones When Case status=' Customer Responded'");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
        caseStatus.markCustomerResponded();
        caseStatus.getStatus();
        test.log(Status.INFO, " SLA test for SF Case in Customer Responded status completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void SLATeamCheck_Test7() throws InterruptedException {
        test = extent.createTest("SLA Milestones When Case status='Verification'");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
        caseStatus.markStatusAsVerification();
        caseStatus.getStatus();
        test.log(Status.INFO, " SLA test for SF Case in Verification status completed");
    }
    @Test(dependsOnMethods = "caseCreation")
    public void ChangeInFieldNoChangeInSLA() throws InterruptedException {
        test = extent.createTest("Any Change in Field DO NOT CHANGE SLA Assigned");
        SLATimeAndStatus caseStatus = new SLATimeAndStatus(driver, wait, test);
        caseStatus.changeAnyFieldNoChangeInSLA();
        test.log(Status.INFO, " Change in Internal Priority do not change SLA Assigned test is completed");
    }

}
