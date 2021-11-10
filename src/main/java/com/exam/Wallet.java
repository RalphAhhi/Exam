package com.exam;

import com.exam.exception.InsufficientFundException;

import java.util.*;
import java.util.stream.Collectors;

public class Wallet {

    private Map<Denomination, WalletItem> walletItemByDenomincation = new HashMap<>();

    public void addDenominationCount(Denomination denomination, Integer count) {
        WalletItem walletItem = walletItemByDenomincation.get(denomination);
        if (walletItem == null) {
            walletItem = new WalletItem(denomination);
        }
        walletItem.setCount(walletItem.getCount() + count);
        walletItemByDenomincation.put(denomination, walletItem);
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

    public Integer deduct(Wallet changeWallet, Denomination denomination, int amount) {
        WalletItem item = getWalletItemByDenomination(denomination);
        int countToDeduct = amount / item.getDenomination().getValue();

        if (countToDeduct > item.getCount()) {
            countToDeduct = item.getCount();
        }
        int newCount = item.getCount() - countToDeduct;

        item.setCount(newCount);
        changeWallet.addDenominationCount(denomination, countToDeduct);
        return amount - (countToDeduct * denomination.getValue());

    }

    public Collection<WalletItem> getWalletItemsWithCount() {
        return walletItemByDenomincation.values().stream().filter(item->item.getCount()>0).collect(Collectors.toList());
    }

    public List<Denomination> getDenominationWithCOunt() {
        return walletItemByDenomincation.values().stream().filter(item->item.getCount()>0).map(i->i.getDenomination())
                .collect(Collectors.toList());
    }
}
