package pl.com.pszerszenowicz.gateway.model.dto;

import lombok.Builder;
import lombok.Data;
import pl.com.pszerszenowicz.gateway.model.ScanType;

import java.math.BigInteger;

@Data
@Builder
public class ReceivedScan{

    private ScanType scanType;
    private BigInteger deviceId;
    private BigInteger userId;
    private String barcode;

}
