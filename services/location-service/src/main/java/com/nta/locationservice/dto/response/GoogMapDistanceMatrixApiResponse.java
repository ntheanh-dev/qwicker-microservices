package com.nta.locationservice.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoogMapDistanceMatrixApiResponse {
    private List<Row> rows;

    @Setter
    @Getter
    public static class Row {
        private List<Element> elements;

        @Setter
        @Getter
        public static class Element {
            private Distance distance;
            private Duration duration;
            private String status;

            @Setter
            @Getter
            public static class Distance {
                private String text;
                private int value;
            }

            @Setter
            @Getter
            public static class Duration {
                private String text;
                private int value;
            }
        }
    }
}
