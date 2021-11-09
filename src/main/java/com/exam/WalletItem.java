package com.exam;

public class WalletItem {
    private Denomination denomination;
    private int count;

    public WalletItem(Denomination denomination){
            this.denomination = denomination;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public void setDenomination(Denomination denomination) {
        this.denomination = denomination;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getTotal(){
        return denomination.getValue() * count;
    }

}
