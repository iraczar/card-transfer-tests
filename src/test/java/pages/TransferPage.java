package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromCardField = $("[data-test-id='from'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");

    private void fillForm(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromCardField.setValue(fromCard.getCardNumber());
    }

    public DashboardPage makeValidTransfer(String amount, DataHelper.CardInfo fromCard) {
        fillForm(amount, fromCard);
        transferButton.click();
        return new DashboardPage();
    }

    public TransferPage makeInvalidTransfer(String amount, DataHelper.CardInfo fromCard) {
        fillForm(amount, fromCard);
        transferButton.click();
        return this;
    }

    public void shouldShowError(String expectedMessageSubstring) {
        errorNotification.shouldBe(Condition.visible).shouldHave(Condition.text(expectedMessageSubstring));
    }
}