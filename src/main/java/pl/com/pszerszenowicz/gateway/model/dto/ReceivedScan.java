package pl.com.pszerszenowicz.gateway.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import pl.com.pszerszenowicz.gateway.model.ScanType;

import java.math.BigInteger;

@Data
@Builder
public class ReceivedScan{

    @NotBlank(message = "Scan Type is required.")
    private ScanType scanType;
    @NotBlank(message = "Device Id is required.")
    private String deviceId;
    @NotBlank(message = "User Id is required.")
    private String userId;
    @NotBlank(message = "Barcode is required.")
    @Pattern(regexp = "^\\d{2,}$", message = "Barcode must contain at least 2 numbers and contain only numbers.")
    private String barcode;

}
