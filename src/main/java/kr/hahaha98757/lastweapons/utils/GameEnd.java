package kr.hahaha98757.lastweapons.utils;

public class GameEnd extends Exception {
    public GameEnd() {
        super("Unknown Error.");
    }

    public GameEnd(String message) {
        super(message);
    }
}