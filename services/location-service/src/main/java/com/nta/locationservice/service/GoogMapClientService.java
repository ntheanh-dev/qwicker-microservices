package com.nta.locationservice.service;

import com.nta.locationservice.dto.response.GoogMapDistanceMatrixApiResponse;
import com.nta.locationservice.repository.httpClient.GoogMapClient;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogMapClientService {
  private final GoogMapClient googMapClient;
  private static final String KEY = "mSU8H8q4TEUcZmaniecYM0Tm4RLQuKOqAC8kzhGr";

  public int getNearestShipperIndex(final String origin, final List<String> destinations) {
    List<GoogMapDistanceMatrixApiResponse> responses =
        getGoogMapDistanceMatrixAsync(origin, destinations);
    return responses.stream()
        .map(r -> r.getRows().getFirst().getElements().getFirst().getDuration().getValue())
        .min(Integer::compareTo)
        .orElse(0);
  }

  private List<GoogMapDistanceMatrixApiResponse> getGoogMapDistanceMatrixAsync(
      final String origin, final List<String> destinations) {
    List<CompletableFuture<GoogMapDistanceMatrixApiResponse>> futures =
        destinations.stream()
            .map(
                d ->
                    CompletableFuture.supplyAsync(
                            () -> {
                              try {
                                return googMapClient.getDistance(origin, d, "car", KEY);
                              } catch (Exception e) {
                                log.error("Cannot call API Bing Map {}", e.getMessage());
                                return null;
                              }
                            })
                        .exceptionally(
                            ex -> {
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
