package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$x;

/**
 * Главная страница со списком карт пользователя.
 */
public class DashboardPage {

    // (?i) - регистр не важен; ищем "баланс" (или "Баланс"), затем число.
    private static final Pattern BALANCE_PATTERN = Pattern.compile("(?i)баланс:?\\s*(\\d+)");

    /**
     * Находит div строки конкретной карты по последним 4 цифрам номера —
     * этого достаточно, чтобы однозначно отличить одну карту от другой,
     * и не зависит от того, есть пробелы в номере на странице или нет.
     */
    private SelenideElement findCardRow(String cardNumber) {
        String lastFourDigits = cardNumber.replaceAll("[^0-9]", "")
                .substring(cardNumber.replaceAll("[^0-9]", "").length() - 4);
        return $x("//div[contains(text(),'" + lastFourDigits + "')]");
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String text = findCardRow(cardInfo.getCardNumber()).text();
        Matcher matcher = BALANCE_PATTERN.matcher(text);
        if (!matcher.find()) {
            throw new IllegalStateException("Не удалось прочитать баланс из текста строки: '" + text + "'");
        }
        return Integer.parseInt(matcher.group(1));
    }

    public TransferPage selectCardToTopUp(DataHelper.CardInfo cardInfo) {
        SelenideElement row = findCardRow(cardInfo.getCardNumber());
        row.$("[data-test-id='action-deposit']").click();
        return new TransferPage();
    }
}