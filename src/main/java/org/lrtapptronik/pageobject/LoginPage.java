package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.lrtapptronik.Utility.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class LoginPage  extends Utils {



    public By emailInput = By.id("username");
    public By passwordInput = By.id("password");
    public By loginButton = By.id("Login");
    public By showNavMenu = By.xpath("//button[@title='Show Navigation Menu']");

    public LoginPage(WebDriver driver, WebDriverWait wait, ExtentTest test) {

        super(driver, wait, test);
    }

    public void enterUsername(String username) {
        driver.findElement(emailInput).sendKeys(username);
        test.log(Status.INFO, "Entered username."); //veena  -> why not this is getting print.
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
        test.log(Status.INFO, "Entered password.");
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
        test.log(Status.INFO, "Clicked Login.");
    }

    public void loginValidUser(String username, String password) {
        try{
            enterUsername(username);
            enterPassword(password);
            clickLogin();

        } catch (NoSuchElementException | TimeoutException e) {
            test.log(Status.FAIL, "Test failed due to missing element or incorrect XPath: " + e.getMessage());
            Assert.fail("Test failed due to missing element or incorrect XPath.");
        }
 try {
            wait.until(ExpectedConditions.elementToBeClickable(showNavMenu));
            test.log(Status.PASS, "Login successful.");
        }

catch (TimeoutException e){
            test.log(Status.FAIL, "Login failed due to invalid credentials.");
            Assert.fail("Login failed: Invalid credentials or login element not found.");

        }


    }

}
