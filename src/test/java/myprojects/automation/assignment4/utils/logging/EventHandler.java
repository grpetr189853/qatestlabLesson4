package myprojects.automation.assignment4.utils.logging;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;


public class EventHandler extends AbstractWebDriverEventListener {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(EventHandler.class);

    @Override
    public void beforeAlertAccept(WebDriver webDriver) {
    }

    @Override
    public void afterAlertAccept(WebDriver webDriver) {
    }

    @Override
    public void afterAlertDismiss(WebDriver webDriver) {
    }

    @Override
    public void beforeAlertDismiss(WebDriver webDriver) {
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        LOGGER.info("Before navigating to: '" + url + "'");
        CustomReporter.log("Navigate to " + url);
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        LOGGER.info("WebDriver navigated to '" + url + "'");
        CustomReporter.log("Done navigating to" + url);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        LOGGER.info("Navigate back");
        CustomReporter.log("Navigate back");
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        LOGGER.info("After Navigate back, URL" + driver.getCurrentUrl());
        CustomReporter.log("Current URL: " + driver.getCurrentUrl());
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        LOGGER.info("Navigate forward");
        CustomReporter.log("Navigate forward");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        LOGGER.info("After Navigate forward, URL" + driver.getCurrentUrl());
        CustomReporter.log("Current URL: " + driver.getCurrentUrl());
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        LOGGER.info("Refresh page");
        CustomReporter.log("Refresh page");
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        LOGGER.info("After refresh, URL" + driver.getCurrentUrl());
        CustomReporter.log("Current URL: " + driver.getCurrentUrl());
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        LOGGER.info("Trying to find Element By : " + by.toString());
        CustomReporter.log("Search for element " + by.toString());
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        LOGGER.info("Found Element By : " + by.toString());
        if (element != null) {
            CustomReporter.log("Found element " + element.getTagName());
        }
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        if (element != null) {
            LOGGER.info("WebDriver clicking on element - "
                    + (element));
            CustomReporter.log("Click on element " + element.getTagName());
        }
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        if(element != null) {

            LOGGER.info("WebDriver clicked on element - "
                    + (element));


            CustomReporter.log("WebDriver clicked on element - "
                    + element);//.getTagName()

        }
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        LOGGER.info("WebDriver sending  '" + keysToSend + "to element '" + element + "'");
        String value = Arrays.stream(keysToSend).map(CharSequence::toString).collect(Collectors.joining());
        CustomReporter.log(String.format("Change value of %s: %s\n", element.getTagName(), value));
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        LOGGER.info("WebDriver sended  '" + keysToSend + "to element '" + element + "'");
        CustomReporter.log(String.format("Changed element " + element.getTagName()));
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        LOGGER.info("Execute script: " + script);
        CustomReporter.log("Execute script: " + script);
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        LOGGER.info("Script executed");
        CustomReporter.log("Script executed");
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        // already logged by reporter
    }
}
