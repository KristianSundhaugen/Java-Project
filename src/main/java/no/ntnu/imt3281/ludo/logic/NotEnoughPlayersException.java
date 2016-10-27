package no.ntnu.imt3281.ludo.logic;

public class NotEnoughPlayersException extends Exception {
    public NotEnoughPlayersException(String message) {
        super(message);
    }
}
