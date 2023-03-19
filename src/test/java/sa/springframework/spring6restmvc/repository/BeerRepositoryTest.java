package sa.springframework.spring6restmvc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sa.springframework.spring6restmvc.bootstrap.DataLoader;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.service.BeerCsvServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({DataLoader.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBear(){
        Beer savedBeer = beerRepository.save(Beer.builder().beerName("My Beer").build());
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}