package com.exam;

import java.util.Comparator;

public class DenominationDescSort implements Comparator<Denomination> {

    @Override
    public int compare(Denomination o1, Denomination o2) {
        if(o1.getValue() >o2.getValue()){
            return -1;
        }else{
            return 1;
        }
    }
}
