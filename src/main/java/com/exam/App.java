package com.exam;

public class App {
    public static void main(String[] args) {
        IRollbackProcessor rbProcessor = new RollbackProcess();
        ICashHolderPrinter printer = new CashHolderPrinter();
        Wallet wallet = new Wallet();
        ICashRegister cashRegister = new CashRegister(wallet, printer, rbProcessor);
        ICashRegisterProcessor processor = new CashRegisterProcessor(cashRegister);
        CashRegisterApplication app = new CashRegisterApplication(processor);
        app.start();
    }
}
