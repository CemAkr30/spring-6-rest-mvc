package sa.springframework.spring6restmvc.service;


import org.springframework.data.domain.Page;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.model.BeerDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Page<BeerDTO> listBeers(String beerName, Integer pageNumber, Integer pageSize);

    List<BeerDTO> beerList() throws IOException, InterruptedException;

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(Beer beer);

    Boolean deleteById(UUID id);

    Optional<BeerDTO> updateBeerById(UUID id, Beer beer);

    Optional<BeerDTO> patchBeerById(UUID id, Beer beer);
}
