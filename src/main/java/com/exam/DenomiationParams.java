package com.exam;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DenomiationParams {
    private Map<Denomination, Integer> countByDenomination = new HashMap<>();


    public Integer getCountByDenomination(Denomination denomination) {
        return countByDenomination.get(denomination);
    }

    public Set<Denomination> getDenominationKeys() {
        return countByDenomination.keySet();
    }

    public void addDenominationCount(Denomination denomination, Integer count){
        Integer currentCount = countByDenomination.get(denomination);
        if(currentCount == null){
            currentCount  = 0;
        }
        currentCount += count;
        countByDenomination.put(denomination,currentCount);
    }
}
