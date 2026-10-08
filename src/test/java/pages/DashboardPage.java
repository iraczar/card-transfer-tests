package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$;

/**
 * Главная страница со списком карт пользователя.
 */
public class DashboardPage {

    private static final Pattern BALANCE_PATTERN = Pattern.compile("(?i)баланс:?\\s*(\\d+)");

    private final ElementsCollection cardRows = $(".CardList_cardBlock__gEjoa").$$("div[data-test-id]");

    private SelenideElement findCardRow(DataHelper.CardInfo cardInfo) {
        return cardRows.findBy(Condition.text(cardInfo.getLastFourDigits()));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String text = findCardRow(cardInfo).text();
        Matcher matcher = BALANCE_PATTERN.matcher(text);
        if (!matcher.find()) {
            throw new IllegalStateException("Не удалось прочитать баланс из текста строки: '" + text + "'");
        }
        return Integer.parseInt(matcher.group(1));
    }

    public TransferPage selectCardToTopUp(DataHelper.CardInfo cardInfo) {
        findCardRow(cardInfo).$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }
}