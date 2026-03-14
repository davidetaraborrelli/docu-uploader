package com.davidetaraborrelli.searchservice.dto;

import java.util.List;

public record SearchResponse(
        String query,
        int totalResults,
        List<SearchResultItem> results
) {}
