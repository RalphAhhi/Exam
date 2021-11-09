package com.exam;

import com.exam.constant.Const;

public class CashHolderPrinter implements ICashHolderPrinter {

    private static final String CUR_SYMBOL = "$";

    @Override
    public void printCashStatus(Wallet wallet) {
        StringBuilder sb = new StringBuilder(CUR_SYMBOL);
        sb.append(wallet.getTotalWalletAmount()).append(" ");
        Const.denominationList.forEach(denom-> sb.append(wallet.getCountByDenomination(denom)).append(" "));
        System.out.println(sb.toString());
    }

}
