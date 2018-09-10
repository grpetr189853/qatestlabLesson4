package myprojects.automation.assignment4;


import com.sun.glass.ui.View;
import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.tests.CreateProductTest;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.EventHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.runtime.GlobalFunctions.parseInt;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    /**
     * Logs in to Admin Panel.
     * @param login
     * @param password
     */
    public void login(String login, String password) {
        driver.get(Properties.getBaseAdminUrl());
        driver.findElement(By.id("email")).sendKeys(login);
        driver.findElement(By.id("passwd")).sendKeys(password);
        driver.findElement(By.name("submitLogin")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    }

    public String createProduct(ProductData newProduct) {
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        ClickOnGoodsMenu(driver);
        By newGood = By.id("page-header-desc-configuration-add");
        waitForContentLoad(newGood);
        WebElement newGoodElement = driver.findElement(newGood);
        newGoodElement.click();

        By productNameLocator = By.id("form_step1_name_1");
        waitForContentLoad(productNameLocator);
        WebElement productName = driver.findElement(productNameLocator);
        productName.sendKeys(newProduct.getName());

        WebElement productQty = driver.findElement(By.id("form_step1_qty_0_shortcut"));
        productQty.sendKeys(Keys.BACK_SPACE);
        productQty.sendKeys(Integer.toString(newProduct.getQty()));
        WebElement productPrice = driver.findElement(By.id("form_step1_price_shortcut"));
        for (int i = 0; i < 10; i++ ) {
            productPrice.sendKeys(Keys.BACK_SPACE);
        }
        productPrice.sendKeys(newProduct.getPrice());

        WebElement toggleActive = driver.findElement(By.className("switch-input"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", toggleActive);

        WebElement buttonSubmit = driver.findElement(By.className("js-btn-save"));
        buttonSubmit.click();

        String mainWindowHandle = driver.getWindowHandle();
        WebElement growl = driver.findElement(By.id("growls"));
        wait.until(ExpectedConditions.invisibilityOf(growl));

        return mainWindowHandle;
    }

    public void ClickOnGoodsMenu(WebDriver driver) {
        By menuCatalog = null;
        menuCatalog = By.xpath("//li[contains(@data-submenu,'9')]");
        //wait.until(ExpectedConditions.elementToBeSelected(menuCatalog));
        waitForContentLoad(menuCatalog);
        WebElement menuHoverLink = driver.findElement(menuCatalog);

        actions = new Actions(driver);
        actions.moveToElement(menuHoverLink).perform();
        WebDriverWait wait = new WebDriverWait(driver, 20);

        By menuGoods = By.xpath("//li[contains(@data-submenu,'10')]");
        waitForContentLoad(menuGoods);
        WebElement menuGoodsElement = driver.findElement(menuGoods);
        wait.until(ExpectedConditions.elementToBeClickable(menuGoods));
        if(menuGoodsElement != null )
            menuGoodsElement.click();
    }

    public void CheckProduct(ProductData newProduct) {

        String originalWindow = driver.getWindowHandle();
        final Set<String> oldWindowsSet = driver.getWindowHandles();

//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("prestashop-automation")));
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("prestashop-automation")));

        driver.findElement(By.partialLinkText("prestashop-automation")).click();

        String newWindow = (new WebDriverWait(driver, 10))
                .until(new ExpectedCondition<String>() {
                           public String apply(WebDriver driver) {
                               Set<String> newWindowsSet = driver.getWindowHandles();
                               newWindowsSet.removeAll(oldWindowsSet);
                               return newWindowsSet.size() > 0 ?
                                       newWindowsSet.iterator().next() : null;
                           }
                       }
                );

        driver.switchTo().window(newWindow);

        List<WebElement> allGoods = driver.findElements(By.className("all-product-link"));
        WebElement goods = allGoods.get(0);
        goods.click();

        WebElement good = driver.findElement(By.partialLinkText(newProduct.getName()));

        Assert.assertEquals(good.getText(),newProduct.getName());

        good.click();

        WebElement productTitle = driver.findElement(By.xpath("//h1[contains(@itemprop,'name')]"));
        Assert.assertEquals(productTitle.getText().toLowerCase(),newProduct.getName().toLowerCase());

        WebElement productTargetPrice = driver.findElement(By.xpath("//span[contains(@itemprop,'price')]"));
        Assert.assertEquals(Float.parseFloat(convertNumbers(productTargetPrice.getText().trim())),Float.parseFloat(convertNumbers(newProduct.getPrice().trim())));

        WebElement productTargetQty = driver.findElement(By.xpath("//div[contains(@class,'product-quantities')]/span"));
        Assert.assertEquals((Integer) Integer.parseInt(convertNumbers(productTargetQty.getText().trim())),newProduct.getQty());
    }
    public void deleteUnusedProduct(String mainWindowHandle) {
        //delete element

        driver.switchTo().window(mainWindowHandle);
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0, document.body.scrollWidth);");
        WebElement deleteButton = driver.findElement(By.partialLinkText("delete"));
        js.executeScript("arguments[0].click();", deleteButton);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn.btn-primary.btn-lg.continue")));
        driver.findElement(By.cssSelector(".btn.btn-primary.btn-lg.continue")).click();
    }
    public String convertNumbers(String inputNumberInStrFormat) {
        String outputNumberInStrFormat = "";
        for(int i = 0; i < inputNumberInStrFormat.length(); i++) {
            if(inputNumberInStrFormat.charAt(i) == 44) {
                outputNumberInStrFormat += ".";
            } else if((inputNumberInStrFormat.charAt(i) >= 48 && inputNumberInStrFormat.charAt(i) <= 57)  || inputNumberInStrFormat.charAt(i) == 46 ) {
                outputNumberInStrFormat += inputNumberInStrFormat.charAt(i);
            }

        }
        return outputNumberInStrFormat;
    }
    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad(By somethingLocator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(somethingLocator));
    }

}
