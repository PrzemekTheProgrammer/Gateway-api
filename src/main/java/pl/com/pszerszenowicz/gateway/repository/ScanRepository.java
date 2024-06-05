package pl.com.pszerszenowicz.gateway.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.com.pszerszenowicz.gateway.model.Scan;
import pl.com.pszerszenowicz.gateway.model.dto.ShowScan;

import java.util.List;

public interface ScanRepository extends MongoRepository<Scan, String> {
    List<ShowScan> findByBarcode(String barcode);
}
