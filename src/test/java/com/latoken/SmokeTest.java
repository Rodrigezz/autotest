package com.latoken;

import com.latoken.enums.TradingPairs;
import com.latoken.pages.ExchangePage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static java.lang.Thread.sleep;

public class SmokeTest extends BaseTest {

    @Test(priority = 4)
    public void UsdtTradingPairsTest() throws InterruptedException {
        driver.get("https://latoken.com/exchange");
        ExchangePage exchangePage = new ExchangePage(driver);
        parseAndAssertAllCells(exchangePage);
    }

    @Test(priority = 1)
    public void BtcTradingPairsTest() throws InterruptedException {
        driver.get("https://latoken.com/exchange");
        ExchangePage exchangePage = new ExchangePage(driver);
        exchangePage.goToTradingPair(TradingPairs.BTC);
        parseAndAssertAllCells(exchangePage);
    }

    @Test(priority = 2)
    public void EthTradingPairsTest() throws InterruptedException {
        driver.get("https://latoken.com/exchange");
        ExchangePage exchangePage = new ExchangePage(driver);
        exchangePage.goToTradingPair(TradingPairs.ETH);
        parseAndAssertAllCells(exchangePage);
    }

    @Test
    public void TrxTradingPairsTest() throws InterruptedException {
        driver.get("https://latoken.com/exchange");
        ExchangePage exchangePage = new ExchangePage(driver);
        exchangePage.goToTradingPair(TradingPairs.TRX);
        parseAndAssertAllCells(exchangePage);
    }

    @Test(priority = 3)
    public void LaTradingPairsTest() throws InterruptedException {
        driver.get("https://latoken.com/exchange");
        ExchangePage exchangePage = new ExchangePage(driver);
        exchangePage.goToTradingPair(TradingPairs.LA);
        parseAndAssertAllCells(exchangePage);
    }

    private void parseAndAssertAllCells(ExchangePage exchangePage) throws InterruptedException {
        sleep(2000);
        List<List<String>> parsedTradingPairs = exchangePage.getParsedTradingPairs();
        SoftAssert softly = new SoftAssert();
        for (List<String> row : parsedTradingPairs) {
            for (int i = 0; i < row.size() - 1; i++) {
                if (i == 6 || i == 8) {
                    continue;
                }
                String td = row.get(i);
                softly.assertTrue(!td.isEmpty(), String.format("Row: %s failed on a td: %s ---> isn't empty", row, td));
                if (i == 2)
                    softly.assertTrue(td.contains("$"), String.format("Row: %s failed on a td: %s ---> USD Price doesn't contain a $ sign", row, td));
                if (i == 4)
                    softly.assertTrue(td.contains("$") || td.contains("--"), String.format("Row: %s failed on a td: %s ---> Market cap doesn't contain a $ sign or --", row, td));
                if (i == 5 || i == 7)
                    softly.assertTrue(td.contains("%"), String.format("Row: %s failed on a td: %s ---> Chg 24h or Chg 7d doesn't contain percent sign", row, td));
                if (i == 9)
                    softly.assertTrue(td.equalsIgnoreCase("trade"), String.format("Row: %s failed on a td: %s ---> Td doesn;t contain a 'Trade'", row, td));
            }
        }
        softly.assertAll();
    }
}
