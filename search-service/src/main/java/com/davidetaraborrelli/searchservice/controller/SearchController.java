package com.davidetaraborrelli.searchservice.controller;

import com.davidetaraborrelli.searchservice.dto.SearchResponse;
import com.davidetaraborrelli.searchservice.dto.SearchResultItem;
import com.davidetaraborrelli.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam("q") String query,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {

        List<SearchResultItem> results = searchService.search(query, limit);
        SearchResponse response = new SearchResponse(query, results.size(), results);
        return ResponseEntity.ok(response);
    }
}
