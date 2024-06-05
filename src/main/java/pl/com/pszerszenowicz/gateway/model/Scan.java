package pl.com.pszerszenowicz.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.com.pszerszenowicz.model.ProductInfo;

import java.math.BigInteger;

@Document(collection = "scan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scan {

    @Id
    private String id;
    private ScanType scanType;
    private String deviceId;
    private String userId;
    private String barcode;
    private ScanStatus status;
    private ProductInfo productInfo;


}
