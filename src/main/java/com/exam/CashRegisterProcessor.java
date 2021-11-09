package com.exam;

import com.exam.exception.InsufficientFundException;
import com.exam.exception.InvalidCommandException;
import com.exam.exception.RollbackException;

public class CashRegisterProcessor implements ICashRegisterProcessor {

    private ICashRegister cashRegister;

    public CashRegisterProcessor(ICashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }

    @Override
    public void execute(String params) throws InvalidCommandException, InsufficientFundException, RollbackException {
        String[] paramKeys = params.split(" ");
        String action = paramKeys[0].toUpperCase();
        switch (action) {
            case "PUT":
                cashRegister.put(CashRegisterParamBuilder.generateParam(paramKeys));
                break;
            case "TAKE":
                cashRegister.take(CashRegisterParamBuilder.generateParam(paramKeys));
                break;
            case "CHANGE":
                cashRegister.change(Integer.parseInt(paramKeys[1]));
                break;
            case "SHOW":
                cashRegister.show();
                break;
            case "EXIT":
                System.exit(0);
            default:
                throw new InvalidCommandException(action);
        }
    }
}
