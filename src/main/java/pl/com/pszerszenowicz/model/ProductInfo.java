package pl.com.pszerszenowicz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

    private String name;
    private int quantity;
    private QuantityType quantityType;
    private String details;

    public enum QuantityType{
        ml,g,szt
    }

}
