package com.exam;

import com.exam.constant.Const;
import com.exam.exception.RollbackException;

import java.util.Stack;

public class RollbackProcess implements IRollbackProcessor{
    private Stack<Wallet> walletStates ;
    private boolean isRollbackCreated;

    public void saveWalletState(Wallet wallet){
        Wallet state = new Wallet();
        Const.denominationList.forEach(denom->state.addDenominationCount(denom, wallet.getCountByDenomination(denom)));
        walletStates.push(state);
        this.isRollbackCreated = true;
    }

    public Wallet getLastState() throws RollbackException {
        if(!isRollbackCreated){
            throw new RollbackException("No Rollback state found");
        }
        if(walletStates.size()==1){
            this.isRollbackCreated = false;
        }
        return walletStates.pop();
    }
}
