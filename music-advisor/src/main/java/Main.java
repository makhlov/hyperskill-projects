import application.Application;

/**
 * Application entry point
 */
public class Main {
    public static void main(String[] args) {
        Application.getInstance(args).run();
    }
}