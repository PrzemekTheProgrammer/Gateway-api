package pl.com.pszerszenowicz.gateway.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.pszerszenowicz.gateway.model.dto.ReceivedScan;
import pl.com.pszerszenowicz.gateway.model.dto.ShowScan;
import pl.com.pszerszenowicz.gateway.service.ScanService;

import java.util.List;

@RestController
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    ScanService scanService;

    @PostMapping("/send")
    public ResponseEntity<?> sendScan(@Valid @RequestBody ReceivedScan receivedScan)
    {
        return scanService.sendScan(receivedScan);
    }

    @GetMapping("/GetScansByBarcode/{barcode}")
    public List<ShowScan> getScansByBarcode(@PathVariable String barcode) {
        return scanService.getScansByBarcode(barcode);
    }

}
