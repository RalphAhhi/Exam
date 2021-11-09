package com.exam;

import com.exam.exception.InsufficientFundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Wallet {

    private Map<Denomination, WalletItem> walletItemByDenomincation = new HashMap<>();

    public void addDenominationCount(Denomination denomination, Integer count) {
        WalletItem walletItem = walletItemByDenomincation.get(denomination);
        if (walletItem == null) {
            walletItem = new WalletItem(denomination);
        }
        walletItem.setCount(walletItem.getCount() + count);
        walletItemByDenomincation.put(denomination,walletItem);
    }

    public void subtractDenominationCount(Denomination denomination, Integer count) throws InsufficientFundException {
        if (walletItemByDenomincation.containsKey(denomination)) {
            int newValue = walletItemByDenomincation.get(denomination).getCount() - count;
            if (newValue < 0) {
                throw new InsufficientFundException();
            } else {
                walletItemByDenomincation.get(denomination).setCount(newValue);
            }
        } else {
            throw new InsufficientFundException();
        }
    }

    public Integer getCountByDenomination(Denomination denomination) {
        if (walletItemByDenomincation.containsKey(denomination)) {
            return walletItemByDenomincation.get(denomination).getCount();
        } else {
            return 0;
        }
    }

    public WalletItem getWalletItemByDenomination(Denomination denomination) {
        return walletItemByDenomincation.get(denomination);
    }

    public Integer getTotalWalletAmount() {
        return walletItemByDenomincation.values().stream().filter(Objects::nonNull).map(
                walletItem -> walletItem.getTotal()).reduce((a, b) -> a + b).get();
    }
}
