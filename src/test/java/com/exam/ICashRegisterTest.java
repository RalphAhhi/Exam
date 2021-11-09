package com.exam;

import com.exam.exception.InsufficientFundException;
import com.exam.exception.RollbackException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class ICashRegisterTest {

    ICashRegister cr;
    Wallet wallet;

    @Mock
    ICashHolderPrinter printer;

    @Mock
    IRollbackProcessor rollbackProcessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        wallet = new Wallet();
        cr = new CashRegister(wallet, printer, rollbackProcessor);
    }

    @Test
    public void testShow() {
        cr.show();
        Mockito.verify(printer, Mockito.times(1)).printCashStatus(wallet);
    }

    @Test
    public void testPut() {
        String[] param = "PUT 1 2 3 4 5".split(" ");
        cr.put(CashRegisterParamBuilder.generateParam(param));
        Assert.assertTrue("Should have a total of 68", wallet.getTotalWalletAmount() == 68);
        Assert.assertEquals(wallet.getCountByDenomination(Denomination.TWENTY), new Integer(1));
        Assert.assertEquals(wallet.getCountByDenomination(Denomination.TEN), new Integer(2));
        Assert.assertEquals(wallet.getCountByDenomination(Denomination.FIVE), new Integer(3));
        Assert.assertEquals(wallet.getCountByDenomination(Denomination.TWO), new Integer(4));
        Assert.assertEquals(wallet.getCountByDenomination(Denomination.ONE), new Integer(5));
        Mockito.verify(printer, Mockito.times(1)).printCashStatus(wallet);
    }

    @Test
    public void testTakeWithoutInitialValue() throws InsufficientFundException, RollbackException {

        addWalletData(1, 2, 4, 5, 3);
        String[] param = "take 1 1 2 3 1".split(" ");
        cr.take(CashRegisterParamBuilder.generateParam(param));

        Assert.assertTrue(wallet.getCountByDenomination(Denomination.TWENTY) == 0);
        Assert.assertTrue(wallet.getCountByDenomination(Denomination.TEN) == 1);
        Assert.assertTrue(wallet.getCountByDenomination(Denomination.FIVE) == 2);
        Assert.assertTrue(wallet.getCountByDenomination(Denomination.TWO) == 2);
        Assert.assertTrue(wallet.getCountByDenomination(Denomination.ONE) == 2);
    }

    @Test
    public void testTakeWithInsufficientFund() throws RollbackException {
        String[] param = "take 1 2 3 4 5".split(" ");
        boolean hasInsuddicientError = false;
        try {
            cr.take(CashRegisterParamBuilder.generateParam(param));
        } catch (InsufficientFundException e) {
            hasInsuddicientError = true;
        }
        Assert.assertTrue(hasInsuddicientError);
    }

    @Test
    public void testTakeInsufficientFundShouldTriggerRollback() throws RollbackException {
        addWalletData(0, 0, 1, 3, 2);
        String[] param = "take 1 2 3 4 5".split(" ");
        try {
            cr.take(CashRegisterParamBuilder.generateParam(param));
        } catch (InsufficientFundException e) {
        }
        Mockito.verify(rollbackProcessor, Mockito.times(1)).getLastState();

    }

    @Test
    public void testChange() throws InsufficientFundException, RollbackException {
        addWalletData(0, 0, 1, 3, 2);
        cr.change(8);
        Assert.assertTrue(wallet.getTotalWalletAmount() == 5);
    }

    private void addWalletData(int twenty, int ten, int five, int two, int one) {
        wallet.addDenominationCount(Denomination.TWENTY, twenty);
        wallet.addDenominationCount(Denomination.TEN, ten);
        wallet.addDenominationCount(Denomination.FIVE, five);
        wallet.addDenominationCount(Denomination.TWO, two);
        wallet.addDenominationCount(Denomination.ONE, one);
    }
}
