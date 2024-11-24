package com.nta.locationservice.service;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.nta.locationservice.dto.response.DurationBingMapApiResponse;
import com.nta.locationservice.repository.httpClient.BingMapClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BingMapClientService {
    private final BingMapClient bingMapClient;

    private static final String BING_MAP_KEY = "AiG0p7k1VuqiubVqZ22aZXS6HEih9Yg95wRzucCj_gRvT0HeaMMuanyX13L4qGfd";

    public int getNearestShipperIndex(Point myPoint, List<Point> points) {
        List<DurationBingMapApiResponse> responses = getBingMapDurationsAsync(myPoint, points);
        OptionalInt minIndex = IntStream.range(0, responses.size())
                .reduce((i, j) -> responses
                                        .get(i)
                                        .getResourceSets()
                                        .getFirst()
                                        .getResources()
                                        .getFirst()
                                        .getTravelDuration()
                                < responses
                                        .get(j)
                                        .getResourceSets()
                                        .getFirst()
                                        .getResources()
                                        .getFirst()
                                        .getTravelDuration()
                        ? i
                        : j);
        if (minIndex.isPresent()) {
            log.info(String.valueOf(minIndex.getAsInt()));
            return minIndex.getAsInt();
        }
        return 0;
    }

    private List<DurationBingMapApiResponse> getBingMapDurationsAsync(Point myPoint, List<Point> points) {
        List<CompletableFuture<DurationBingMapApiResponse>> futures = points.stream()
                .map(point -> CompletableFuture.supplyAsync(() -> {
                            String wp0 = String.format("%.10f,%.10f", myPoint.getY(), myPoint.getX());
                            String wp1 = String.format("%.10f,%.10f", point.getY(), point.getX());
                            try {
                                return bingMapClient.getBingMap("json", wp0, wp1, BING_MAP_KEY);
                            } catch (Exception e) {
                                log.error("Cannot call API Bing Map {}", e.getMessage());
                                return null;
                            }
                        })
                        .exceptionally(ex -> {
                            log.error("Error when execute async tasks: {}", ex.getMessage());
                            return null;
                        }))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
