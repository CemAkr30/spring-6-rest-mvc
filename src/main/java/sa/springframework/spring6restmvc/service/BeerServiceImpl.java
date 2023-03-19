package sa.springframework.spring6restmvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.exception.NotFoundException;
import sa.springframework.spring6restmvc.genUtils.GenUtilMap;
import sa.springframework.spring6restmvc.mapper.BeerMapper;
import sa.springframework.spring6restmvc.model.BeerDTO;
import sa.springframework.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {


    private static final Integer DEFAULT_PAGE_NUMBER = 25;
    private static final Integer DEFAULT_PAGE_SIZE = 25;
    private Map<UUID, Beer> beerMap;
    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, Integer pageNumber, Integer pageSize){
        Page<BeerDTO> beerPage = (Page<BeerDTO>) new GenUtilMap<BeerDTO, Beer>().pojoToListDto(new BeerDTO(), new ArrayList<>(beerMap.values()));
        return beerPage ;
    }

    @Override
    public List<BeerDTO> beerList() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        List<BeerDTO> beerDTOS =
//                Collections.singletonList(
//                        restTemplate.getForObject("http://localhost:8084/api/beer?beerName&pageNumber=1&pageSize=5", BeerDTO.class)
//                );
        return  new GenUtilMap<BeerDTO, Beer>().pojoToListDto(new BeerDTO(), new ArrayList<>(beerMap.values()));
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
        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return Optional.ofNullable(
                Optional.of(
                        new GenUtilMap<BeerDTO, Beer>().pojoToDto(new BeerDTO(), beerMap.get(id))
                        )
                .orElseThrow(NotFoundException::new)
        );
    }

    @Override
    public BeerDTO saveNewBeer(Beer beer) {
        Beer saveBear = Beer.builder()
                .id(UUID.randomUUID())
                .version(beer.getVersion())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(beer.getQuantityOnHand())
                .build();
                beerMap.put(saveBear.getId(), saveBear);
        return new GenUtilMap<BeerDTO,Beer>().pojoToDto(new BeerDTO(), saveBear);
    }

    @Override
    public Boolean deleteById(UUID id) {
        log.info("Delete Beer by Id - in service. Id: " + id.toString());
        beerMap.remove(id);
        return Boolean.TRUE;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, Beer beer) {
        log.info("Update Beer by Id - in service. Id: " + id.toString());
       Beer beerOld = beerMap.get(id);
         if(beerOld != null) {
             beerOld.setBeerName(beer.getBeerName());
             beerOld.setBeerStyle(beer.getBeerStyle());
             beerOld.setPrice(beer.getPrice());
             beerOld.setQuantityOnHand(beer.getQuantityOnHand());
             beerOld.setUpdateDate(LocalDateTime.now());
             beerOld.setVersion(beer.getVersion());
             beerOld.setUpc(beer.getUpc());
         }
         beerMap.remove(id);
         beerMap.put(id, beerOld);
        return Optional.ofNullable(
                Optional.of(
                        new GenUtilMap<BeerDTO, Beer>().pojoToDto(new BeerDTO(), beerOld)
                        )
                .orElseThrow(NotFoundException::new)
        );
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID id, Beer beer) {
        log.info("Patch Beer by Id - in service. Id: " + id.toString());
        Beer beerOld = beerMap.get(id);
        if(beerOld != null) {
            beerOld.setBeerName(beer.getBeerName());
            beerOld.setBeerStyle(beer.getBeerStyle());
            beerOld.setPrice(beer.getPrice());
            beerOld.setQuantityOnHand(beer.getQuantityOnHand());
            beerOld.setUpdateDate(LocalDateTime.now());
            beerOld.setVersion(beer.getVersion());
            beerOld.setUpc(beer.getUpc());
        }
        //heap tarafında instance değiştiği için map içindeki değer verilen request body göre değişecek.
        return Optional.ofNullable(new GenUtilMap<BeerDTO,Beer>().pojoToDto(new BeerDTO(), beerOld));
    }
}
