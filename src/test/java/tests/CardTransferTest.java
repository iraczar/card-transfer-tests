package tests;

import data.DataHelper;
import org.junit.jupiter.api.Test;
import pages.TransferPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /**
     * Баг: см. Issue #1 в репозитории — приложение позволяет перевести
     * сумму больше остатка на карте-источнике, баланс уходит в минус.
     * Тест намеренно оставлен "красным", так как воспроизводит реальный дефект.
     */
    @Test
    void shouldNotTransferMoreThanAvailableBalance() {
        DataHelper.CardInfo firstCard = DataHelper.getFirstCardInfo();
        DataHelper.CardInfo secondCard = DataHelper.getSecondCardInfo();

        int secondBalanceBefore = dashboardPage.getCardBalance(secondCard);
        int excessiveAmount = secondBalanceBefore + 5000;

        TransferPage transferPage = dashboardPage.selectCardToTopUp(firstCard);
        transferPage.makeInvalidTransfer(String.valueOf(excessiveAmount), secondCard);

        transferPage.shouldShowError("Ошибка");
    }
}
