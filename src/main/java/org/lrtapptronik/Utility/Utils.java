package org.lrtapptronik.Utility;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils   {

    public WebDriver driver;
    public WebDriverWait wait;
    public ExtentTest test;

    public Utils(WebDriver driver, WebDriverWait wait, ExtentTest test)
    {
        this.driver = driver;
        this.wait = wait;
        this.test = test;

    }

}
