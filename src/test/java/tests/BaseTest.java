package tests;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.DashboardPage;
import pages.LoginPage;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class BaseTest {

    protected DashboardPage dashboardPage;

    @BeforeAll
    static void setUpAll() {
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;

        // В GitHub Actions переменная окружения CI выставлена автоматически —
        // там нет монитора, поэтому включаем headless. Локально у тебя
        // останется обычный видимый браузер.
        boolean runningInCi = System.getenv("CI") != null;
        Configuration.headless = runningInCi;

        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-features=PasswordLeakDetection,PasswordCheck");
        if (runningInCi) {
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        }
        Configuration.browserCapabilities = options;
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