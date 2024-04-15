package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatternEvaluatorTest {

    @Test
    void calculatePoints() {
        PlayArea playArea = Mockito.mock(PlayArea.class);

        EvaluableCard evaluableCard = Mockito.mock(EvaluableCard.class);

        Pair<Coordinate, EvaluableCard> pair = new Pair<>(null, evaluableCard);

        PlayableCard playableCard = Mockito.mock(PlayableCard.class);

        Map<Coordinate, PlayableCard> playedCards = new HashMap<>(){{
            put(new Coordinate(2, -2), playableCard);
            put(new Coordinate(1, -1), playableCard);
            put(new Coordinate(-1, 1), playableCard);
            put(new Coordinate(-2, 2), playableCard);
            put(new Coordinate(-3, 3), playableCard);
            put(new Coordinate(-4, 4), playableCard);
            put(new Coordinate(-5, 5), playableCard);
            put(new Coordinate(-6, 6), playableCard);
        }};

        Pair<Coordinate, Item>[] requiredPattern = new Pair[]{
                new Pair<>(new Coordinate(0, 0), Item.PLANT),
                new Pair<>(new Coordinate(-1, 1), Item.PLANT),
                new Pair<>(new Coordinate(-2, 2), Item.PLANT)
        };

        when(playArea.getSelectedCard()).thenReturn(pair);
        when(playArea.getPlayedCards()).thenReturn(playedCards);
        when(evaluableCard.getPoints()).thenReturn(5);
        when(evaluableCard.getRequiredPattern()).thenReturn(requiredPattern);
        when(playableCard.getPermanentResource())
                .thenReturn(Item.PLANT)
                .thenReturn(Item.FUNGI)
                .thenReturn(Item.PLANT)
                .thenReturn(Item.PLANT)
        ;

        Evaluator patternEvaluator = new PatternEvaluator();

        int points = patternEvaluator.calculatePoints(playArea);

        assertEquals(10, points);

        verify(playArea, times(1)).getSelectedCard();
        verify(playArea, times(1)).getPlayedCards();
        verify(evaluableCard, times(1)).getPoints();
        verify(evaluableCard, times(1)).getRequiredPattern();
        verify(playableCard, times(9)).getPermanentResource();
    }
}