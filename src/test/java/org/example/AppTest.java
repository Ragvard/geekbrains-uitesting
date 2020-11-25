package org.example;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class AppTest 
{
    private WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setupTest() {
        driver = new ChromeDriver();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void test_navigation_trendingmusic() {
        // Заходим на сайт
        driver.get("https://www.youtube.com");

        // Нажимаем на кнопку "В тренде", она может быть полноразмерной или минимизированной, но href всегда одинаковый
        driver.findElement(By.xpath("//a[@href=\"/feed/trending\"]")).click();

        // Ждем загрузки страницы "В тренде"
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
               By.xpath("//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[1]"))
        );

        // Заходим в раздел музыки
        driver.findElement(By.xpath("//*[@id=\"contents\"]/ytd-channel-list-sub-menu-avatar-renderer[1]")).click();

        // Ждем загрузки раздела и проверяем, что это именно раздел музыки
        new WebDriverWait(driver, 5).until(ExpectedConditions.textToBe(
                By.xpath("//*[@id=\"header\"]/ytd-text-header-renderer"), "Музыка")
        );

        driver.close();
    }

    @Test
    public void test_search_searchbytitle() {
        // Заходим на сайт
        driver.get("https://www.youtube.com");

        // Вводим запрос в поисковую строку
        driver.findElement(By.name("search_query")).sendKeys("Gangsta's Paradise but only the choir bit for 10 minutes");

        // Нажимаем на кнопку поиска
        driver.findElement(By.id("search-icon-legacy")).click();

        // Ждем появления списка результатов
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ytd-search-sub-menu-renderer"))
        );

        // Получаем список результатов
        List<WebElement> webElements = driver.findElements(By.id("video-title"));

        // Проходим по всем видео на странице, на случай если нужное видео не первое
        for (WebElement element : webElements) {
            // При нахождении нужного видео заходим в него
            if (element.getText().equals("Gangsta's Paradise but only the choir bit for 10 minutes")) {

                element.click();

                // Убеждаемся, что нужное видео открыто
                new WebDriverWait(driver, 5).until(ExpectedConditions.textToBe(
                        By.xpath("//*[@id=\"container\"]/h1/yt-formatted-string"), "Gangsta's Paradise but only the choir bit for 10 minutes")
                );

                driver.close();

                return;
            }
        }

        driver.close();
        // Не нашли нужного видео
        fail();
    }
}
