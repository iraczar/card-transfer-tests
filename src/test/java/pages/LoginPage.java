package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

/**
 * Страница логина (первый экран приложения).
 * Селекторы проверены по реальному фронтенду приложения
 * (извлечены из main.*.chunk.js внутри app-ibank-build-for-testers.jar).
 */
public class LoginPage {

    private final SelenideElement loginField = $("[data-test-id='login'] input");
    private final SelenideElement passwordField = $("[data-test-id='password'] input");
    private final SelenideElement submitButton = $("[data-test-id='action-login']");

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
        submitButton.click();
        return new VerificationPage();
    }
}
