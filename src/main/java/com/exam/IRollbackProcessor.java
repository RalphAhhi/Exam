package com.exam;

import com.exam.exception.RollbackException;

public interface IRollbackProcessor {

    void saveWalletState(Wallet wallet);

    Wallet getLastState() throws RollbackException;
}
