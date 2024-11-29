package com.bank.management.exception;

public class BadCredentialException extends RuntimeException  {
    public BadCredentialException() {
        super("Bad credentials");
    }
}
