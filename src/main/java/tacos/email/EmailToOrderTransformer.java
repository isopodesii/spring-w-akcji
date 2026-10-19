package tacos.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;

@Component
public class EmailToOrderTransformer extends AbstractMailMessageTransformer<Order> {

    private static final String SUBJECT_KEYWORDS = "TACO ORDER";

    @Override
    protected AbstractIntegrationMessageBuilder<Order> doTransform(Message mailMessage) {
        Order tacoOrder = processPayload(mailMessage);
        return MessageBuilder.withPayload(tacoOrder);
    }

    private Order processPayload(Message mailMessage) {
        try {
            String subject = mailMessage.getSubject();
            if (subject.toUpperCase().contains(SUBJECT_KEYWORDS)) {
                String email = ((InternetAddress) mailMessage.getFrom()[0]).getAddress();
                String content = mailMessage.getContent().toString();
                return parseEmailToOrder(email, content);
            }
        } catch (MessagingException | IOException e) {
            // Ciche przechwycenie wyjątków zgodnie z konwencją
        }
        return null;
    }

    private Order parseEmailToOrder(String email, String content) {
        Order order = new Order();

        // Jeśli Twoja klasa Order ma setter dla adresu e-mail, możesz go tutaj wywołać:
        // order.setDeliveryName(email);

        String[] lines = content.split("\\r?\\n");
        for (String line : lines) {
            if (line.trim().length() > 0 && line.contains(":")) {
                String[] lineSplit = line.split(":");
                String tacoName = lineSplit[0].trim();
                String ingredients = lineSplit[1].trim();
                String[] ingredientsSplit = ingredients.split(",");

                List<Ingredient> tacoIngredients = new ArrayList<>();
                for (String ingredientName : ingredientsSplit) {
                    Ingredient ingredient = lookupIngredient(ingredientName.trim());
                    if (ingredient != null) {
                        tacoIngredients.add(ingredient);
                    }
                }

                Taco taco = new Taco();
                taco.setName(tacoName);
                taco.setIngredients(tacoIngredients);

                // Upewnij się, że dopisałaś metodę addTaco(Taco taco) w klasie Order!
                // Alternatywnie możesz użyć: order.getTacos().add(taco);
                order.addTaco(taco);
            }
        }
        return order;
    }

    private Ingredient lookupIngredient(String ingredientName) {
        for (Ingredient ingredient : ALL_INGREDIENTS) {
            String ucIngredientName = ingredientName.toUpperCase();
            if (LevenshteinDistance.getDefaultInstance().apply(ucIngredientName, ingredient.getName()) < 3 ||
                    ucIngredientName.contains(ingredient.getName()) ||
                    ingredient.getName().contains(ucIngredientName)) {
                return ingredient;
            }
        }
        return null;
    }

    private static Ingredient[] ALL_INGREDIENTS = new Ingredient[] {
            new Ingredient("FLTO", "FLOUR TORTILLA", Type.WRAP),
            new Ingredient("COTO", "CORN TORTILLA", Type.WRAP),
            new Ingredient("GRBF", "GROUND BEEF", Type.PROTEIN),
            new Ingredient("CARN", "CARNITAS", Type.PROTEIN),
            new Ingredient("TMTO", "TOMATOES", Type.VEGGIES),
            new Ingredient("LETC", "LETTUCE", Type.VEGGIES),
            new Ingredient("CHED", "CHEDDAR", Type.CHEESE),
            new Ingredient("JACK", "MONTERREY JACK", Type.CHEESE),
            new Ingredient("SLSA", "SALSA", Type.SAUCE),
            new Ingredient("SRCR", "SOUR CREAM", Type.SAUCE)
    };
}