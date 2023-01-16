package Exceptions;

public class IncorrectBoardException extends Exception {

    public  IncorrectBoardException() {
        message = "The board is incorrect";
    }

    public IncorrectBoardException(String message) {
        this.message = message;
    }

    private String message;
    @Override
    public String getMessage() {
        return message;
    }
}
