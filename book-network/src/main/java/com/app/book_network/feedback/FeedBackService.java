package com.app.book_network.feedback;

import com.app.book_network.book.Book;
import com.app.book_network.book.BookRepository;
import com.app.book_network.common.PageResponse;
import com.app.book_network.exception.OperationNotPermittedException;
import com.app.book_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final BookRepository bookRepository;
    private final FeedBackMapper feedBackMapper;

    public Long saveFeedBack(FeedBackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+request.bookId()));


        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You cannot give a feed back for book");
        }

        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("you cannot give a feed back for book");
        }

        FeedBack feedBack = feedBackMapper.toFeedBack(request);
        return feedBackRepository.save(feedBack).getId();
    }

    public PageResponse<FeedBackResponse> findAllFeedBackByBook(Long bookId, Integer page, Integer size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<FeedBack> feedBacks = feedBackRepository.findAllByBookId(bookId, pageable);
        List<FeedBackResponse> feedBackResponses = feedBacks.stream()
                .map(f -> feedBackMapper.toFeedBackResponse(f, user.getId())).toList();
        return new PageResponse<>(
            feedBackResponses,
                feedBacks.getNumber(),
                feedBacks.getSize(),
                feedBacks.getTotalElements(),
                feedBacks.getTotalPages(),
                feedBacks.isFirst(),
                feedBacks.isLast()
        );
    }
}
