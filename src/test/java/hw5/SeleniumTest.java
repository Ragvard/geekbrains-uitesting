package hw5;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
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


public class SeleniumTest
{
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
        // Заходим на сайт
        driver.get("https://www.youtube.com");

        Actions builder = new Actions(driver);

        // Нажимаем на кнопку "В тренде", она может быть полноразмерной или минимизированной, но href всегда одинаковый
        WebElement element = driver.findElement(By.xpath("//a[@href=\"/feed/trending\"]"));

        // Страница откроется в новой вкладке
        builder.keyDown(Keys.CONTROL)
                .click(element)
                .keyUp(Keys.CONTROL)
                .build()
                .perform();

        // Переключаемся на новую вкладку
        List<String> windowHandles = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(1));

        // Ждем загрузки страницы "В тренде"
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
               By.xpath("//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[1]"))
        );

        // Заходим в раздел музыки
        element = driver.findElement(By.xpath("//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[1]"));

        // Страница откроется в новой вкладке
        builder.keyDown(Keys.CONTROL)
                .click(element)
                .keyUp(Keys.CONTROL)
                .build()
                .perform();

        // Переключаемся на новую вкладку
        windowHandles = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window(windowHandles.get(2));

        // Ждем загрузки раздела и проверяем, что это именно раздел музыки
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id=\"header\"]/ytd-text-header-renderer"))
        );

        element = driver.findElement(By.xpath("//*[@id=\"header\"]/ytd-text-header-renderer"));
        assertEquals("style-scope ytd-section-list-renderer", element.getAttribute("class"));
        assertEquals("text-header-renderer-style-bold", element.getAttribute("header-style"));

        driver.close();
    }

    @Test
    public void test_search_searchbytitle() {
        // Заходим на сайт
        driver.get("https://www.youtube.com");

        Actions builder = new Actions(driver);

        // Вводим запрос в поисковую строку
        WebElement search_query = driver.findElement(By.name("search_query"));

        builder.click(search_query)
                .sendKeys("House of a rising sun but it's literally (the) animals")
                .sendKeys(Keys.ENTER)
                .build()
                .perform();

        // Ждем появления списка результатов
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );

        // Получаем список результатов
        List<WebElement> webElements = driver.findElements(By.id("video-title"));

        // Проходим по всем видео на странице, на случай если нужное видео не первое
        for (WebElement element : webElements) {
            // При нахождении нужного видео заходим в него
            if (element.getText().equals("House of a rising sun but it's literally (the) animals")) {

                element.click();

                // Убеждаемся, что нужное видео открыто
                new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"container\"]/h1/yt-formatted-string"))
                );

                element = driver.findElement(By.xpath("//*[@id=\"container\"]/h1/yt-formatted-string"));
                assertEquals("style-scope ytd-video-primary-info-renderer", element.getAttribute("class"));
                assertEquals("House of a rising sun but it's literally (the) animals", element.getText());

                driver.close();

                return;
            }
        }

        driver.close();
        // Не нашли нужного видео
        fail();
    }

    @Test
    public void test_likes_likevideo() {
        // Заходим на сайт
        driver.get("https://www.youtube.com");

        Actions builder = new Actions(driver);

        // Вводим запрос в поисковую строку
        WebElement search_query = driver.findElement(By.name("search_query"));

        builder.click(search_query)
                .sendKeys("Doom Eternal OST - The Only Thing they Fear is You (Mick Gordon)")
                .sendKeys(Keys.ENTER)
                .build()
                .perform();

        // Ждем появления списка результатов
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );

        // Получаем список результатов
        List<WebElement> webElements = driver.findElements(By.id("video-title"));

        // Проходим по всем видео на странице, на случай если нужное видео не первое
        for (WebElement element : webElements) {
            // При нахождении нужного видео заходим в него
            if (element.getText().equals("Doom Eternal OST - The Only Thing they Fear is You (Mick Gordon)")) {

                element.click();

                // Убеждаемся, что нужное видео открыто
                new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"container\"]/h1/yt-formatted-string"))
                );

                element = driver.findElement(By.xpath("//*[@id=\"container\"]/h1/yt-formatted-string"));
                assertEquals("style-scope ytd-video-primary-info-renderer", element.getAttribute("class"));
                assertEquals("Doom Eternal OST - The Only Thing they Fear is You (Mick Gordon)", element.getText());

                element = driver.findElement(By.xpath("//*[@id=\"top-level-buttons\"]/ytd-toggle-button-renderer[1]"));

                if (element.getAttribute("class").equals("style-scope ytd-menu-renderer force-icon-button style-text")) {
                    // Убрать лайк
                    element.click();
                }

                // Ассерт не проходит, так как не вошел в аккаунт и вместо этого вылезает уведомление
                element.click();
                assertEquals("style-scope ytd-menu-renderer force-icon-button style-default-active", element.getAttribute("class"));

                driver.close();

                return;
            }
        }

        driver.close();
        // Не нашли нужного видео
        fail();
    }

    @Test
    public void test_subscription_subscribe() {
        // Заходим на сайт
        driver.get("https://www.youtube.com");

        Actions builder = new Actions(driver);

        // Вводим название канала в поиск
        WebElement search_query = driver.findElement(By.name("search_query"));

        builder.click(search_query)
                .sendKeys("Kurzgesagt")
                .sendKeys(Keys.ENTER)
                .build()
                .perform();

        // Ждем появления списка результатов
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.id("content-section"))
        );

        // Получаем список результатов
        List<WebElement> webElements = driver.findElements(By.id("channel-title"));

        // Проходим по всем каналам на странице, хотя я не уверен, что ютуб выводит больше одного
        for (WebElement element : webElements) {
            // При нахождении нужного канала заходим в него
            if (element.getText().equals("Kurzgesagt – In a Nutshell")) {

                element.click();

                // Убеждаемся, что зашли на нужный канал
                new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[@id=\"channel-header-container\"]/div/div[1]/ytd-channel-name/div/div/yt-formatted-string"))
                );

                element = driver.findElement(By.xpath("//*[@id=\"channel-header-container\"]/div/div[1]/ytd-channel-name/div/div/yt-formatted-string"));
                assertEquals("style-scope ytd-channel-name", element.getAttribute("class"));
                assertEquals("Kurzgesagt – In a Nutshell", element.getText());

                // В зависимости от того, зашел пользователь в аккаунт или нет, путь к кнопке будет разный. Указан путь для залогиненного пользователя
                element = driver.findElement(By.xpath("//*[@id=\"subscribe-button\"]/ytd-subscribe-button-renderer/paper-button"));
                // Путь без аккаунта - //*[@id="subscribe-button"]/ytd-button-renderer/a/paper-button, с ним на следующей строке почему-то падает с nullpointer

                // Падает, так как не вошел в аккаунт и путь к кнопке другой
                // Проверить, что еще не подписаны
                if (element.getAttribute("subscribed").equals("true")) {
                    // Отписаться
                    element.click();
                    driver.findElement(By.xpath("//*[@id=\"confirm-button\"]")).click();
                }

                // Подписаться
                element.click();
                assertEquals("true", element.getAttribute("subscribed"));

                driver.close();

                return;
            }
        }

        driver.close();

        // Не нашли нужного канала
        fail();
    }
}
