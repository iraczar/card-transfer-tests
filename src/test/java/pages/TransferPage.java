package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

/**
 * Страница перевода средств (открывается после клика на «Пополнить»
 * на карте-получателе). Здесь вводится карта-источник и сумма перевода.
 *
 * Реальное поведение приложения (проверено по коду):
 *  - при УСПЕШНОМ переводе приложение возвращает на список карт;
 *  - при ОШИБКЕ (например, "недостаточно средств") приложение остаётся
 *    на этой же странице и показывает блок [data-test-id='error-notification'],
 *    навигации на дашборд НЕ происходит.
 * Поэтому здесь два разных метода отправки формы — под каждый случай.
 */
public class TransferPage {

    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromCardField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    private void fillForm(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromCardField.setValue(fromCard.getCardNumber());
    }

    /**
     * Использовать, когда по условию теста перевод должен пройти успешно.
     * Возвращает DashboardPage, так как приложение само переключится
     * обратно на список карт.
     */
    public DashboardPage makeValidTransfer(String amount, DataHelper.CardInfo fromCard) {
        fillForm(amount, fromCard);
        transferButton.click();
        return new DashboardPage();
    }

    /**
     * Использовать, когда по условию теста ожидается ошибка (например,
     * сумма перевода больше остатка на карте). Остаётся на этой же
     * странице, только кликает кнопку — саму ошибку проверяй через
     * getErrorMessage()/isErrorShown().
     */
    public TransferPage makeInvalidTransfer(String amount, DataHelper.CardInfo fromCard) {
        fillForm(amount, fromCard);
        transferButton.click();
        return this;
    }

    public boolean isErrorShown() {
        return errorNotification.is(Condition.visible);
    }

    public String getErrorMessage() {
        return errorNotification.shouldBe(Condition.visible).text();
    }
}
