package sa.springframework.spring6restmvc.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.json.JSONObject;
import org.h2.util.json.JSONString;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.mapper.BeerMapper;
import sa.springframework.spring6restmvc.model.BeerDTO;
import sa.springframework.spring6restmvc.repository.BeerRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {


    private static final Integer DEFAULT_PAGE_NUMBER = 25;
    private static final Integer DEFAULT_PAGE_SIZE = 25;
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers(String beerName, Integer pageNumber, Integer pageSize) {
        Page<Beer> beerPage;
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        pageRequest.withSort(Sort.by(Sort.Order.asc("beerName")));
        if(StringUtils.hasText(beerName)){
            beerPage = beerRepository.findAllByBeerName(beerName,pageRequest);
        }else{
            beerPage = beerRepository.findAll(pageRequest);
        }
        return  beerPage
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public List<BeerDTO> beerList() throws IOException, InterruptedException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/beer")
                .queryParam("beerName", "")
                .queryParam("pageNumber", 1)
                .queryParam("pageSize", 5);
       ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity("http://localhost:8084"+builder.toUriString(), String.class);
        log.info(stringResponseEntity.getBody());
       List<BeerDTO> beerDTOS = restTemplate.getForEntity("http://localhost:8084"+builder.toUriString(), new ArrayList<BeerDTO>().getClass()).getBody();
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8084"+builder.toUriString()))
//                .build();
//        HttpResponse<String> response = client.send(request,
//                HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
//        Gson gson = new Gson();
//        List<BeerDTO> beerDTOS1 = gson.fromJson(response.body(),new ArrayList<BeerDTO>().getClass());
        // Gson json objesini java objesine çevirirken kullanılır.
        return  beerDTOS;
    }
    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if ( pageNumber == null || pageNumber < 1 ) {
            queryPageNumber = DEFAULT_PAGE_NUMBER;
        } else {
            queryPageNumber = pageNumber - 1;
        }

        if ( pageSize == null || pageSize < 1 ) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            queryPageSize = pageSize;
        }
        return PageRequest.of(pageNumber - 1, pageSize);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(Beer beer) {
        beer.setId(UUID.randomUUID());
        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, Beer beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, Beer beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())){
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null){
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())){
                foundBeer.setUpc(beer.getUpc());
            }
            if (beer.getPrice() != null){
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
