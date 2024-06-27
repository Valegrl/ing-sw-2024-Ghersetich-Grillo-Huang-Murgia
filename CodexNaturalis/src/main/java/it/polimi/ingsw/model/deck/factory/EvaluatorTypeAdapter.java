package it.polimi.ingsw.model.deck.factory;

import com.google.gson.*;
import it.polimi.ingsw.model.evaluator.*;

import java.lang.reflect.Type;

/**
 * This class is a custom serializer and deserializer for the Evaluator class.
 * It implements JsonSerializer and JsonDeserializer interfaces provided by Gson.
 * The serialize method converts an Evaluator object into a JsonElement.
 * The deserialize method converts a JsonElement back into an Evaluator object.
 */
public class EvaluatorTypeAdapter implements JsonDeserializer<Evaluator>, JsonSerializer<Evaluator> {

    /**
     * This method is used to convert a JsonElement back into an Evaluator object.
     *
     * @param json The JsonElement being deserialized.
     * @param typeOfT The type of the Object to deserialize to.
     * @param context The context for deserialization.
     * @return An Evaluator object corresponding to the specified JsonElement.
     * @throws JsonParseException if json is not in the expected format of Evaluator.
     */
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

    /**
     * This method is used to convert an Evaluator object into a JsonElement.
     *
     * @param src The Evaluator object to be converted.
     * @param typeOfSrc The specific genericized type of src.
     * @param context The context for serialization.
     * @return A JsonElement corresponding to the specified Evaluator.
     * @throws JsonParseException if the Evaluator type is unknown.
     */
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
