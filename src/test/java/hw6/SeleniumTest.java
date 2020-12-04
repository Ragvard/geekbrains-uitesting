package hw6;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;


public class SeleniumTest {
    private WebDriver driver;
    private static Logger logger = LoggerFactory.getLogger(SeleniumTest.class);

    @BeforeAll
    public static void setupClass() {
        logger.debug("Начало тестирования");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        logger.debug("Начало теста");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown() {
        logger.debug("Конец теста");
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    static public void close() {
        logger.debug("Конец тестирования");
    }

    @Test
    public void testNavigationTrendingmusic() {
        WebElement result = new YouTubePage(driver, logger)
                .menuClickTrending()
                .trendingClickMusic()
                .trendingGetSectionname();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-section-list-renderer", result.getAttribute("class"));
        assertEquals("text-header-renderer-style-bold", result.getAttribute("header-style"));

        driver.close();
    }

    @Test
    public void testSearchSearchbytitle() {
        WebElement result = new YouTubePage(driver, logger)
                .searchVideo("1943 British tank development in a nutshell")
                .getVideoTitle();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-video-primary-info-renderer", result.getAttribute("class"));
        assertEquals("1943 British tank development in a nutshell", result.getText());

        driver.close();
    }

    @Test
    public void testLikesLikevideo() {
        WebElement likebutton = new YouTubePage(driver, logger)
                .searchVideo("This Video Has 26,124,676 Views")
                .videoLike()
                .getVideoLikebutton();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", likebutton.getAttribute("class"));

        driver.close();
    }

    @Test
    public void testLikesDislikevideo() {
        WebElement dislikebutton = new YouTubePage(driver, logger)
                .searchVideo("cat")
                .videoDislike()
                .getVideoLikebutton();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", dislikebutton.getAttribute("class"));

        driver.close();
    }

    @Test
    public void testSubscriptionSubscribe() {
        WebElement subscribebutton = new YouTubePage(driver, logger)
                .searchChannel("Sabaton")
                .channelSubscribe()
                .getChannelSubscribebutton();

        logger.debug("Начало проверок");
        assertEquals("true", subscribebutton.getAttribute("subscribed"));

        driver.close();
    }

    @Test
    public void testSubscriptionUnsubscribe() {
        WebElement subscribebutton = new YouTubePage(driver, logger)
                .searchChannel("Sabaton")
                .channelUnsubscribe()
                .getChannelSubscribebutton();

        logger.debug("Начало проверок");
        assertEquals("null", subscribebutton.getAttribute("subscribed"));

        driver.close();
    }
}
