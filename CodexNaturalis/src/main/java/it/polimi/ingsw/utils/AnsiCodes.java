package it.polimi.ingsw.utils;

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

    public static final String BOLD = "\u001B[1m";
}
