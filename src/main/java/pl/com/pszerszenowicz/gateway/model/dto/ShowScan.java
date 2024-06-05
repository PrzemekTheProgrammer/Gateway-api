package pl.com.pszerszenowicz.gateway.model.dto;

import pl.com.pszerszenowicz.gateway.model.ScanStatus;
import pl.com.pszerszenowicz.gateway.model.ScanType;
import pl.com.pszerszenowicz.model.ProductInfo;

public record ShowScan(ScanType scanType,
        String deviceId,
        String userId,
        String barcode,
        ScanStatus status,
        ProductInfo productInfo) {
}
