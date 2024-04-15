package it.polimi.ingsw.model.evaluator;

import it.polimi.ingsw.model.card.EvaluableCard;
import it.polimi.ingsw.model.player.PlayArea;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicEvaluatorTest {

    @Test
    void calculatePoints() {

        PlayArea playArea = Mockito.mock(PlayArea.class);

        EvaluableCard evaluableCard = Mockito.mock(EvaluableCard.class);

        Pair<Coordinate, EvaluableCard> pair = new Pair<>(null, evaluableCard);

        when(playArea.getSelectedCard()).thenReturn(pair);
        when(evaluableCard.getPoints()).thenReturn(5);

        Evaluator basicEvaluator = new BasicEvaluator();

        int points = basicEvaluator.calculatePoints(playArea);

        assertEquals(5, points);

        verify(playArea, times(1)).getSelectedCard();
        verify(evaluableCard, times(1)).getPoints();
    }
}