package com.app.book_network.feedback;

import com.app.book_network.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedBackMapper {

    public FeedBack toFeedBack(FeedBackRequest request) {
        return FeedBack.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder().id(request.bookId())
                        .shareable(false).archived(false).build())
                .build();
    }

    public FeedBackResponse toFeedBackResponse(FeedBack feedBack, Long userId) {
        return FeedBackResponse.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedBack(Objects.equals(feedBack.getCreatedBy(), userId))
                .build();
    }
}
