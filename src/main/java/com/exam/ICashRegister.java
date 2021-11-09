package com.exam;

import com.exam.exception.InsufficientFundException;
import com.exam.exception.RollbackException;

public interface ICashRegister {

    void show();
    void put(DenomiationParams denomiationParams);
    void take(DenomiationParams denomiationParams) throws InsufficientFundException, RollbackException;
    void change(int amount) throws InsufficientFundException, RollbackException;
}
