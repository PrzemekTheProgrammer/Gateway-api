package pl.com.pszerszenowicz.gateway.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfo {

    private String name;
    private String info;

}
