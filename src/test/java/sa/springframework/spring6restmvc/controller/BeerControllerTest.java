package sa.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.exception.NotFoundException;
import sa.springframework.spring6restmvc.genUtils.GenUtilMap;
import sa.springframework.spring6restmvc.mapper.BeerMapper;
import sa.springframework.spring6restmvc.model.BeerDTO;
import sa.springframework.spring6restmvc.repository.BeerRepository;
import sa.springframework.spring6restmvc.service.BeerService;
import sa.springframework.spring6restmvc.service.BeerServiceImpl;
import sa.springframework.spring6restmvc.service.BeerServiceJPA;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @MockBean
    BeerService beerService;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;


    BeerMapper beerMapper;


    BeerRepository beerRepository;

    BeerService beerServiceImpl;

    BeerService beerServiceJpa;
    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
        beerServiceJpa = new BeerServiceJPA(beerRepository,beerMapper,restTemplateBuilder);
    }

    @Test
    void testCreateNewBeer() throws Exception {
        objectMapper.findAndRegisterModules();
        BeerDTO beer = (BeerDTO) beerServiceImpl.listBeers(""
        ,1,1).get();
        //log.info(beer.toString());
        log.info(objectMapper.writeValueAsString(beer));
        beer.setId(null);
        BeerDTO beerSaved = beerServiceImpl.listBeers("",1,1).get(1);
        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerSaved);

        mockMvc.perform(post("/api/beer/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isOk());

    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceJpa.listBeers("",1,1).get(0);

        mockMvc.perform(put("/api/beer/update/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isOk());

        verify(beerService).updateBeerById(any(UUID.class),any(Beer.class));
        // method request atabiliyor muyuz kontrol eder. Fakat states kontrol edemez. Onun için given-when kullanılır.
    }


    @Test
    void testGetBeerById() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get("/api/beer/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBeer() throws Exception{
        BeerDTO beer = (BeerDTO) beerServiceImpl.listBeers("",1,1).get();

        mockMvc.perform(delete("/api/beer/deleteById/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteById(argumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(argumentCaptor.getValue());
    }


    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = (BeerDTO) beerServiceImpl.listBeers("",1,1).get();
        Beer beer1 = new GenUtilMap<BeerDTO,Beer>().dtoToPojo(beer,new Beer());
        mockMvc.perform(put("/api/beer/update/" + beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer1)))
                .andExpect(status().isOk());

        verify(beerService).updateBeerById(any(UUID.class),any(Beer.class));
    }

    @Test
    void listBeers() throws Exception {

        given(beerService.listBeers("",1,1)).willReturn(beerServiceImpl.listBeers("",1,1));

        mockMvc.perform(get("/api/beer")
                .accept(MediaType.APPLICATION_JSON)
                 .queryParam("beerName",""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(beerServiceImpl.listBeers("",1,1).get().toList().get(0).getId().toString()))
                .andExpect(jsonPath("$[0].beerName").value(beerServiceImpl.listBeers("",1,1).get().toList().get(0).getId().toString()))
                .andExpect(jsonPath("$[0].beerStyle").value(beerServiceImpl.listBeers("",1,1).get().toList().get(0).getId().toString()));
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeer = (BeerDTO) beerServiceImpl.listBeers("",1,1).get();
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.ofNullable(testBeer));

        mockMvc.perform(get("/api/beer/"+ UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testBeer.getId().toString()))
                .andExpect(jsonPath("$.beerName").value(testBeer.getBeerName()))
                .andExpect(jsonPath("$.beerStyle").value(testBeer.getBeerStyle().toString()))
                .andExpect(jsonPath("$.upc").value(testBeer.getUpc()));
    }
}