package org.lrtapptronik.pageobject;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.lrtapptronik.Utility.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CaseCreationPage {

    public By showNavMenu = By.xpath("//button[@title='Show Navigation Menu']");
    public By clickCases = By.xpath("//span[contains(text(), 'Cases')]");
    public By newButton = By.xpath("//div[@title='New']");
    public By accComboBox = By.xpath("//label[normalize-space(text())='Account']/parent::lightning-grouped-combobox//input");
    public By accVal= By.xpath("//span[@title='"+ ConfigReader.get("Account")+"']");
    public By contactSelectBy = By.xpath("//select[@name='Contact']");
    public By nextBtn = By.xpath("//button[text()='Next']");
    public By txtSubject= By.xpath("//input[@name='Subject']");
    public By caseDetails = By.xpath("//button[text()='Next']");
    public By clickSubmit = By.xpath("//button[text()='Submit']");
    public By caseNumber = By.xpath("//p[@title='Case Number']/parent::div//lightning-formatted-text");
    public By openedFeed =By.xpath("//a[text()='Feed']");

    private WebDriver driver;
    private WebDriverWait wait;
    private ExtentTest test;
    public  static String caseSerialNum;

    public CaseCreationPage(WebDriver driver, WebDriverWait wait, ExtentTest test) {
        this.driver = driver;
        this.wait = wait;
        this.test = test;
    }

    public void openAerviceCaseNextBtn() throws InterruptedException {
        click(showNavMenu, "Opened navigation menu.");
        click(clickCases, "Clicked Cases link.");
        wait.until(ExpectedConditions.elementToBeClickable(newButton)).click();
        test.log(Status.INFO, "Clicked On New Button to create case .");

        wait.until(ExpectedConditions.elementToBeClickable(accComboBox)).click();
        test.log(Status.INFO, "Clicked Account field.");
        wait.until(ExpectedConditions.elementToBeClickable(accVal)).click();
        Thread.sleep(3500);
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

        click(nextBtn,"Clicked Next (Customer Contact Details).");


    }
    public void caseDetails(){

        wait.until(ExpectedConditions.elementToBeClickable(txtSubject)).sendKeys(ConfigReader.get("Subject"));
        test.log(Status.INFO, "Entered Subject: " +ConfigReader.get("Subject")+ ".");

        click(caseDetails,"Clicked Next (Case Details).");
        click(clickSubmit,"Clicked Submit.");

        WebElement caseNum =wait.until(ExpectedConditions.presenceOfElementLocated(caseNumber));

         caseSerialNum = caseNum.getText();
        test.log(Status.PASS, "Case Number : " + caseSerialNum);
        test.log(Status.INFO, "Verifying email now");
        click(openedFeed,"Opened Feed tab.");

        try{
            WebElement verifyContent = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[contains(@class, 'emailMessageBody') and contains(text(), 'Your case has been created')]")));
            test.log(Status.PASS, "Verified email content: " + verifyContent.getText());
        } catch (NoSuchElementException | TimeoutException e) {
            test.log(Status.FAIL, "Email verification failed: " + e.getMessage());
            Assert.fail("Email not generated or XPath issue.");
        } catch (Exception e) {
            test.log(Status.FAIL, "Case creation failed: " + e.getMessage());
            Assert.fail("Failure during case creation flow.");
        }

    }

    private void click(By locator, String logMessage) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();
        test.log(Status.INFO, logMessage);
    }

}
