package org.lrtapptronik.tests.Test;


import com.aventstack.extentreports.Status;
import org.lrtapptronik.pageobject.CaseCreationPage;
import org.lrtapptronik.Utility.ConfigReader;
import org.lrtapptronik.pageobject.EscalationPage;
import org.lrtapptronik.pageobject.LoginPage;
import org.testng.annotations.Test;


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
        CaseCreationPage caseCre = new CaseCreationPage(driver,wait, test);
        test.log(Status.INFO, "Calling caseCreation().openAerviceCaseNextBtn() method. ");
        caseCre.openAerviceCaseNextBtn();
        test.log(Status.INFO, "Calling caseCreation().caseDetails() method. ");
        caseCre.caseDetails();
    }

    @Test(dependsOnMethods = "caseCreation")
    public void caseEscalation() {
        test = extent.createTest("Case Escalation");
        CaseCreationPage caseObj = new CaseCreationPage(driver, wait, test);
        test.log(Status.INFO, "Escalating Case Number: " + caseObj.caseSerialNum);
        test.log(Status.INFO, "Calling caseEscalation().caseEscalations() method. ");
        EscalationPage escPage = new EscalationPage(driver, wait, test);
        escPage.caseEscalations();
        test.log(Status.INFO, "Calling caseEscalation().fillJiraDetails() method. ");
        escPage.fillJiraDetails();
    }
}
