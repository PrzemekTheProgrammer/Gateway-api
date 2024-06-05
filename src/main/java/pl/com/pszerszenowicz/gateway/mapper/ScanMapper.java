package pl.com.pszerszenowicz.gateway.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import pl.com.pszerszenowicz.gateway.model.Scan;
import pl.com.pszerszenowicz.gateway.model.dto.ShowScan;

@Component
public class ScanMapper {

    public ShowScan ScanToShowScan(Scan scan) {
        return new ShowScan(scan.getScanType(),
                scan.getDeviceId(),
                scan.getUserId(),
                scan.getBarcode(),
                scan.getStatus(),
                scan.getProductInfo());
    }

}
