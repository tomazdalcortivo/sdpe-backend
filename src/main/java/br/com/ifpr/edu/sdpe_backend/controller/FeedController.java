package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.DTO.FeedPostDTO;
import br.com.ifpr.edu.sdpe_backend.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<Page<FeedPostDTO>> getFeed(
            @PageableDefault(page = 0, size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(feedService.buscarFeed(pageable));
    }

}