package com.exam;

import com.exam.constant.Const;
import com.exam.exception.InsufficientFundException;
import com.exam.exception.RollbackException;

public class CashRegister implements ICashRegister {

    private Wallet wallet;
    private ICashHolderPrinter cashHolderPrinter;
    private IRollbackProcessor rollbackProcessor;

    /**
     * com.exam.Wallet when there is an existing instance wallet, you can transfer it to different cash register
     *
     * @param wallet
     * @param cashHolderPrinter
     */
    public CashRegister(Wallet wallet, ICashHolderPrinter cashHolderPrinter, IRollbackProcessor rollbackProcessor) {
        this.wallet = wallet;
        this.cashHolderPrinter = cashHolderPrinter;
        this.rollbackProcessor = rollbackProcessor;
    }

    @Override
    public void show() {
        cashHolderPrinter.printCashStatus(wallet);
    }

    @Override
    public void put(DenomiationParams denomiationParams) {
        denomiationParams.getDenominationKeys().forEach(key ->
                wallet.addDenominationCount(key, denomiationParams.getCountByDenomination(key)));
        cashHolderPrinter.printCashStatus(wallet);

    }

    @Override
    public void take(DenomiationParams denomiationParams) throws InsufficientFundException, RollbackException {
        rollbackProcessor.saveWalletState(wallet);
        try {
            for (Denomination key : denomiationParams.getDenominationKeys()) {
                wallet.subtractDenominationCount(key, denomiationParams.getCountByDenomination(key));
            }
            cashHolderPrinter.printCashStatus(wallet);
        } catch (InsufficientFundException e) {
            this.wallet = rollbackProcessor.getLastState();
            throw e;
        }

    }

    @Override
    public void change(int amount) throws InsufficientFundException, RollbackException {
        rollbackProcessor.saveWalletState(wallet);
        if (amount > wallet.getTotalWalletAmount()) {
            throw new InsufficientFundException();
        }
        int currentAmount = amount;
        Wallet changeWallet = new Wallet();
        for (Denomination denom : Const.denominationList) {
            if (currentAmount == 0) {
                break;
            }
            WalletItem walletItem = wallet.getWalletItemByDenomination(denom);
            if (walletItem != null && walletItem.getCount() > 0
                    && walletItem.getDenomination().getValue() <= currentAmount) {
                int totalDeductable = currentAmount / denom.getValue();
                int totalNewCount = walletItem.getCount() - totalDeductable;
                if (totalNewCount < 0) {
                    totalDeductable += totalNewCount;
                }
                wallet.subtractDenominationCount(denom, totalDeductable);
                changeWallet.addDenominationCount(denom, totalDeductable);
                currentAmount = currentAmount - (totalDeductable * denom.getValue());
            }
        }
        if (changeWallet.getTotalWalletAmount() != amount) {
            this.wallet = rollbackProcessor.getLastState();
            throw new InsufficientFundException();
        }
        cashHolderPrinter.printCashStatus(changeWallet);
    }
}
