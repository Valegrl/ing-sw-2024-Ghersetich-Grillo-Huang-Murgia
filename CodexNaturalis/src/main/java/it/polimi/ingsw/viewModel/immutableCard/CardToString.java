package it.polimi.ingsw.viewModel.immutableCard;

/**
 * This interface represents the methods that a card must implement
 * to be viewed when a Command Line Interface(TUI) game is played.
 */
public interface CardToString {
    /**
     * Returns a string representing the front of a card during a Command Line Interface(TUI) game.
     *
     * @return A string representing the card details.
     */
    String printCard();

    /**
     * Returns a string representing the back of a card during a Command Line Interface(TUI) game.
     *
     * @return A string representing the card details.
     */
    default String printCardBack() {
        return "";
    };
}
