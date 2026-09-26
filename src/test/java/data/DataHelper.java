package data;

/**
 * Класс с тестовыми данными и вспомогательными методами их генерации.
 * Хардкод в этом ДЗ разрешён и намеренно используется по условию задания:
 * логин, пароль, код верификации и номера карт заданы преподавателем.
 */
public class DataHelper {

    private DataHelper() {
        // утилитный класс, экземпляры не нужны
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002");
    }

    /**
     * Класс с данными для авторизации.
     */
    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    /**
     * Класс с кодом подтверждения (верификации).
     */
    public static class VerificationCode {
        private final String code;

        public VerificationCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    /**
     * Класс с данными карты. Хранит только номер карты —
     * актуальный баланс всегда считывается с реальной страницы,
     * а не хранится в тестовых данных, чтобы тесты не зависели
     * от текущего состояния SUT (баланс меняется от теста к тесту).
     */
    public static class CardInfo {
        private final String cardNumber;

        public CardInfo(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        /**
         * Последние 4 цифры номера карты — обычно именно так карта
         * промаскирована на странице списка карт (например "0001").
         */
        public String getLastFourDigits() {
            return cardNumber.substring(cardNumber.length() - 4);
        }
    }
}
