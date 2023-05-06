package com.lucferreira.myanimeback.util;

import com.lucferreira.myanimeback.exception.ScrapeParseError;
import com.lucferreira.myanimeback.service.scraper.mal.media.MediaAnchors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ParseNumberTest {

    @Test
    void getIntValue_ReturnsInteger_WhenValidTargetAndParseTextMapProvided() throws ScrapeParseError {
        // Given
        MediaAnchors target = MediaAnchors.MEDIA_MEMBERS;
        String value = "100000";
        Map<MediaAnchors, String> parseTextMap = Map.of(target, value);

        // When
        Integer result = ParseNumber.getIntValue(target, parseTextMap);

        // Then
        Assertions.assertEquals(Integer.valueOf(value), result);
    }

    @Test
    void getIntValue_ReturnsNull_WhenTargetNotFoundInParseTextMap() throws ScrapeParseError {
        // Given
        MediaAnchors target = MediaAnchors.MEDIA_MEMBERS;
        Map<MediaAnchors, String> parseTextMap = Map.of();

        // When
        Integer result = ParseNumber.getIntValue(target, parseTextMap);

        // Then
        Assertions.assertNull(result);
    }

    @Test
    void getIntValue_ThrowsScrapeParseError_WhenInvalidValueFound() {
        // Given
        MediaAnchors target = MediaAnchors.MEDIA_MEMBERS;
        String value = "invalid_value";
        Map<MediaAnchors, String> parseTextMap = Map.of(target, value);

        // When & Then
        Assertions.assertThrows(ScrapeParseError.class, () -> ParseNumber.getIntValue(target, parseTextMap));
    }

    @Test
    void getDoubleValue_ReturnsDouble_WhenValidTargetAndParseTextMapProvided() throws ScrapeParseError {
        // Given
        MediaAnchors target = MediaAnchors.MEDIA_SCORE_VALUE;
        String value = "8.5";
        Map<MediaAnchors, String> parseTextMap = Map.of(target, value);

        // When
        Double result = ParseNumber.getDoubleValue(target, parseTextMap);

        // Then
        Assertions.assertEquals(Double.valueOf(value), result);
    }

    @Test
    void getDoubleValue_ReturnsNull_WhenTargetNotFoundInParseTextMap() throws ScrapeParseError {
        // Given
        MediaAnchors target = MediaAnchors.MEDIA_SCORE_VALUE;
        Map<MediaAnchors, String> parseTextMap = Map.of();

        // When
        Double result = ParseNumber.getDoubleValue(target, parseTextMap);

        // Then
        Assertions.assertNull(result);
    }

    @Test
    void getDoubleValue_ThrowsScrapeParseError_WhenInvalidValueFound() {
        // Given
        MediaAnchors target = MediaAnchors.MEDIA_SCORE_VALUE;
        String value = "invalid_value";
        Map<MediaAnchors, String> parseTextMap = Map.of(target, value);

        // When & Then
        Assertions.assertThrows(ScrapeParseError.class, () -> ParseNumber.getDoubleValue(target, parseTextMap));
    }
}
