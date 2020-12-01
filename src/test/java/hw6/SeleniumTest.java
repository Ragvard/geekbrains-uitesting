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
    public void test_navigation_trendingmusic() {
        WebElement result = new YTPage(driver, logger)
                .menu_clicktrending()
                .trending_clickmusic()
                .trending_getsectionname();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-section-list-renderer", result.getAttribute("class"));
        assertEquals("text-header-renderer-style-bold", result.getAttribute("header-style"));

        driver.close();
    }

    @Test
    public void test_search_searchbytitle() {
        WebElement result = new YTPage(driver, logger)
                .searchvideo("1943 British tank development in a nutshell")
                .getVideo_title();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-video-primary-info-renderer", result.getAttribute("class"));
        assertEquals("1943 British tank development in a nutshell", result.getText());

        driver.close();
    }

    @Test
    public void test_likes_likevideo() {
        WebElement likebutton = new YTPage(driver, logger)
                .searchvideo("This Video Has 26,124,676 Views")
                .video_like()
                .getVideo_likebutton();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", likebutton.getAttribute("class"));

        driver.close();
    }

    @Test
    public void test_likes_dislikevideo() {
        WebElement dislikebutton = new YTPage(driver, logger)
                .searchvideo("cat")
                .video_dislike()
                .getVideo_likebutton();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", dislikebutton.getAttribute("class"));

        driver.close();
    }

    @Test
    public void test_subscription_subscribe() {
        WebElement subscribebutton = new YTPage(driver, logger)
                .searchchannel("Sabaton")
                .channel_subscribe()
                .getChannel_subscribebutton();

        logger.debug("Начало проверок");
        assertEquals("true", subscribebutton.getAttribute("subscribed"));

        driver.close();
    }

    @Test
    public void test_subscription_unsubscribe() {
        WebElement subscribebutton = new YTPage(driver, logger)
                .searchchannel("Sabaton")
                .channel_unsubscribe()
                .getChannel_subscribebutton();

        logger.debug("Начало проверок");
        assertEquals("null", subscribebutton.getAttribute("subscribed"));

        driver.close();
    }
}
