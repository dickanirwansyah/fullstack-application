package com.app.book_network.book;

import com.app.book_network.common.PageResponse;
import com.app.book_network.history.BorrowedBooksResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Book")
@RestController
@RequestMapping(value = "books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping(value = "/save-book")
    public ResponseEntity<Long> saveBook(@Valid @RequestBody BookRequest request,
                                         Authentication connectedUser){
        return ResponseEntity.ok().body(bookService.save(request, connectedUser));
    }

    @GetMapping(value = "/findbook-byid/{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id")Long bookId){
        return ResponseEntity.ok()
                .body(bookService.findById(bookId));
    }

    @GetMapping(value = "/findall-books")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                   @RequestParam(name = "size", defaultValue = "10",required = false) int size,
                                                                   Authentication connectedUser){
        return ResponseEntity.ok()
                .body(bookService.findAll(page, size, connectedUser));
    }

    @GetMapping(value = "/findall-books-by-owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                          @RequestParam(name = "size", defaultValue = "10",required = false) int size,
                                                                          Authentication connectedUser){
        return ResponseEntity.ok()
                .body(bookService.findAllBookByOwner(page, size, connectedUser));
    }

    @GetMapping(value = "/findall-book-borrowed-books")
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> findAllBorrowedBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                                    @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                                    Authentication connectedUser){

        return ResponseEntity.ok()
                .body(bookService.findAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping(value = "/findall-returned-books")
    public ResponseEntity<PageResponse<BorrowedBooksResponse>> findAllReturnedBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                                    @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                                                                    Authentication connectedUser){

        return ResponseEntity.ok()
                .body(bookService.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping(value = "/shareable/{book-id}")
    public ResponseEntity<Long> updateShareableStatus(@PathVariable("book-id")Long bookId, Authentication connectedUser){

        return ResponseEntity.ok()
                .body(bookService.updateShareableStatus(bookId, connectedUser));
    }

    @PatchMapping(value = "/archived/{book-id}")
    public ResponseEntity<Long> updateArchivedStatus(@PathVariable("book-id")Long bookId, Authentication connectedUser){

        return ResponseEntity.ok()
                .body(bookService.updateArchivedStatus(bookId, connectedUser));
    }

    @PostMapping(value = "/borrowed/{book-id}")
    public ResponseEntity<Long> borrowBook(@PathVariable("book-id")Long bookId, Authentication connectedUser){

        return ResponseEntity.ok()
                .body(bookService.borrowedBook(bookId, connectedUser));
    }

    @PatchMapping(value = "/borrow/return/{book-id}")
    public ResponseEntity<Long> returnBook(@PathVariable("book-id")Long bookId, Authentication connectedUser){

        return ResponseEntity.ok()
                .body(bookService.returnBorrowedBook(bookId, connectedUser));
    }

    @PatchMapping(value = "/borrow/return/approva/{book-id}")
    public ResponseEntity<Long> approveReturnBorrowBook(@PathVariable("book-id")Long bookId,Authentication authentication){
        return ResponseEntity.ok()
                .body(bookService.approveReturnBorrowedBook(bookId, authentication));
    }

    @PostMapping(value = "/cover/{book-id}",consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(@PathVariable("book-id")Long bookId,
                                                    @Parameter()
                                                    @RequestPart("file") MultipartFile file,
                                                    Authentication authentication){
        bookService.uploadBookCoverPicture(file, authentication, bookId);
        return ResponseEntity.accepted().build();
    }
}
