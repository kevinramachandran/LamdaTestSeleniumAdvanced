import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaTest {

    WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HomePC\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void testIntegrationFlow() {
        // Step 1: Navigate to LambdaTest
        driver.get("https://www.lambdatest.com");

        // Step 2: Wait for DOM to be fully loaded (footer as indicator)
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("footer")));

        // Step 3: Scroll to "Explore all Integrations" element
        WebElement exploreLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[text()='Explore all Integrations']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", exploreLink);

        // Step 4: Open in new tab using JavaScript
        String originalWindow = driver.getWindowHandle();
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0].href, '_blank');", exploreLink);

        // Step 5: Capture window handles and print
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> driver.getWindowHandles().size() == 2);
        List<String> windows = new ArrayList<>(driver.getWindowHandles());
        System.out.println("Window handles: " + windows);

        // Switch to the new tab
        for (String handle : windows) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        // Step 6: Verify the new tab URL
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/integrations"));
        assertEquals("https://www.lambdatest.com/integrations", driver.getCurrentUrl(), "URL does not match expected!");

        // Step 7: Scroll to “Codeless Automation” section
        WebElement codelessHeader = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@id='codeless_row']/h2[contains(text(),'Codeless Automation')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", codelessHeader);

        // Step 8: Click "Integrate Testing Whiz with LambdaTest"
        WebElement testingWhizLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@id='codeless_row']//a[contains(text(),'Integrate Testing Whiz with LambdaTest')]")));
        testingWhizLink.click();

        // Step 9: Verify the title of the page
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);
        assertEquals("TestingWhiz Integration With LambdaTest", title, "Title does not match expected!");

        // Step 10: Close the current window (integration tab)
        driver.close();

        // Step 11: Switch back to original and print current window count
        driver.switchTo().window(originalWindow);
        System.out.println("Remaining Window Handles: " + driver.getWindowHandles().size());

        // Step 12: Navigate to blog page
        driver.get("https://www.lambdatest.com/blog");

        // Step 13: Click on ‘Community’ link and verify URL
        WebElement communityLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.linkText("Community")));
        communityLink.click();

        // Wait and verify redirected URL
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("community.lambdatest.com"));
        assertEquals("https://community.lambdatest.com/", driver.getCurrentUrl(), "Community URL mismatch!");

        // Step 14: Close the final browser window
        driver.quit();
    }

    /*@AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }*/
}
