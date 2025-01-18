package com.nta.locationservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler__");
        scheduler.setPoolSize(200);
        return scheduler;
    }
}
