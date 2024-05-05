package it.polimi.ingsw.immutableModel.immutableCard;

public interface ViewCard {
    String printCard();

    default String printCardBack() {
        return "";
    };
}
