package hw6;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import org.slf4j.Logger;

public class YouTubePage {

    private WebDriver driver;

    private Logger logger;

    private String site = "https://www.youtube.com";

    @FindBy(xpath = "//a[@href=\"/feed/trending\"]")
    private WebElement menuTrending;

    @FindBy(xpath = "//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[1]")
    private WebElement trendingMusicbutton;

    @FindBy(xpath = "//*[@id=\"header\"]/ytd-text-header-renderer")
    private WebElement trendingSectionname;


    @FindBy(name = "search_query")
    private WebElement searchbar;


    @FindBy(xpath = "//*[@id=\"container\"]/h1/yt-formatted-string")
    private WebElement videoTitle;

    @FindBy(xpath = "//*[@id=\"top-level-buttons\"]/ytd-toggle-button-renderer[1]")
    private WebElement videoLikebutton;

    @FindBy(xpath = "//*[@id=\"top-level-buttons\"]/ytd-toggle-button-renderer[2]")
    private WebElement videoDislikebutton;


    @FindBy(xpath = "//*[@id=\"channel-header-container\"]/div/div[1]/ytd-channel-name/div/div/yt-formatted-string")
    private WebElement channelTitle;

    @FindBy(xpath = "//*[@id=\"subscribe-button\"]/ytd-subscribe-button-renderer/paper-button")
    private WebElement channelSubscribebutton;

    public YouTubePage(WebDriver driver, Logger logger) {
        this.driver = driver;
        this.logger = logger;
        driver.get(site);
        logger.debug("Открыта страница " + site);
        PageFactory.initElements(driver, this);
    }

    // Left menu

    public YouTubePage menuClickTrending() {
        menuTrending.click();
        logger.debug("Нажата кнопка меню-в тренде");
        new WebDriverWait(driver, 5).until(
                ExpectedConditions.visibilityOfAllElements(
                        trendingMusicbutton
                ));
        return this;
    }

    // Trending

    public YouTubePage trendingClickMusic() {
        trendingMusicbutton.click();
        logger.debug("Нажата кнопка в тренде-музыка");
        new WebDriverWait(driver, 5).until(
                ExpectedConditions.visibilityOf(trendingSectionname));
        return this;
    }

    public WebElement trendingGetSectionname() {
        return trendingSectionname;
    }

    // Search

    public YouTubePage searchVideo(String query) {
        new Actions(driver)
                .click(searchbar)
                .sendKeys(query)
                .sendKeys(Keys.ENTER)
                .build()
                .perform();

        logger.debug("Выполнен поиск видео по запросу: " + query);

        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );

        List<WebElement> webElements = driver.findElements(By.id("video-title"));
        logger.debug("Перебор видео на странице...");
        for (WebElement element : webElements) {
            logger.debug("Найдено видео " + element.getText());
            if (element.getText().equals(query)) {
                element.click();
                new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(
                        videoTitle));
                return this;
            }
        }

        logger.debug("Видео по запросу не найдно");

        throw new IllegalArgumentException("No video by query " + query);
    }

    public YouTubePage searchChannel(String query) {
        new Actions(driver)
                .click(searchbar)
                .sendKeys(query)
                .sendKeys(Keys.ENTER)
                .build()
                .perform();

        logger.debug("Выполнен поиск канала по запросу: " + query);

        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );

        List<WebElement> webElements = driver.findElements(By.id("channel-title"));
        logger.debug("Перебор каналов на странице...");
        for (WebElement element : webElements) {
            logger.debug("Найден канал " + element.getText());
            if (element.getText().equals(query)) {
                element.click();
                new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(
                        channelTitle));
                return this;
            }
        }

        logger.debug("Канал по запросу не найдн");

        throw new IllegalArgumentException("No channel by query " + query);
    }

    // Video


    public YouTubePage videoLike() {
        logger.debug("Проверка на наличие лайка");
        if (!videoLikebutton.getAttribute("class").equals("style-scope ytd-menu-renderer force-icon-button style-text")) {
            logger.debug("Видео уже лайкнуто");
            videoLikebutton.click();
            logger.debug("Лайк убран");
        }
        videoLikebutton.click();
        logger.debug("Видео лайкнуто");
        return this;
    }

    public YouTubePage videoDislike() {
        logger.debug("Проверка на наличие лайка");
        if (!videoDislikebutton.getAttribute("class").equals("style-scope ytd-menu-renderer force-icon-button style-text")) {
            logger.debug("Видео уже дизлайкнуто");
            videoDislikebutton.click();
            logger.debug("Дизайк убран");
        }
        videoDislikebutton.click();
        logger.debug("Видео дизлайкнуто");
        return this;
    }

    // Channel

    public YouTubePage channelSubscribe() {
        logger.debug("Проверка на наличие подписки");
        if (channelSubscribebutton.getAttribute("subscribed").equals("true")) {
            logger.debug("Подписка уже оформлена");
            channelSubscribebutton.click();
            driver.findElement(By.xpath("//*[@id=\"confirm-button\"]")).click();
            logger.debug("Подписка отменена");
        }

        channelSubscribebutton.click();
        logger.debug("Подписка оформлена");

        return this;
    }

    public YouTubePage channelUnsubscribe() {
        logger.debug("Проверка на наличие подписки");
        if (channelSubscribebutton.getAttribute("subscribed").equals("null")) {
            logger.debug("Подписка не оформлена");
            channelSubscribebutton.click();
            logger.debug("Подписка оформлена");

        }

        channelSubscribebutton.click();
        driver.findElement(By.xpath("//*[@id=\"confirm-button\"]")).click();
        logger.debug("Подписка отменена");

        return this;
    }

    public WebElement getVideoTitle() {
        return videoTitle;
    }

    public WebElement getVideoLikebutton() {
        return videoLikebutton;
    }

    public WebElement getChannelSubscribebutton() {
        return channelSubscribebutton;
    }
}
