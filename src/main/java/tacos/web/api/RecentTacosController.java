//package tacos.web.api;
//
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//
//import java.util.List;
//
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.rest.webmvc.RepositoryRestController;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import tacos.Taco;
//import tacos.data.TacoRepository;
//
//@RepositoryRestController
//public class RecentTacosController {
//
//    private final TacoRepository tacoRepo;
//
//    public RecentTacosController(TacoRepository tacoRepo) {
//        this.tacoRepo = tacoRepo;
//    }
//
//    @GetMapping(path="/tacos/recent", produces="application/hal+json")
//    public ResponseEntity<CollectionModel<TacoResource>> recentTacos() {
//        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
//        List<Taco> tacos = tacoRepo.findAll(page).getContent();
//
//        // Używamy zaktualizowanej metody toCollectionModel z Twojego assemblera
//        CollectionModel<TacoResource> recentResources =
//                new TacoResourceAssembler().toCollectionModel(tacos);
//
//        // Dodajemy link z wykorzystaniem nowych statycznych importów WebMvcLinkBuilder
//        recentResources.add(
//                linkTo(methodOn(RecentTacosController.class).recentTacos())
//                        .withRel("recents"));
//
//        return new ResponseEntity<>(recentResources, HttpStatus.OK);
//    }
//}