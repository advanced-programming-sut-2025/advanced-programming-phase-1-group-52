package com.example.main.auth;

/**
 * Result of authentication operation
 */
public class AuthResult {
    private final boolean success;
    private final String message;
    
    public AuthResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public AuthResult(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return "AuthResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
} 