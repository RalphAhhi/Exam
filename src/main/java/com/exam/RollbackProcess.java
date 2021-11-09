package com.exam;

import com.exam.constant.Const;
import com.exam.exception.RollbackException;

public class RollbackProcess implements IRollbackProcessor{
    private Wallet wallet ;
    private boolean isRollbackCreated;

    public void saveWalletState(Wallet wallet){
        this.wallet = new Wallet();
        Const.denominationList.forEach(denom->this.wallet.addDenominationCount(denom, wallet.getCountByDenomination(denom)));
        this.isRollbackCreated = true;
    }

    public Wallet getLastState() throws RollbackException {
        if(!isRollbackCreated){
            throw new RollbackException("No Rollback state found");
        }
        return wallet;
    }
}
