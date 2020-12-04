package hw7;

import io.qameta.allure.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


public class SeleniumTest{
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
    public void teardown() throws IOException {
        logger.debug("Конец теста");
        screenshot();
        logger.debug("Скриншот добавлен в отчет");
        driver.close();
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    static public void close() {
        logger.debug("Конец тестирования");
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] screenshot() throws IOException {
        String path = "./target/screenshot-" + System.currentTimeMillis() + ".png";
        File temp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destination = new File(path);
        try {
            FileUtils.copyFile(temp, destination);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        logger.debug("Скриншот сохранен в " + path);
        return Files.readAllBytes(Paths.get(path));
    }

    @Test
    @Epic("Навигация по сайту")
    @Feature("Переход в подразделы \"В тренде\"")
    @Story("Переход в подраздел \"Музыка\"")
    @DisplayName("Тест на корректный переход в раздел \"В тренде - Музыка\"")
    @Description("Проверяется возможность перейти в раздел \"В тренде\" через кнопку в боковом меню, " +
            "затем в подраздел \"Музыка\"")
    public void testNavigationTrendingmusic() throws IOException {
        WebElement result = new YouTubePage(driver, logger)
                .menuClickTrending()
                .trendingClickMusic()
                .trendingGetSectionname();
        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-section-list-renderer", result.getAttribute("class"));
        assertEquals("text-header-renderer-style-bold", result.getAttribute("header-style"));
    }

    @Test
    @Epic("Поиск по сайту")
    @Feature("Поиск через поисковую строку")
    @Story("Поиск видео")
    @DisplayName("Тест на корректный поиск видео по его названию")
    @Description("Проверяется корректность поиска видео при ввода его названия в поисковую строку")
    public void testSearchSearchbytitle() {
        WebElement result = new YouTubePage(driver, logger)
                .searchVideo("1943 British tank development in a nutshell")
                .getVideoTitle();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-video-primary-info-renderer", result.getAttribute("class"));
        assertEquals("1943 British tank development in a nutshell", result.getText());
    }

    @Test
    @Epic("Оценка видео")
    @Feature("Лайки и дизлайки")
    @Story("Добавление лайка")
    @DisplayName("Тест на корректное добавление лайка под видео")
    @Description("Проверяется корректность поиска видео при ввода его названия в поисковую строку, " +
            "затем корректность добавления лайка. В случае, если лайк уже поставлен, он убирается перед проверкой")
    public void testLikesLikevideo() {
        WebElement likebutton = new YouTubePage(driver, logger)
                .searchVideo("Cats Sing Believer by Imagine Dragons | Cats Singing Song")
                .videoLike()
                .getVideoLikebutton();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", likebutton.getAttribute("class"));
    }

    @Test
    @Epic("Оценка видео")
    @Feature("Лайки и дизлайки")
    @Story("Добавление дизлайка")
    @DisplayName("Тест на корректное добавление дизлайка под видео")
    @Description("Проверяется корректность поиска видео при ввода его названия в поисковую строку, " +
            "затем корректность добавления дизлайка. В случае, если дизлайк уже поставлен, он убирается перед проверкой")
    public void testLikesDislikevideo() {
        WebElement dislikebutton = new YouTubePage(driver, logger)
                .searchVideo("White Cat Meowing meme (Full video)")
                .videoDislike()
                .getVideoLikebutton();

        logger.debug("Начало проверок");
        assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", dislikebutton.getAttribute("class"));
    }

    @Test
    @Epic("Подписки на канал")
    @Feature("Подписки на канал")
    @Story("Оформление подписки")
    @DisplayName("Тест на корректное оформление подписки на указанный канал")
    @Description("Проверяется корректность поиска канала при ввода его названия в поисковую строку, " +
            "затем корректность оформления подписки на канал. В случае, если подписка уже есть, она отменяется перед проверкой")
    public void testSubscriptionSubscribe() {
        WebElement subscribebutton = new YouTubePage(driver, logger)
                .searchChannel("MU6 - MusiX")
                .channelSubscribe()
                .getChannelSubscribebutton();

        logger.debug("Начало проверок");
        assertEquals("true", subscribebutton.getAttribute("subscribed"));
    }

    @Test
    @Epic("Подписки на канал")
    @Feature("Подписки на канал")
    @Story("Отмена подписки")
    @DisplayName("Тест на корректную отмену подписки на указанный канал")
    @Description("Проверяется корректность поиска канала при ввода его названия в поисковую строку, " +
            "затем корректность отмены подписки на канал. В случае, если подписки нет, она оформляется перед проверкой")
    public void testSubscriptionUnsubscribe() {
        WebElement subscribebutton = new YouTubePage(driver, logger)
                .searchChannel("MU6 - MusiX")
                .channelUnsubscribe()
                .getChannelSubscribebutton();

        logger.debug("Начало проверок");
        assertEquals("null", subscribebutton.getAttribute("subscribed"));

    }
}
