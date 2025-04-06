package com.nta.paymentservice.controller;

import com.nta.paymentservice.dto.response.ApiResponse;
import com.nta.paymentservice.dto.response.StatisticIncomeResponse;
import com.nta.paymentservice.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StatisticController {

    StatisticService statisticService;

    @GetMapping("/shipper-income")
    @PreAuthorize("hasRole('SHIPPER')")
    public ApiResponse<List<StatisticIncomeResponse>> getStatistic(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "timeType") String type) throws ParseException {
        return ApiResponse.<List<StatisticIncomeResponse>>builder()
                .result(statisticService.getStatistics(startDate, endDate, type))
                .build();
    }
}
