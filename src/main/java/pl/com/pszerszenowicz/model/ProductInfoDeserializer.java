package pl.com.pszerszenowicz.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ProductInfoDeserializer implements Deserializer<ProductInfo> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public ProductInfo deserialize(String s, byte[] bytes) {
        try{
            if(bytes == null) {
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(bytes),ProductInfo.class);
        }   catch (Exception e) {
            System.out.println("Error when deserializing byte[] to ProductInfo");
            log.error("{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
