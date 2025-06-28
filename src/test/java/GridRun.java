import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridRun {

    WebDriver driver;

    @ParameterizedTest
    @CsvSource({
            "chrome,128.0,Windows 10",
            "MicrosoftEdge,127.0,macOS Ventura"
    })
    void testIntegrationFlow(String browser, String version, String platform) throws Exception {
        // Setup LambdaTest capabilities
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browser);
        caps.setCapability("browserVersion", version);
        caps.setCapability("platformName", platform);

        // LambdaTest Credentials (replace with your actual username and access key)
        caps.setCapability("user", "your_lambdatest_username");
        caps.setCapability("accessKey", "your_lambdatest_access_key");

        // Enable logging capabilities
        caps.setCapability("network", true);
        caps.setCapability("video", true);
        caps.setCapability("console", true);
        caps.setCapability("visual", true);

        // Test metadata
        caps.setCapability("build", "JUnit Lambda Grid Build");
        caps.setCapability("name", "Integration Flow Test");

        driver = new RemoteWebDriver(new URL("https://hub.lambdatest.com/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Step 1: Navigate to LambdaTest
        driver.get("https://www.lambdatest.com");

        // Step 2: Wait for footer to ensure DOM is ready
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.presenceOfElementLocated(By.tagName("footer")));

        // Step 3: Scroll to "Explore all Integrations"
        WebElement exploreLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Explore all Integrations']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", exploreLink);

        // Step 4: Open the link in a new tab using JavaScript
        String originalWindow = driver.getWindowHandle();
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0].href, '_blank');", exploreLink);

        // Step 5: Wait for new window and switch
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.getWindowHandles().size() == 2);
        List<String> windows = new ArrayList<>(driver.getWindowHandles());
        System.out.println("Window handles: " + windows);

        for (String handle : windows) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        // Step 6: Verify URL
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/integrations"));
        assertEquals("https://www.lambdatest.com/integrations", driver.getCurrentUrl(), "URL does not match expected!");

        // Step 7: Scroll to Codeless Automation
        WebElement codelessHeader = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@id='codeless_row']/h2[contains(text(),'Codeless Automation')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", codelessHeader);

        // Step 8: Click on Testing Whiz link
        WebElement testingWhizLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@id='codeless_row']//a[contains(text(),'Integrate Testing Whiz with LambdaTest')]")));
        testingWhizLink.click();

        // Step 9: Verify title
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.titleContains("TestingWhiz Integration With LambdaTest"));
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);
        assertEquals("TestingWhiz Integration With LambdaTest", title, "Title does not match expected!");

        // Step 10: Close integration tab
        driver.close();

        // Step 11: Switch back to original
        driver.switchTo().window(originalWindow);
        System.out.println("Remaining Window Handles: " + driver.getWindowHandles().size());

        // Step 12: Navigate to Blog page
        driver.get("https://www.lambdatest.com/blog");

        // Step 13: Click 'Community' and verify URL
        WebElement communityLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.linkText("Community")));
        communityLink.click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("community.lambdatest.com"));
        assertEquals("https://community.lambdatest.com/", driver.getCurrentUrl(), "Community URL mismatch!");

        // Step 14: Close everything
        driver.quit();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
