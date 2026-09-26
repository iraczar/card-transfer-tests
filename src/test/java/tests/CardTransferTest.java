package tests;

import data.DataHelper;
import org.junit.jupiter.api.Test;
import pages.TransferPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты перевода средств с карты на карту.
 *
 * Важно: баланс карт НЕ хардкодится в тестах (например "10000"), так как
 * SUT не перезапускается между тестами и баланс от прошлых прогонов
 * переносится дальше. Вместо этого баланс всегда считывается со страницы
 * непосредственно перед действием, а ожидаемый результат считается
 * относительно этого прочитанного значения.
 */
class CardTransferTest extends BaseTest {

    @Test
    void shouldIncreaseBalanceOnFirstCardAfterTransferFromSecond() {
        DataHelper.CardInfo firstCard = DataHelper.getFirstCardInfo();
        DataHelper.CardInfo secondCard = DataHelper.getSecondCardInfo();
        int amount = 1000;

        int firstBalanceBefore = dashboardPage.getCardBalance(firstCard);
        int secondBalanceBefore = dashboardPage.getCardBalance(secondCard);

        TransferPage transferPage = dashboardPage.selectCardToTopUp(firstCard);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), secondCard);

        int firstBalanceAfter = dashboardPage.getCardBalance(firstCard);
        int secondBalanceAfter = dashboardPage.getCardBalance(secondCard);

        assertEquals(firstBalanceBefore + amount, firstBalanceAfter,
                "Баланс карты-получателя должен увеличиться на сумму перевода");
        assertEquals(secondBalanceBefore - amount, secondBalanceAfter,
                "Баланс карты-источника должен уменьшиться на сумму перевода");
    }

    @Test
    void shouldNotTransferMoreThanAvailableBalance() {
        DataHelper.CardInfo firstCard = DataHelper.getFirstCardInfo();
        DataHelper.CardInfo secondCard = DataHelper.getSecondCardInfo();

        int secondBalanceBefore = dashboardPage.getCardBalance(secondCard);
        int excessiveAmount = secondBalanceBefore + 5000;

        TransferPage transferPage = dashboardPage.selectCardToTopUp(firstCard);
        transferPage.makeInvalidTransfer(String.valueOf(excessiveAmount), secondCard);

        // Ожидание по здравому смыслу: при превышении остатка приложение должно
        // показать ошибку и НЕ выполнять перевод. Если по факту перевод всё же
        // проходит (баг!) — тест упадёт здесь, и это повод завести issue в GitHub,
        // а не подгонять тест под баг.
        assertTrue(transferPage.isErrorShown(),
                "При переводе суммы больше остатка ожидается сообщение об ошибке");
    }
}
