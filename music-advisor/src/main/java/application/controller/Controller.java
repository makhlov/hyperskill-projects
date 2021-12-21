package application.controller;

public interface Controller {
    boolean isSignedIn();
    boolean isExitCommandReceived();
    void perform(String command, String[] args);
}
