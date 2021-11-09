package com.exam;

import com.exam.exception.InsufficientFundException;
import com.exam.exception.InvalidCommandException;
import com.exam.exception.RollbackException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CashRegisterProcessorTest {

    ICashRegisterProcessor crProcessor;
    @Mock
    ICashRegister cashRegister;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        crProcessor = new CashRegisterProcessor(cashRegister);
    }

    @Test
    public void testShowCommandShouldCallShowMethod() throws InvalidCommandException, InsufficientFundException, RollbackException {
        crProcessor.execute("Show");
        Mockito.verify(cashRegister,Mockito.times(1)).show();
    }
    @Test
    public void testPutCommandShouldCallPutMethod() throws InvalidCommandException, InsufficientFundException, RollbackException {
        String command=  "Put 1 2 3 4 5";
        crProcessor.execute(command);
        Mockito.verify(cashRegister,Mockito.times(1)).put(Mockito.any());
    }

    @Test
    public void testTakeCommandShouldCallTakeMethod() throws InvalidCommandException, InsufficientFundException, RollbackException {
        String command=  "take 1 2 3 4 5";
        crProcessor.execute(command);
        Mockito.verify(cashRegister,Mockito.times(1)).take(Mockito.any());
    }
}


