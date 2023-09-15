package com.example.apidocs.openapi.exception;

public class MismatchedColumnCountException extends RuntimeException {

        public MismatchedColumnCountException(String message) {
            super(message);
        }

        public MismatchedColumnCountException() {
            super("Number of columns must match number of headers");
        }
}
