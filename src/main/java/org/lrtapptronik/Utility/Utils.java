package org.lrtapptronik.Utility;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.lrtapptronik.pageobject.CaseCreationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Utils   {

    public WebDriver driver;
    public WebDriverWait wait;
    public ExtentTest test;
    public By delete = By.xpath("//button[text()='Delete']");
    public By deleted = By.xpath("//button[@title='Delete']");
    By deleteBtn = By.xpath("//li[@data-target-selection-name='sfdc:StandardButton.Case.Delete']//button[text()='Delete']");

    public Utils(WebDriver driver, WebDriverWait wait, ExtentTest test)
    {
        this.driver = driver;
        this.wait = wait;
        this.test = test;

    }


        public  int convertToSeconds(String timeStr) {
            int hours = 0, minutes = 0, seconds = 0;

            String[] parts = timeStr.split(",");
            for (String part : parts) {
                part = part.trim();
                if (part.contains("hr")) {
                    hours = Integer.parseInt(part.split(" ")[0]);
                } else if (part.contains("min")) {
                    minutes = Integer.parseInt(part.split(" ")[0]);
                } else if (part.contains("sec")) {
                    seconds = Integer.parseInt(part.split(" ")[0]);
                }
            }

            return hours * 3600 + minutes * 60 + seconds;
        }
    public  int convertToSecondsForDays(String timeStr) {
        timeStr = timeStr.replace(",", ""); // remove commas
        String[] parts = timeStr.split(" ");
        int seconds = 0;

        for (int i = 0; i < parts.length; i += 2) {
            int value = Integer.parseInt(parts[i]);
            String unit = parts[i + 1].toLowerCase();

            if (unit.startsWith("day")) seconds += value * 86400;
            else if (unit.startsWith("hr")) seconds += value * 3600;
            else if (unit.startsWith("min")) seconds += value * 60;
            else if (unit.startsWith("sec")) seconds += value;
        }

        return seconds;
    }


    public void delRecord() throws InterruptedException {
        CaseCreationPage obj =new CaseCreationPage(driver, wait, test);
        test.info("Escalating Case Number to be deleted:" +obj.caseSerialNum);
        Thread.sleep(5000);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(delete));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
      // click(delete,"Deleting the created test record");
        Thread.sleep(5000);
       driver.findElement(By.xpath("//button[contains(@class, 'slds-button') and text()='Delete']")).click();
        //Thread.sleep(2000);
        driver.findElement(By.xpath("//button[@title='Delete' and .//span[text()='Delete']]")).click();

    }
    public void click(By locator, String logMessage) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
        test.log(Status.INFO, logMessage);
    }



}
