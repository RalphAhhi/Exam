package com.exam;

import com.exam.exception.InsufficientFundException;
import com.exam.exception.InvalidCommandException;
import com.exam.exception.RollbackException;

import java.security.InvalidParameterException;
import java.util.Scanner;

public class CashRegisterApplication implements ICashRegisterApplication {

    private ICashRegisterProcessor cashRegisterProcessor;

    public CashRegisterApplication(ICashRegisterProcessor cashRegisterProcessor) {
        this.cashRegisterProcessor = cashRegisterProcessor;
    }

    public void start() {
        System.out.println("Ready");
        while (true) {
            try {
                Scanner input = new Scanner(System.in);  // Create a Scanner object
                cashRegisterProcessor.execute(input.nextLine());
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            } catch (InsufficientFundException e) {
                System.out.println("Sorry");
            } catch (InvalidParameterException e) {
                System.out.println("Invalid Command params");
            } catch (RollbackException e) {
                System.out.println("Rollback is triggered but an error occured");
            }
        }
    }
}
