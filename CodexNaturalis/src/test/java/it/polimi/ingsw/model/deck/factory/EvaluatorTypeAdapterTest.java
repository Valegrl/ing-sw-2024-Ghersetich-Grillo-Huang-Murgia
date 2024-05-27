package it.polimi.ingsw.model.deck.factory;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.evaluator.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EvaluatorTypeAdapterTest {
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
        assertThrows(JsonParseException.class, () -> evaluatorTypeAdapter.deserialize(jsonElement, Evaluator.class, context));
    }

    @Test
    void testDeserializeNullJsonElement() {
        Evaluator result = evaluatorTypeAdapter.deserialize(null, Evaluator.class, context);
        assertNull(result);
    }

    @Test
    void testSerializeBasicEvaluator() {
        EvaluatorTypeAdapter adapter = new EvaluatorTypeAdapter();
        JsonSerializationContext context = Mockito.mock(JsonSerializationContext.class);
        Evaluator basicEvaluator = new BasicEvaluator();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "BasicEvaluator");
        Mockito.when(context.serialize(basicEvaluator, BasicEvaluator.class)).thenReturn(jsonObject);

        JsonElement result = adapter.serialize(basicEvaluator, Evaluator.class, context);
        assertEquals("BasicEvaluator", result.getAsJsonObject().get("type").getAsString());
    }

    @Test
    void testSerializeCornerEvaluator() {
        EvaluatorTypeAdapter adapter = new EvaluatorTypeAdapter();
        JsonSerializationContext context = Mockito.mock(JsonSerializationContext.class);
        Evaluator cornerEvaluator = new CornerEvaluator();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "CornerEvaluator");
        Mockito.when(context.serialize(cornerEvaluator, CornerEvaluator.class)).thenReturn(jsonObject);

        JsonElement result = adapter.serialize(cornerEvaluator, Evaluator.class, context);
        assertEquals("CornerEvaluator", result.getAsJsonObject().get("type").getAsString());
    }

    @Test
    void testSerializeItemEvaluator() {
        EvaluatorTypeAdapter adapter = new EvaluatorTypeAdapter();
        JsonSerializationContext context = Mockito.mock(JsonSerializationContext.class);
        Evaluator itemEvaluator = new ItemEvaluator();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "ItemEvaluator");
        Mockito.when(context.serialize(itemEvaluator, ItemEvaluator.class)).thenReturn(jsonObject);

        JsonElement result = adapter.serialize(itemEvaluator, Evaluator.class, context);
        assertEquals("ItemEvaluator", result.getAsJsonObject().get("type").getAsString());
    }

    @Test
    void testSerializePatternEvaluator() {
        EvaluatorTypeAdapter adapter = new EvaluatorTypeAdapter();
        JsonSerializationContext context = Mockito.mock(JsonSerializationContext.class);
        Evaluator patternEvaluator = new PatternEvaluator();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "PatternEvaluator");
        Mockito.when(context.serialize(patternEvaluator, PatternEvaluator.class)).thenReturn(jsonObject);

        JsonElement result = adapter.serialize(patternEvaluator, Evaluator.class, context);
        assertEquals("PatternEvaluator", result.getAsJsonObject().get("type").getAsString());
    }

    @Test
    void testSerializeUnknownEvaluator() {
        EvaluatorTypeAdapter adapter = new EvaluatorTypeAdapter();
        JsonSerializationContext context = Mockito.mock(JsonSerializationContext.class);
        Evaluator unknownEvaluator = Mockito.mock(Evaluator.class);

        assertThrows(NullPointerException.class, () -> adapter.serialize(unknownEvaluator, Evaluator.class, context));
    }
}