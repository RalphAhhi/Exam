package com.exam;

import com.exam.constant.Const;

import java.security.InvalidParameterException;
import java.util.*;

public class CashRegisterParamBuilder {

    public static DenomiationParams generateParam(String[] params) {
        DenomiationParams denomiationParams = new DenomiationParams();
        List<Denomination> denominationList = Const.denominationList;
        for (int x = 1; x < params.length; x++) {
            if (x - 1 < denominationList.size()){
                if(!params[x].equals("")){
                    denomiationParams.addDenominationCount(denominationList.get(x - 1), Integer.parseInt(params[x]));
                }else{
                    throw new InvalidParameterException();
                }
            }else{
                break;
            }
        }
        return denomiationParams;
    }
}
