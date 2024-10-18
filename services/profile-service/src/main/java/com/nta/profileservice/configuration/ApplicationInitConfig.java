package com.nta.profileservice.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nta.profileservice.entity.Vehicle;
import com.nta.profileservice.repository.VehicleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner initApplicationRunner(VehicleRepository vehicleRepository) {
        return args -> {
            log.info("Initializing application...");
            vehicleRepository.save(Vehicle.builder()
                    .id("1")
                    .icon(
                            "https://res.cloudinary.com/dqpo9h5s2/image/upload/v1706106196/vehicle_icon/gjisuqtnu1gl7rtpdron.png")
                    .name("Xe Máy")
                    .description("Vận chuyển mặt hàng nhỏ giá trị đến 3 triệu đồng")
                    .capacity(
                            "'2', '0.5 x 0.4 x 0.5 Mét - Lên đến 30kg', 'Vận chuyển mặt hàng nhỏ giá trị đến 3 triệu đồng', 'https://res.cloudinary.com/dqpo9h5s2/image/upload/v1706106196/vehicle_icon/gjisuqtnu1gl7rtpdron.png', 'Xe Máy'\n")
                    .build());
            vehicleRepository.save(Vehicle.builder()
                    .id("2")
                    .icon(
                            "https://res.cloudinary.com/dqpo9h5s2/image/upload/v1706106556/vehicle_icon/pkbqdybiilwiynh0yyxv.png")
                    .name("Xe Van 500 kg")
                    .description("Hoạt Động Tất Cả Khung Giờ | Chở Tối Đa 500Kg * 1.5CBM")
                    .capacity("1.7 x 1.2 x 1.2 Mét Lên đến 500 kg")
                    .build());
            vehicleRepository.save(Vehicle.builder()
                    .id("3")
                    .icon(
                            "https://res.cloudinary.com/dqpo9h5s2/image/upload/v1706106626/vehicle_icon/rqqk1cvmbt7q5agsgdry.png")
                    .name("Xe Van 1000 kg")
                    .description("Hoạt Động Tất Cả Khung Giờ | Chở Tối Đa 1000Kg * 4CBM")
                    .capacity("1.7 x 1.2 x 1.2 Mét Lên đến 500 kg")
                    .build());
            vehicleRepository.save(Vehicle.builder()
                    .id("4")
                    .icon(
                            "https://res.cloudinary.com/dqpo9h5s2/image/upload/v1706106669/vehicle_icon/lnnx9evnqsxy1gmygn23.png")
                    .name("Xe Tải 500kg")
                    .description("Giờ Cấm Tải 6H-9H & 16H-20H | Chở tối đa 500Kg & 1.5CBM")
                    .capacity("1.9 x 1.4 x 1.4 Mét Lên đến 500 kg")
                    .build());
            vehicleRepository.save(Vehicle.builder()
                    .id("5")
                    .icon(
                            "https://res.cloudinary.com/dqpo9h5s2/image/upload/v1706106704/vehicle_icon/enknv9eqjzcdpc10jxxo.png")
                    .name("Xe Tải 1000kg")
                    .description("Giờ Cấm Tải 6H-9H & 16H-20H | Chở tối đa 1000Kg & 5CBM")
                    .capacity("3 x 1.6 x 1.6 Mét Lên đến 1000 kg")
                    .build());
        };
    }
}