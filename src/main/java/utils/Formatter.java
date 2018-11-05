package utils;

public class Formatter {

    public static String formatInput(String input) {
        return input.toLowerCase()
                .replaceAll(Constants.HYPHEN_REGEX, " ");
    }
}
