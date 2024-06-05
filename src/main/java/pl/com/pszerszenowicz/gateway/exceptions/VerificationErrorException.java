package pl.com.pszerszenowicz.gateway.exceptions;

public class VerificationErrorException extends RuntimeException{

    public VerificationErrorException(String message){
        super(message);
    }

}
