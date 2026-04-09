package vn.io.nguyen32.crm.configuration;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.cors")
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CorsProperties {
  List<String> allowedOrigins;
  List<String> allowedMethods;
  List<String> allowedHeaders;
  List<String> exposedHeaders;
  Boolean allowCredentials;
  Long maxAge;
}
