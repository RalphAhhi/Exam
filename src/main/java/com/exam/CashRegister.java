package com.exam;


import com.exam.constant.Const;
import com.exam.exception.InsufficientFundException;
import com.exam.exception.RollbackException;

import java.util.*;
import java.util.stream.Collectors;

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

//    @Override
//    public void change(int amount) throws InsufficientFundException, RollbackException {
//        rollbackProcessor.saveWalletState(wallet);
//        if (amount > wallet.getTotalWalletAmount()) {
//            throw new InsufficientFundException();
//        }
//        int currentAmount = amount;
//        Wallet changeWallet = new Wallet();
//        for (Denomination denom : Const.denominationList) {
//            if (currentAmount == 0) {
//                break;
//            }
//            WalletItem walletItem = wallet.getWalletItemByDenomination(denom);
//            if (walletItem != null && walletItem.getCount() > 0
//                    && walletItem.getDenomination().getValue() <= currentAmount) {
//                int totalDeductable = currentAmount / denom.getValue();
//                int totalNewCount = walletItem.getCount() - totalDeductable;
//                if (totalNewCount < 0) {
//                    totalDeductable += totalNewCount;
//                }
//                wallet.subtractDenominationCount(denom, totalDeductable);
//                changeWallet.addDenominationCount(denom, totalDeductable);
//                currentAmount = currentAmount - (totalDeductable * denom.getValue());
//            }
//        }
//        if (changeWallet.getTotalWalletAmount() != amount) {
//            this.wallet = rollbackProcessor.getLastState();
//            throw new InsufficientFundException();
//        }
//        cashHolderPrinter.printCashStatus(changeWallet);
//    }

    @Override
    public void change(int amount) throws InsufficientFundException, RollbackException {
        rollbackProcessor.saveWalletState(wallet);
        if (amount > wallet.getTotalWalletAmount()) {
            throw new InsufficientFundException();
        }
        int currentAmount = amount;
        List<Denomination> denominationList = Const.denominationList.stream().filter(d -> d.getValue() <= amount).collect(Collectors.toList());
        Wallet changeWallet = new Wallet();
        for (Denomination denom : denominationList) {
            if (currentAmount == 0) {
                break;
            }
            currentAmount = wallet.deduct(changeWallet, denom, currentAmount);

        }
        revalidate(wallet,changeWallet,currentAmount);
        if (changeWallet.getTotalWalletAmount() != amount) {
            this.wallet = rollbackProcessor.getLastState();
            throw new InsufficientFundException();
        }
        cashHolderPrinter.printCashStatus(changeWallet);
    }

    private void revalidate(Wallet wallet, Wallet changeWallet, int amount) throws InsufficientFundException {
        if (amount == 0) {
            return;
        }
        List<Denomination> denominationList = changeWallet.getDenominationWithCOunt().
                stream().sorted(new DenominationDescSort()).collect(Collectors.toList());

        for (int x = 0; x < denominationList.size() - 1; x++) {
            Denomination denom = denominationList.get(x);
            while (changeWallet.getCountByDenomination(denom) > 0) {
                changeWallet.subtractDenominationCount(denom, 1);
                wallet.addDenominationCount(denom, 1);
                amount = amount + denom.getValue();
                for (int y = x + 1; y < denominationList.size(); y++) {
                    if (amount == 0) {
                        break;
                    }
                    amount = wallet.deduct(changeWallet, denominationList.get(y), amount);
                }
            }
        }
    }
}
