package com.nta.paymentservice.controller.internal;

import com.nta.paymentservice.dto.response.ApiResponse;
import com.nta.paymentservice.dto.response.StatisticIncomeResponse;
import com.nta.paymentservice.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {

    StatisticService statisticService;

    @GetMapping("/shipper-income")
    public ApiResponse<List<StatisticIncomeResponse>> getStatistic(@RequestParam LocalDateTime startDate,
                                                                   @RequestParam LocalDateTime endDate,
                                                                   @RequestParam String timeType) {
        return ApiResponse.<List<StatisticIncomeResponse>>builder().result(
                statisticService.getStatistics(startDate, endDate, timeType)
        ).build();
    }
}
