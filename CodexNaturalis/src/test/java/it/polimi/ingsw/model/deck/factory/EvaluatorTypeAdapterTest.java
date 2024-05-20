package it.polimi.ingsw.model.deck.factory;

import org.junit.jupiter.api.Test;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.evaluator.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class EvaluatorTypeAdapterTest {
    private EvaluatorTypeAdapter evaluatorTypeAdapter;
    private JsonDeserializationContext context;
    private JsonElement jsonElement;
    private JsonObject jsonObject;

    @BeforeEach
    void setUp() {
        evaluatorTypeAdapter = new EvaluatorTypeAdapter();
        context = Mockito.mock(JsonDeserializationContext.class);
        jsonElement = Mockito.mock(JsonElement.class);
        jsonObject = new JsonObject();
        when(jsonElement.getAsJsonObject()).thenReturn(jsonObject);
    }

    @Test
    void testDeserializeBasicEvaluator() {
        jsonObject.addProperty("type", "BasicEvaluator");
        when(context.deserialize(jsonElement, BasicEvaluator.class)).thenReturn(new BasicEvaluator());
        Evaluator result = evaluatorTypeAdapter.deserialize(jsonElement, Evaluator.class, context);
        assertInstanceOf(BasicEvaluator.class, result);
    }

    @Test
    void testDeserializeCornerEvaluator() {
        jsonObject.addProperty("type", "CornerEvaluator");
        when(context.deserialize(jsonElement, CornerEvaluator.class)).thenReturn(new CornerEvaluator());
        Evaluator result = evaluatorTypeAdapter.deserialize(jsonElement, Evaluator.class, context);
        assertInstanceOf(CornerEvaluator.class, result);
    }

    @Test
    void testDeserializeItemEvaluator() {
        jsonObject.addProperty("type", "ItemEvaluator");
        when(context.deserialize(jsonElement, ItemEvaluator.class)).thenReturn(new ItemEvaluator());
        Evaluator result = evaluatorTypeAdapter.deserialize(jsonElement, Evaluator.class, context);
        assertInstanceOf(ItemEvaluator.class, result);
    }

    @Test
    void testDeserializePatternEvaluator() {
        jsonObject.addProperty("type", "PatternEvaluator");
        when(context.deserialize(jsonElement, PatternEvaluator.class)).thenReturn(new PatternEvaluator());
        Evaluator result = evaluatorTypeAdapter.deserialize(jsonElement, Evaluator.class, context);
        assertInstanceOf(PatternEvaluator.class, result);
    }

    @Test
    void testDeserializeUnknownEvaluator() {
        jsonObject.addProperty("type", "UnknownEvaluator");
        Evaluator result = evaluatorTypeAdapter.deserialize(jsonElement, Evaluator.class, context);
        assertNull(result);
    }

    @Test
    void testDeserializeNullJsonElement() {
        Evaluator result = evaluatorTypeAdapter.deserialize(null, Evaluator.class, context);
        assertNull(result);
    }
}