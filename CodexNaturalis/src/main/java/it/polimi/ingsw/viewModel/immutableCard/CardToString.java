package it.polimi.ingsw.viewModel.immutableCard;

/**
 * This interface represents the methods that a card must implement
 * to be viewed when a Command Line Interface(TUI) game is played.
 */
public interface CardToString {
    /**
     * Prints the front of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    String printCard();

    /**
     * Prints the back of a card during a Command Line Interface(TUI) game.
     *
     * @return a string representing the card details.
     */
    default String printCardBack() {
        return "";
    };
}
