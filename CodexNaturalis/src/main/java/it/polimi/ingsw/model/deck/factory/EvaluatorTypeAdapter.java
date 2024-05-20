package it.polimi.ingsw.model.deck.factory;

import com.google.gson.*;
import it.polimi.ingsw.model.evaluator.*;

import java.lang.reflect.Type;

public class EvaluatorTypeAdapter implements JsonDeserializer<Evaluator>, JsonSerializer<Evaluator> {

    @Override
    public Evaluator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if (json == null) return null;
        JsonObject jsonObject = json.getAsJsonObject();
        if (!jsonObject.has("type")) {
            throw new JsonParseException("Missing 'type' field in JSON object");
        }

        String evaluatorType = jsonObject.get("type").getAsString();

        return switch (evaluatorType) {
            case "BasicEvaluator" -> context.deserialize(json, BasicEvaluator.class);
            case "CornerEvaluator" -> context.deserialize(json, CornerEvaluator.class);
            case "ItemEvaluator" -> context.deserialize(json, ItemEvaluator.class);
            case "PatternEvaluator" -> context.deserialize(json, PatternEvaluator.class);
            default -> throw new JsonParseException("Unknown evaluator type: " + evaluatorType);
        };
    }

    @Override
    public JsonElement serialize(Evaluator src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) return JsonNull.INSTANCE;

        JsonObject jsonObject = context.serialize(src, src.getClass()).getAsJsonObject();

        if (src instanceof BasicEvaluator) {
            jsonObject.addProperty("type", "BasicEvaluator");
        } else if (src instanceof CornerEvaluator) {
            jsonObject.addProperty("type", "CornerEvaluator");
        } else if (src instanceof ItemEvaluator) {
            jsonObject.addProperty("type", "ItemEvaluator");
        } else if (src instanceof PatternEvaluator) {
            jsonObject.addProperty("type", "PatternEvaluator");
        } else {
            throw new JsonParseException("Unknown evaluator type: " + src.getClass().getSimpleName());
        }

        return jsonObject;
    }
}
