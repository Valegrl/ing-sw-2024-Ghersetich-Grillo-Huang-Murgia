package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.card.EvaluableCard;
import it.polimi.ingsw.model.card.PlayableCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CornerEvaluatorTest {

    @Test
    void calculatePoints() {
        PlayArea playArea = Mockito.mock(PlayArea.class);

        EvaluableCard evaluableCard = Mockito.mock(EvaluableCard.class);

        Pair<Coordinate, EvaluableCard> pair = new Pair<>(new Coordinate(2, 0), evaluableCard);

        PlayableCard playableCard = Mockito.mock(PlayableCard.class);

        Map<Coordinate, PlayableCard> playedCards = new HashMap<>();
        playedCards.put(new Coordinate(-1, 1), playableCard);
        playedCards.put(new Coordinate(1, 1), playableCard);
        playedCards.put(new Coordinate(1, -1), playableCard);
        playedCards.put(new Coordinate(-1, -1), playableCard);

        when(playArea.getSelectedCard()).thenReturn(pair);
        when(playArea.getPlayedCards()).thenReturn(playedCards);
        when(evaluableCard.getPoints()).thenReturn(5);

        Evaluator cornerEvaluator = new CornerEvaluator();

        int points = cornerEvaluator.calculatePoints(playArea);

        assertEquals(10, points);

        verify(playArea, times(1)).getSelectedCard();
        verify(playArea, times(1)).getPlayedCards();
        verify(evaluableCard, times(1)).getPoints();
    }
}