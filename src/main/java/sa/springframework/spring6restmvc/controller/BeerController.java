package sa.springframework.spring6restmvc.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.model.BeerDTO;
import sa.springframework.spring6restmvc.service.BeerService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(BeerController.BASE_URL)
public class BeerController {

    public static final String BASE_URL = "/api/beer";
    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<BeerDTO> listBeers(@RequestParam String beerName, @RequestParam Integer pageNumber, @RequestParam Integer pageSize){
        return beerService.listBeers(beerName,pageNumber,pageSize).getContent();
    }

    @RequestMapping( path = "/restTemplate", method = RequestMethod.GET)
    public List<BeerDTO> restTemplate() throws IOException, InterruptedException {
        return beerService.beerList();
    }

    @RequestMapping(path = "/{beerId}" , method = RequestMethod.GET)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID id){
        log.debug("Get Beer by Id - in controller");
        return beerService.getBeerById(id).get();
    }

    @RequestMapping(path = "/create" , method = RequestMethod.POST)
    public ResponseEntity createBeer(@Validated @RequestBody Beer beer, HttpServletResponse response){
        BeerDTO beerSaved = beerService.saveNewBeer(beer);
        response.addHeader("Location", "beer/deleteById/" + beerSaved.getId());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/deleteById/{beerId}" , method = RequestMethod.DELETE)
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID id){
        beerService.deleteById(id);
        return ResponseEntity.ok("Beer Deleted " + id.toString());
    }

    @RequestMapping(path = "/update/{beerId}" , method = RequestMethod.PUT)
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer beer){
       BeerDTO beerReturn =  beerService.updateBeerById(id, beer).get();
        return new ResponseEntity(beerReturn, HttpStatus.OK);
    }

    @RequestMapping(path = "/update/{beerId}" , method = RequestMethod.PATCH)
    public ResponseEntity patchBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer beer){
        BeerDTO beerReturn =  beerService.patchBeerById(id, beer).get();
        return new ResponseEntity(beerReturn, HttpStatus.OK);
    }

}