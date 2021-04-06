package com.latoken.pages;

import com.latoken.enums.TradingPairs;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class ExchangePage {

    public WebDriver driver;
    private static final String ARIA_ROWINDEX = "aria-rowindex";
    private static final String tdLocator = "//div[@aria-rowindex='%s']/div[@aria-colindex]";
    private static final String tradingPairLocator = "//div[text()='%s']";
    private static final By ariaRowindexLocator = By.xpath(String.format("//div[@%s]", ARIA_ROWINDEX));

    public ExchangePage(WebDriver driver) {
        this.driver = driver;
    }

    public ExchangePage goToTradingPair(TradingPairs tradingPair) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(tradingPairLocator, tradingPair.getTradingPair()))));
        driver.findElement(By.xpath(String.format(tradingPairLocator, tradingPair.getTradingPair()))).click();
        return this;
    }

    public List<List<String>> getParsedTradingPairs() throws InterruptedException {
        List<WebElement> tradingPairsRows = new ArrayList<>(findTradingPairsRows());
        List<List<String>> parsedTradingPairsRows = new ArrayList<>();
        Integer lastAriaRowindex = 0;
//        WebDriverWait wait = new WebDriverWait(driver, 10);
        while (!getLastAriaRowindex(tradingPairsRows).equals(lastAriaRowindex)) {
            parsedTradingPairsRows.addAll(parseTradingPairsRows(tradingPairsRows));
            lastAriaRowindex = getLastAriaRowindex(tradingPairsRows);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                    tradingPairsRows.get(tradingPairsRows.size() - 1));
//            wait.ignoring(StaleElementReferenceException.class)
//                    .until(webDriver -> !webDriver.findElement(By.className("ReactVirtualized__Grid__innerScrollContainer"))
//                            .getAttribute("style").contains("pointer-events: none;"));
            sleep(1000);
            Integer finalLastAriaRowindex = lastAriaRowindex;
            List<WebElement> truncatedElementsList = findTradingPairsRows().stream().filter(el ->
                    Integer.parseInt(el.getAttribute(ARIA_ROWINDEX)) > finalLastAriaRowindex).collect(Collectors.toList());
            if (!truncatedElementsList.isEmpty()) {
                tradingPairsRows.clear();
                tradingPairsRows.addAll(truncatedElementsList);
            }
        }
        return parsedTradingPairsRows;
    }

    private List<List<String>> parseTradingPairsRows(List<WebElement> tradingPairsRows) {
        System.out.println("Parsing trading pairs rows...");
        List<List<String>> parsedTradingPairsRows = new ArrayList<>();
        for (WebElement webElement : tradingPairsRows) {
            new FluentWait<>(driver)
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                    .until(webDriver -> webDriver.findElements(By.xpath(String.format(tdLocator, webElement.getAttribute(ARIA_ROWINDEX))))
                            .stream().allMatch(WebElement::isDisplayed));
            List<String> collect = driver
                    .findElements(By.xpath(String.format(tdLocator, webElement.getAttribute(ARIA_ROWINDEX))))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
            parsedTradingPairsRows.add(collect);
        }
        System.out.println(parsedTradingPairsRows);
        return parsedTradingPairsRows;
    }

    private List<WebElement> findTradingPairsRows() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(ariaRowindexLocator)));
        return driver.findElements(ariaRowindexLocator);
    }

    private Integer getLastAriaRowindex(List<WebElement> tradingPairsElements) {
        return Integer.valueOf(tradingPairsElements.get(tradingPairsElements.size() - 1)
                .getAttribute(ARIA_ROWINDEX));
    }

}
