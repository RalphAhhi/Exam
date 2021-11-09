package com.exam.exception;

public class InvalidCommandException extends Exception {


    public InvalidCommandException(String actionKey){
            super("Invalid command action key: " + actionKey);
    }
}
