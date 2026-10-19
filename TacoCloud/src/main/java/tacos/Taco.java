package tacos;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.annotation.Id;


@Data
@RestResource(rel = "tacos", path = "tacos")
@Document
public class Taco {

    @Id
    private String id;
    @NotNull
    @Size(min=5, message = "Nazwa musi składać się z przynajmniej pięciu znaków.")
    private String name;
    private Date createdAt = new Date();

    @Size(min=1, message = "Musisz wybrać przynajmniej jeden składnik.")
    private List<Ingredient> ingredients;

}
