package com.latoken;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {

    WebDriver driver;

    @BeforeTest
    public void setUp() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("win")) {
            System.setProperty("webdriver.chrome.driver","drivers\\chromedriver.exe");
        } else if (osName.toLowerCase().contains("nux")) {
            System.setProperty("webdriver.chrome.driver","drivers/chromedriver");
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
    }

    @AfterTest
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
