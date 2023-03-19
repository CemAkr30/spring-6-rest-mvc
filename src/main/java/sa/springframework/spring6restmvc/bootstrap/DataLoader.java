package sa.springframework.spring6restmvc.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.entities.Category;
import sa.springframework.spring6restmvc.model.BeerCSVRecord;
import sa.springframework.spring6restmvc.model.BeerStyle;
import sa.springframework.spring6restmvc.repository.BeerRepository;
import sa.springframework.spring6restmvc.repository.CategoryRepository;
import sa.springframework.spring6restmvc.service.BeerCsvService;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final BeerCsvService beerCsvService;

    private final BeerRepository beerRepository;

    private final CategoryRepository categoryRepository;

    public DataLoader(BeerCsvService beerCsvService, BeerRepository beerRepository, CategoryRepository categoryRepository) {
        this.beerCsvService = beerCsvService;
        this.beerRepository = beerRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.info("Loading Bootstrap Data");
//        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
//        List<BeerCSVRecord> recs   = beerCsvService.convertCSV(file);
//
//        recs.forEach(beerCSVRecord -> {
//            BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
//                case "American Pale Lager" -> BeerStyle.LAGER;
//                case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
//                        BeerStyle.ALE;
//                case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
//                case "American Porter" -> BeerStyle.PORTER;
//                case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
//                case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
//                case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
//                case "English Pale Ale" -> BeerStyle.PALE_ALE;
//                default -> BeerStyle.PILSNER;
//            };
//
//            beerRepository.save(Beer.builder()
//                    .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
//                    .beerStyle(beerStyle)
//                    .price(BigDecimal.TEN)
//                    .upc(beerCSVRecord.getIbu())
//                    .quantityOnHand(beerCSVRecord.getCount())
//                    .build());
//        });

        log.info("Loaded " + beerRepository.count() + " beers");

            Beer beer = new Beer();
            beer.setVersion(14214141);
            beer.setBeerName("Beer1");
            beer.setBeerStyle(BeerStyle.ALE);
            beer.setPrice(BigDecimal.TEN);
            beer.setUpc("123456789");
            beer.setQuantityOnHand(100);

       Category category = new Category();
       category.setVersion(14214141);
//       category.addBeer(beer);

       category.addBeer(beer);
       beer.addCategory(category);
       // beerRepository.save(beer); gerek yok zaten category save ederken beer save ediyor ALL CASCADE olduğu için relation da oluşturuyor
//        beerRepository.save(beer);
        categoryRepository.save(category);
    }


}
