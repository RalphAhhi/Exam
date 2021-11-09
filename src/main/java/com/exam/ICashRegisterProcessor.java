package com.exam;

import com.exam.exception.InsufficientFundException;
import com.exam.exception.InvalidCommandException;
import com.exam.exception.RollbackException;

public interface ICashRegisterProcessor {

    void execute(String params) throws InvalidCommandException, InsufficientFundException, RollbackException;
}
