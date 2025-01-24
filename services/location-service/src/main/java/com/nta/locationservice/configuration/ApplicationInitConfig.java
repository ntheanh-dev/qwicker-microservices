package com.nta.locationservice.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

  //    @Bean
  //    @ConditionalOnProperty(
  //            prefix = "spring",
  //            value = "datasource.driverClassName",
  //            havingValue = "com.mysql.cj.jdbc.Driver")
  //    ApplicationRunner initApplicationRunner(VehicleRepository vehicleRepository) {
  //        return args -> {
  //            log.info("Initializing application...");
  //
  //        };
  //    }
}
