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
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class YTPage {

    private WebDriver driver;

    private String site = "https://www.youtube.com";

    @FindBy(xpath = "//a[@href=\"/feed/trending\"]")
    private WebElement menu_trending;

    @FindBy(xpath = "//a[@href=\"")
    private WebElement menu_title;

    @FindBy(xpath = "//a[@href=\"/feed/subscriptions\"]")
    private WebElement menu_subscriptions;

    @FindBy(xpath = "//a[@href=\"/feed/library\"]")
    private WebElement menu_library;

    @FindBy(xpath = "//a[@href=\"/feed/history\"]")
    private WebElement menu_history;

    @FindBy(xpath = "//a[@href=\"/playlist?list=WL\"]")
    private WebElement menu_watchlater;

    @FindBy(xpath = "//a[@href=\"/playlist?list=LL\"]")
    private WebElement menu_likedvideos;


    @FindBy(xpath = "//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[1]")
    private WebElement trending_musicbutton;

    @FindBy(xpath = "//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[2]")
    private WebElement trending_gamesbutton;

    @FindBy(xpath = "//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[3]")
    private WebElement trending_newsbutton;

    @FindBy(xpath = "//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[4]")
    private WebElement trending_filmsbutton;

    @FindBy(xpath = "//*[@id=\"header\"]/ytd-text-header-renderer")
    private WebElement trending_sectionname;


    @FindBy(name = "search_query")
    private WebElement searchbar;


    @FindBy(xpath = "//*[@id=\"container\"]/h1/yt-formatted-string")
    private WebElement video_title;

    @FindBy(xpath = "//*[@id=\"top-level-buttons\"]/ytd-toggle-button-renderer[1]")
    private WebElement video_likebutton;

    @FindBy(xpath = "//*[@id=\"top-level-buttons\"]/ytd-toggle-button-renderer[2]")
    private WebElement video_dislikebutton;


    @FindBy(xpath = "//*[@id=\"channel-header-container\"]/div/div[1]/ytd-channel-name/div/div/yt-formatted-string")
    private WebElement channel_title;

    @FindBy(xpath = "//*[@id=\"subscribe-button\"]/ytd-subscribe-button-renderer/paper-button")
    private WebElement channel_subscribebutton;

    public YTPage(WebDriver driver) {
        this.driver = driver;
        driver.get(site);
        PageFactory.initElements(driver, this);
    }

    // Left menu

    public YTPage menu_clicktrending() {
        menu_trending.click();
        new WebDriverWait(driver, 5).until(
                ExpectedConditions.visibilityOfAllElements(
                        trending_filmsbutton,
                        trending_gamesbutton,
                        trending_musicbutton,
                        trending_newsbutton
                ));
        return this;
    }

    public YTPage menu_clicktitle() {
        menu_title.click();
        // TODO Explicit wait
        return this;
    }

    public YTPage menu_clicksubscriptions() {
        menu_subscriptions.click();
        // TODO Explicit wait
        return this;
    }

    public YTPage menu_clicklibrary() {
        menu_library.click();
        // TODO Explicit wait
        return this;
    }

    public YTPage menu_clickhistory() {
        menu_history.click();
        // TODO Explicit wait
        return this;
    }

    public YTPage menu_watchlater() {
        menu_watchlater.click();
        // TODO Explicit wait
        return this;
    }

    public YTPage menu_likedvideos() {
        menu_likedvideos.click();
        // TODO Explicit wait
        return this;
    }

    // Trending

    public YTPage trending_clickmusic() {
        trending_musicbutton.click();
        new WebDriverWait(driver, 5).until(
                ExpectedConditions.visibilityOf(trending_sectionname));
        return this;
    }

    public YTPage trending_clickgames() {
        trending_gamesbutton.click();
        new WebDriverWait(driver, 5).until(
                ExpectedConditions.visibilityOf(trending_sectionname));
        return this;
    }

    public YTPage trending_clicknews() {
        trending_newsbutton.click();
        new WebDriverWait(driver, 5).until(
                ExpectedConditions.visibilityOf(trending_sectionname));
        return this;
    }

    public YTPage trending_clickfilms() {
        trending_filmsbutton.click();
        new WebDriverWait(driver, 5).until(
                ExpectedConditions.visibilityOf(trending_sectionname));
        return this;
    }

    public WebElement trending_getsectionname() {
        return trending_sectionname;
    }

    // Search

    public YTPage search(String query) {
        new Actions(driver)
                .click(searchbar)
                .sendKeys(query)
                .sendKeys(Keys.ENTER)
                .build()
                .perform();
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );
        return this;
    }

    public YTPage searchvideo(String query) {
        new Actions(driver)
                .click(searchbar)
                .sendKeys(query)
                .sendKeys(Keys.ENTER)
                .build()
                .perform();

        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );

        List<WebElement> webElements = driver.findElements(By.id("video-title"));
        for (WebElement element : webElements) {
            if (element.getText().equals(query)) {
                element.click();
                new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(
                        video_title));
                return this;
            }
        }

        throw new IllegalArgumentException("No video by query " + query);
    }

    public YTPage searchchannel(String query) {
        new Actions(driver)
                .click(searchbar)
                .sendKeys(query)
                .sendKeys(Keys.ENTER)
                .build()
                .perform();

        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );

        List<WebElement> webElements = driver.findElements(By.id("channel-title"));
        for (WebElement element : webElements) {
            if (element.getText().equals(query)) {
                element.click();
                new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(
                        channel_title));
                return this;
            }
        }

        throw new IllegalArgumentException("No channel by query " + query);
    }

    // Video

    public String video_gettitle() {
        return video_title.getText();
    }

    public YTPage video_like() {
        if (video_likebutton.getAttribute("class").equals("style-scope ytd-menu-renderer force-icon-button style-text")) {
            video_likebutton.click();
        }
        video_likebutton.click();
        return this;
    }

    public YTPage video_dislike() {
        if (video_dislikebutton.getAttribute("class").equals("style-scope ytd-menu-renderer force-icon-button style-text")) {
            video_dislikebutton.click();
        }
        video_dislikebutton.click();
        return this;
    }

    // Channel

    public YTPage channel_subscribe() {
        if (channel_subscribebutton.getAttribute("subscribed").equals("true")) {
            channel_subscribebutton.click();
            driver.findElement(By.xpath("//*[@id=\"confirm-button\"]")).click();
        }

        channel_subscribebutton.click();

        return this;
    }

    public YTPage channel_unsubscribe() {
        if (channel_subscribebutton.getAttribute("subscribed").equals("null")) {
            channel_subscribebutton.click();
        }

        channel_subscribebutton.click();
        driver.findElement(By.xpath("//*[@id=\"confirm-button\"]")).click();

        return this;
    }


    // Автоматически сгенерированные геттеры

    public WebDriver getDriver() {
        return driver;
    }

    public String getSite() {
        return site;
    }

    public WebElement getMenu_trending() {
        return menu_trending;
    }

    public WebElement getMenu_title() {
        return menu_title;
    }

    public WebElement getMenu_subscriptions() {
        return menu_subscriptions;
    }

    public WebElement getMenu_library() {
        return menu_library;
    }

    public WebElement getMenu_history() {
        return menu_history;
    }

    public WebElement getMenu_watchlater() {
        return menu_watchlater;
    }

    public WebElement getMenu_likedvideos() {
        return menu_likedvideos;
    }

    public WebElement getTrending_musicbutton() {
        return trending_musicbutton;
    }

    public WebElement getTrending_gamesbutton() {
        return trending_gamesbutton;
    }

    public WebElement getTrending_newsbutton() {
        return trending_newsbutton;
    }

    public WebElement getTrending_filmsbutton() {
        return trending_filmsbutton;
    }

    public WebElement getTrending_sectionname() {
        return trending_sectionname;
    }

    public WebElement getSearchbar() {
        return searchbar;
    }

    public WebElement getVideo_title() {
        return video_title;
    }

    public WebElement getVideo_likebutton() {
        return video_likebutton;
    }

    public WebElement getVideo_dislikebutton() {
        return video_dislikebutton;
    }

    public WebElement getChannel_title() {
        return channel_title;
    }

    public WebElement getChannel_subscribebutton() {
        return channel_subscribebutton;
    }
}
