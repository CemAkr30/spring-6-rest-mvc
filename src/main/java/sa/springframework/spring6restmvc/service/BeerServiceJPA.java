package sa.springframework.spring6restmvc.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        soapRequest();
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


    private void soapRequest() {
        try {
            String url = "http://www.holidaywebservice.com/HolidayService_v2/HolidayService2.asmx?op=GetHolidaysAvailable";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            String countryCode = "Canada";
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"> " +
                    " <soap12:Body> " +
                    " <GetHolidaysAvailable xmlns=\"http://www.holidaywebservice.com/HolidayService_v2/\"> " +
                    " <countryCode>" + countryCode + "</countryCode>" +
                    " </GetHolidaysAvailable>" +
                    " </soap12:Body>" +
                    "</soap12:Envelope>";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(xml);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            System.out.println(responseStatus);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("response:" + response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
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
