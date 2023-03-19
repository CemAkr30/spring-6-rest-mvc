package sa.springframework.spring6restmvc.service;



import sa.springframework.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;


/**
 * Created by jt, Spring Framework Guru.
 */
public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csvFile);
}
