package tacos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }

    @Id
    private String id;
    private Date placedAt = new Date();

    @Field("customer")
    private User user;

    private List<Taco> tacos = new ArrayList<>();
    public void addDesign(Taco design) {
        this.tacos.add(design);
    }

    @NotBlank(message = "Podanie imienia i nazwiska jest obowiązkowe.")
    private String name;

    @NotBlank(message = "Podanie ulicy jest obowiązkowe.")
    private String street;

    @NotBlank(message = "Podanie miasta jest obowiązkowe.")
    private String city;

    @NotBlank(message = "Podanie województwa jest obowiązkowe.")
    private String state;

    @NotBlank(message = "Podanie kodu pocztowego jest obowiązkowe.")
    private String zip;

    @CreditCardNumber(message = "To nie jest prawidłowy numer karty kredytowej.")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Wartość musi być w formacie MM/RR.")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Nieprawidłowy kod CVV.")
    private String ccCVV;
}
