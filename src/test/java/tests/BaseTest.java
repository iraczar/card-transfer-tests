package tests;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pages.DashboardPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class BaseTest {

    protected DashboardPage dashboardPage;

    @BeforeAll
    static void setUpAll() {
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
        // Раскомментируй, если нужно запускать без графического интерфейса (например, в CI):
        // Configuration.headless = true;
    }

    @BeforeEach
    void openAppAndLogin() {
        open("/");
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(DataHelper.getAuthInfo());
        dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCode());
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}
