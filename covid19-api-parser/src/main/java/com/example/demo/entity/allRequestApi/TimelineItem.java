package com.example.demo.entity.allRequestApi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimelineItem {

    @JsonProperty("map")
    private Map<String, TimelineItemInfo> timelineItem;

    @JsonCreator
    public TimelineItem(Map<String, TimelineItemInfo> timelineItem) {
        this.timelineItem = timelineItem;
    }
}
