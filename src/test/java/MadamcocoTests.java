import com.codeborne.selenide.CollectionCondition;
import domain.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.executeJavaScript;

@DisplayName("Web-tests JUnit")
public class MadamcocoTests {

    @BeforeEach
    void setUp() {
        open("https://www.madamecoco.com/");
        executeJavaScript("$('.wis-txt-content').remove()");
        executeJavaScript("$('.wis-appindir').remove()");
    }

    static Stream<Arguments> madamcocoLocaleTestDataProvider() {
        return Stream.of(
                Arguments.of(
                        Locale.EN, List.of("AFAD", "Order Tracking", "Stores", "Contact Us")
                ),
                Arguments.of(
                        Locale.TR, List.of("Sipariş Takibi", "Mağazalar", "İletişim")
                )
        );
    }

    @MethodSource("madamcocoLocaleTestDataProvider")
    @ParameterizedTest(name = " For language {0} should be visible buttons {1}")
    @Tags({
            @Tag("BLOCKER"),
            @Tag("WEB")
    })
    void selenideLocaleTest(Locale siteLocale, List<String> expectedButtons) {
        $$("button.action-menu__button").find(text(siteLocale.name())).click();

        $$(".band-menu a").filter(visible)
                .shouldHave(CollectionCondition.texts(expectedButtons));
    }

    @CsvSource(value = {
            "EN, Rug, Rug",
            "TR, Kilim, Kilim"
    })
    @ParameterizedTest(name = " For language: {0} with search request: {1} should have in the result: {2}")
    @Tags({
            @Tag("BLOCKER"),
            @Tag("WEB")
    })
    void successfulSearchTest(String language, String searchQuery, String expectedResult) {
        $$("button.action-menu__button").find(text(language)).click();
        $("#pz-form-input-AutocompleteInput").setValue(searchQuery).pressEnter();
        $$(".product-item__info h3").first().shouldHave(text(expectedResult));
    }

    @ValueSource(strings = {
            "Cookware",
            "Tableware",
            "Mugs&Cups"
    })
    @ParameterizedTest(name = "In Section 'Gifts for Mother's Day' click item {0}")
    @Tags({
            @Tag("BLOCKER"),
            @Tag("WEB")
    })
    void clickableItemsTest(String itemName) {
        $$("button.action-menu__button").find(text("EN")).click();
        $$(".triple-banner__list-item").find(text(itemName)).click();
    }

}
