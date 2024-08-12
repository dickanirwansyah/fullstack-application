package com.app.book_network.book;

import com.app.book_network.file.FileUtils;
import com.app.book_network.history.BookHistoryTransaction;
import com.app.book_network.history.BorrowedBooksResponse;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookRequest request){
        return Book.builder().id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .isbn(request.isbn())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book){
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullName())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBooksResponse toBorrowedBookResponse(BookHistoryTransaction bookHistoryTransaction){
        return BorrowedBooksResponse.builder()
                .id(bookHistoryTransaction.getId())
                .title(bookHistoryTransaction.getBook().getTitle())
                .authorName(bookHistoryTransaction.getBook().getAuthorName())
                .isbn(bookHistoryTransaction.getBook().getIsbn())
                .synopsis(bookHistoryTransaction.getBook().getSynopsis())
                .rate(bookHistoryTransaction.getBook().getRate())
                .archived(bookHistoryTransaction.getBook().isArchived())
                .shareable(bookHistoryTransaction.getBook().isShareable())
                .owner(bookHistoryTransaction.getBook().getOwner().fullName())
                //.cover todo implement later
                .build();
    }
}
