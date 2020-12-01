package hw6;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class SeleniumTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void test_navigation_trendingmusic() {

        WebElement result = new YTPage(driver)
                .menu_clicktrending()
                .trending_clickmusic()
                .trending_getsectionname();

        assertEquals("style-scope ytd-section-list-renderer", result.getAttribute("class"));
        assertEquals("text-header-renderer-style-bold", result.getAttribute("header-style"));

        driver.close();
    }

    @Test
    public void test_search_searchbytitle() {
        WebElement result = new YTPage(driver)
                .searchvideo("1943 British tank development in a nutshell")
                .getVideo_title();

        assertEquals("style-scope ytd-video-primary-info-renderer", result.getAttribute("class"));
        assertEquals("1943 British tank development in a nutshell", result.getText());

        driver.close();
    }

    @Test
    public void test_likes_likevideo() {
        WebElement likebutton = new YTPage(driver)
                .searchvideo("This Video Has 26,124,676 Views")
                .video_like()
                .getVideo_likebutton();

        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", likebutton.getAttribute("class"));

        driver.close();
    }

    @Test
    public void test_likes_dislikevideo() {
        WebElement dislikebutton = new YTPage(driver)
                .searchvideo("cat")
                .video_dislike()
                .getVideo_likebutton();

        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", dislikebutton.getAttribute("class"));

        driver.close();
    }

    @Test
    public void test_subscription_subscribe() {
        WebElement subscribebutton = new YTPage(driver)
                .searchchannel("Sabaton")
                .channel_subscribe()
                .getChannel_subscribebutton();

        assertEquals("true", subscribebutton.getAttribute("subscribed"));

        driver.close();
    }

    @Test
    public void test_subscription_unsubscribe() {
        WebElement subscribebutton = new YTPage(driver)
                .searchchannel("Sabaton")
                .channel_unsubscribe()
                .getChannel_subscribebutton();

        assertEquals("null", subscribebutton.getAttribute("subscribed"));

        driver.close();
    }
}
