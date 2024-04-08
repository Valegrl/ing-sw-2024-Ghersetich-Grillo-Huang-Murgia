package it.polimi.ingsw.model.deck.factory;

import com.google.gson.*;
import it.polimi.ingsw.model.evaluator.*;

import java.lang.reflect.Type;

public class EvaluatorDeserializer implements JsonDeserializer<Evaluator> {

    @Override
    public Evaluator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if(json == null) return null;
        JsonObject jsonObject = json.getAsJsonObject();
        String evaluatorType = jsonObject.get("type").getAsString();

        return switch (evaluatorType) {
            case ("BasicEvaluator") -> context.deserialize(json, BasicEvaluator.class);
            case ("CornerEvaluator") -> context.deserialize(json, CornerEvaluator.class);
            case ("ItemEvaluator") -> context.deserialize(json, ItemEvaluator.class);
            case ("PatternEvaluator") -> context.deserialize(json, PatternEvaluator.class);
            default -> null;
        };
    }
}
