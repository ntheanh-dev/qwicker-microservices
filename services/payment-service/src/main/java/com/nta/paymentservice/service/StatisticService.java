package com.nta.paymentservice.service;

import com.nta.paymentservice.dto.response.StatisticIncomeResponse;
import com.nta.paymentservice.enums.StatisticIncomeType;
import com.nta.paymentservice.repository.PaymentRepository;
import com.nta.paymentservice.repository.internal.PostClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StatisticService {
    AuthenticationService authenticationService;
    PostClient postClient;
    PaymentRepository paymentRepository;

    @PreAuthorize("hasRole('SHIPPER')")
    public List<StatisticIncomeResponse> getStatistics(final LocalDateTime startDate, final LocalDateTime endDate, final String t) {
        final StatisticIncomeType type = StatisticIncomeType.fromCode(t);

        final var shipperId = authenticationService.getUserDetail().getId();
        final List<StatisticIncomeResponse> response = new ArrayList<>();
        final List<String> postId = postClient.getAcceptedPostsByDateRange("ACCEPTED").getResult();
        if (postId.isEmpty()) {
            return response;
        }

        List<Object[]> results = null;
        switch (type) {
            case HOURLY -> results = paymentRepository.findHourlyIncome(startDate, endDate, postId);
            case DAILY -> results = paymentRepository.findDailyIncome(startDate, endDate, postId);
            case MONTHLY -> results = paymentRepository.findMonthlyIncome(startDate, endDate, postId);
            default -> {
                log.error("Unsupported time type: {}", type);
                return List.of();
            }
        }
        for (Object[] result : results) {
            response.add(toStatisticIncomeResponse(result, type));
        }

        return response;
    }

    private StatisticIncomeResponse toStatisticIncomeResponse(
            final Object[] result, final StatisticIncomeType type) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
        LocalDateTime dateTime = null;
        switch (type) {
            case HOURLY -> dateTime = LocalDateTime.parse(result[0].toString(), formatter);
            case DAILY -> dateTime = LocalDateTime.parse(result[0].toString() + " 00:00:00", formatter);
            case MONTHLY -> dateTime = LocalDateTime.parse(result[0].toString() + "-01 00:00:00", formatter);
        }
        return StatisticIncomeResponse.builder()
                .type(type)
                .dateTime(dateTime)
                .totalPayments((Long) result[1])
                .totalRevenue((BigDecimal) result[2])
                .cashRevenue((BigDecimal) result[3])
                .vnPayRevenue((BigDecimal) result[4])
                .build();
    }
}
