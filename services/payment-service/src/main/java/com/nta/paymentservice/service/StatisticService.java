package com.nta.paymentservice.service;

import com.nta.paymentservice.components.DateUtils;
import com.nta.paymentservice.dto.response.StatisticIncomeResponse;
import com.nta.paymentservice.enums.StatisticIncomeType;
import com.nta.paymentservice.repository.PaymentRepository;
import com.nta.paymentservice.repository.internal.PostClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
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
    DateUtils dateUtils;

    public List<StatisticIncomeResponse> getStatistics(
            final String startDate, final String endDate, final String t) throws ParseException {
        final StatisticIncomeType type = StatisticIncomeType.fromCode(t);
        final LocalDateTime from = dateUtils.parseDateTime(startDate);
        final LocalDateTime to = dateUtils.parseDateTime(endDate);
        final var shipperId = authenticationService.getUserDetail().getId();
        final List<StatisticIncomeResponse> response = new ArrayList<>();
        List<Object[]> results = null;
        switch (type) {
            case HOURLY -> {
                final List<String> postId =
                        postClient
                                .getAcceptedPostsByDateRange(
                                        "ACCEPTED",
                                        StatisticIncomeType.HOURLY.toString(),
                                        shipperId,
                                        startDate,
                                        endDate)
                                .getResult();
                if (postId.isEmpty()) {
                    log.info("No posts found for start date: {}, end date: {}", startDate, endDate);
                    return response;
                }
                results = paymentRepository.findHourlyIncome(from, to, postId);
            }
            case DAILY -> {
                final List<String> postId =
                        postClient
                                .getAcceptedPostsByDateRange(
                                        "ACCEPTED",
                                        StatisticIncomeType.DAILY.toString(),
                                        shipperId,
                                        startDate,
                                        endDate)
                                .getResult();
                if (postId.isEmpty()) {
                    log.info("No posts found for start date: {}, end date: {}", startDate, endDate);
                    return response;
                }
                results = paymentRepository.findDailyIncome(from, to, postId);
            }
            case MONTHLY -> {
                final List<String> postId =
                        postClient
                                .getAcceptedPostsByDateRange(
                                        "ACCEPTED",
                                        StatisticIncomeType.MONTHLY.toString(),
                                        shipperId,
                                        startDate,
                                        endDate)
                                .getResult();
                if (postId.isEmpty()) {
                    log.info("No posts found for start date: {}, end date: {}", startDate, endDate);
                    return response;
                }
                results = paymentRepository.findMonthlyIncome(from, to, postId);
            }
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
            case MONTHLY -> dateTime =
                    LocalDateTime.parse(result[0].toString() + "-01 00:00:00", formatter);
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
