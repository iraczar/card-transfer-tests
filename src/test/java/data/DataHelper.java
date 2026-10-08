package data;

import lombok.Value;

/**
 * Класс с тестовыми данными и вспомогательными методами их генерации.
 * Хардкод в этом ДЗ разрешён и намеренно используется по условию задания:
 * логин, пароль, код верификации и номера карт заданы преподавателем.
 */
public class DataHelper {

    private DataHelper() {
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

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    @Value
    public static class CardInfo {
        String cardNumber;

        /**
         * Последние 4 цифры номера карты — используются, чтобы однозначно
         * найти нужную карту в коллекции карт на странице дашборда.
         */
        public String getLastFourDigits() {
            String digitsOnly = cardNumber.replaceAll("[^0-9]", "");
            return digitsOnly.substring(digitsOnly.length() - 4);
        }
    }
}