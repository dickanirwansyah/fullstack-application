package com.app.book_network.feedback;

import com.app.book_network.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedBackController {

    private final FeedBackService feedBackService;

    @PostMapping
    public ResponseEntity<Long> saveFeedBack(@Valid @RequestBody FeedBackRequest request,
                                             Authentication connectedUser){

        return ResponseEntity.ok()
                .body(feedBackService.saveFeedBack(request, connectedUser));
    }

    @GetMapping(value = "/book/{book-id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> findAllFeedBackByBook(@PathVariable("book-id")Long bookId,
                                                                                @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                                                @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                                                Authentication connectedUser){

        return ResponseEntity.ok()
                .body(feedBackService.findAllFeedBackByBook(bookId, page, size, connectedUser));
    }
}
