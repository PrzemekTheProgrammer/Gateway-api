package pl.com.pszerszenowicz.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scan {

    private ObjectId id;
    private ScanType scanType;
    private BigInteger deviceId;
    private BigInteger userId;
    private String barcode;
    private ScanStatus status;


}
