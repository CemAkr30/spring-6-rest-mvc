package sa.springframework.spring6restmvc.mapper;


import org.mapstruct.Mapper;
import sa.springframework.spring6restmvc.entities.Beer;
import sa.springframework.spring6restmvc.model.BeerDTO;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);

}
