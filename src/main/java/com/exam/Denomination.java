package com.exam;

public enum Denomination {
    TWENTY(20)
    ,TEN(10)
    ,FIVE(5)
    ,TWO(2)
    ,ONE(1);

    private int value;

    Denomination(int value){
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
