package pl.com.pszerszenowicz.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.pszerszenowicz.gateway.model.dto.ReceivedScan;
import pl.com.pszerszenowicz.gateway.service.ScanService;

@RestController
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    ScanService scanService;

    @PostMapping("/send")
    public ResponseEntity<?> sendScan(@RequestBody ReceivedScan receivedScan)
    {
        return scanService.sendScan(receivedScan);
    }

}
