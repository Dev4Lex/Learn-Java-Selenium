import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static com.codeborne.selenide.Selenide.*;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class SignUpTest {
    private SignUpPage page;

    @BeforeClass
    public static void setUp(){
        System.setProperty("webdriver.chrome.driver", "/Users/alexs/Desktop/git/Learn/SeleniumWebDriver/drivers/chromedriver");
        baseUrl = "https://www.spotify.com/ru-ru/signup";
        browser = "chrome";
    }


    @Test
    public void typeInvalidYear(){
        page = new SignUpPage();
        page.open();
        page.closeBanners();
        page.getScroll();
        page.setMonth("Май")
                .typeDay("10")
                .typeYear("85");
        page.getScroll();
        page.setMarketing();
        page.getError("Укажите действительный год.").shouldBe(Condition.visible);
        //page.getError("Выберите месяц.").shouldBe(Condition.visible);
        //page.getError("Укажите действительный день месяца.").shouldBe(Condition.visible);
    }

    @Test
    public void typeInvalidEmail(){
        page = new SignUpPage();
        page.open();
        page.closeBanners();
        page.typeName("test@mail.test")
                .typeConfirmEmailField("wrong@mail.test")
                .typeName("Testname")
                .clickSignUpButton();
        page.getError("Адреса электронной почты не совпадают.").shouldBe(Condition.visible);
    }

    @Test
    public void signUpWithEmptyPassword(){
        page = new SignUpPage();
        page.open();
        page.closeBanners();
        page.typeEmail("test@mail.test")
                .typeConfirmEmailField("test@mail.test")
                .typeName("Testname")
                .clickSignUpButton();
        page.getError("Введите пароль.").shouldBe(Condition.visible);
    }

    @Test
    public void typeInvalidValues(){
        page = new SignUpPage();
        page.open();
        page.closeBanners();
        page.typeEmail("testmail")
                .typeConfirmEmailField("wrong@test.mail")
                .typePassword("qweqwe123!")
                .typeName("Name")
                .getScroll();
        page.setGender("Мужчина");
        page.getScroll();
        page.setMarketing();
        page.clickSignUpButton();

        page.getErrors().shouldHave(CollectionCondition.size(7));
        page.getErrorByNumber(4).shouldHave(text("Выберите месяц."));
    }

    @After
    public void closeBrowser(){
        Selenide.closeWebDriver();
    }

}
