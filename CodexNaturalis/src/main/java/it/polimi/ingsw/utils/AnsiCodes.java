package it.polimi.ingsw.utils;

/**
 * This class provides ANSI escape codes for use in console output.
 * These codes can be used to clear lines, move the cursor, and change the color and style of text.
 */
public class AnsiCodes {
    // ANSI escape codes for clearing lines
    public static final String CLEAR_LINE = "\u001B[2K";
    public static final String MOVE_CURSOR_START = "\r";

    // ANSI escape codes for cursor movement
    public static final String CURSOR_UP_ONE = "\u001B[A";

    // ANSI escape codes for text colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\033[38;2;231;47;39m";
    public static final String GREEN = "\033[38;2;22;126;49m";
    public static final String GOLD = "\033[38;2;179;165;30m";
    public static final String PURPLE = "\033[38;2;148;22;128m";
    public static final String CYAN = "\033[38;2;119;199;200m";
    public static final String BEIGE = "\033[38;2;228;220;182m";

    public static final String BLUE_TOKEN = "\033[38;2;20;95;119m";
    public static final String RED_TOKEN = "\033[38;2;194;57;48m";
    public static final String GREEN_TOKEN = "\033[38;2;55;103;57m";
    public static final String YELLOW_TOKEN = "\033[38;2;197;158;66m";

    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    public static final String GOLD_BACKGROUND = "\u001B[48;2;179;165;30m";
}
