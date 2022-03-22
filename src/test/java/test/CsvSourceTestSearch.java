package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CsvSourceTestSearch {

    @BeforeAll
    static void preconditionAll() {
        Configuration.browserSize = "1920x1280";
    }

    @BeforeEach
    void precondition() {
        Selenide.open("https://www.dns-shop.ru/");
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }

    @CsvSource(value = {
            "Samsung | Смартфоны",
            "Apple | Моноблоки",
            "Honor | Весы"
    },
            delimiter = '|'
    )
    @ParameterizedTest(name = "Ищем {1} от фирмы {0} на сайте DNS")
    void test(String brand, String technic) {
        //Ждем, что страница загрузилась
        Selenide.$("#header-search [name='q']")
                .shouldHave();
        //Вводим значение в поиcк и подтверждаем ввод
        $("#header-search [name='q']")
                .setValue(brand)
                .pressEnter();
        //Проверяем, то на странице найден указанный бренд
        $$("span[data-role='title-text']")
                .shouldHave()
                .findBy(Condition.text(technic).because("Не найдена категория"))
                .click();
        //Сравниваем наименование категории с заданной
        Assert.assertEquals($("h1.title").getText(), technic);
    }
}
