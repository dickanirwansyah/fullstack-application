package com.app.book_network.book;

import com.app.book_network.common.PageResponse;
import com.app.book_network.exception.OperationNotPermittedException;
import com.app.book_network.file.FileStorageService;
import com.app.book_network.history.BookHistoryTransaction;
import com.app.book_network.history.BookHistoryTransactionRepository;
import com.app.book_network.history.BorrowedBooksResponse;
import com.app.book_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookHistoryTransactionRepository bookHistoryTransactionRepository;
    private final FileStorageService fileStorageService;

    public Long save(BookRequest request, Authentication connectedUser){
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Long id){
        return bookRepository.findById(id)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID :: "+id));
    }

    public PageResponse<BookResponse> findAll(int page, int size, Authentication authentication){
        User user = ((User) authentication.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable,user.getId());
        List<BookResponse> booksResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                booksResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    public PageResponse<BookResponse> findAllBookByOwner(int page, int size, Authentication authentication){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        User user = ((User) authentication.getPrincipal());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOnerId(user.getId()),pageable);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast());
    }

    public PageResponse<BorrowedBooksResponse> findAllBorrowedBooks(int page, int size, Authentication authentication){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        User user = ((User) authentication.getPrincipal());
        Page<BookHistoryTransaction> allBorrowedBooks = bookHistoryTransactionRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBooksResponse> borrowedBookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBooksResponse> findAllReturnedBooks(int page, int size, Authentication authentication){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        User user = ((User) authentication.getPrincipal());
        Page<BookHistoryTransaction> allReturnedBooks = bookHistoryTransactionRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBooksResponse> returnedBookResponses = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                returnedBookResponses,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast());
    }

    public Long updateShareableStatus(Long bookId, Authentication authentication) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));
        User user = ((User) authentication.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot update books sharedable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Long updateArchivedStatus(Long bookId, Authentication authentication) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));
        User user = ((User) authentication.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot update books archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Long borrowedBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));

        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        final boolean isAlreadyBorrowred = bookHistoryTransactionRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowred){
            throw new OperationNotPermittedException("The requested is already borrowed");
        }

        BookHistoryTransaction historyTransaction = BookHistoryTransaction
                .builder().user(user).book(book).returned(false).returnApproved(false).build();

        return bookHistoryTransactionRepository.save(historyTransaction).getId();
    }

    public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));

        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }

        User user = ((User) connectedUser.getPrincipal());
        if (!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot borrowed or return your own book");
        }

        BookHistoryTransaction historyTransaction = bookHistoryTransactionRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("You did not borrow this book"));

        historyTransaction.setReturned(true);
        return bookHistoryTransactionRepository.save(historyTransaction).getId();
    }

    public Long approveReturnBorrowedBook(Long bookId, Authentication authentication) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));

        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }

        User user = ((User) authentication.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookHistoryTransaction historyTransaction = bookHistoryTransactionRepository.findByBookIdAndOwnerId(bookId,user.getId())
                .orElseThrow(() -> new EntityNotFoundException("The book is not returned yet. You cannot approved it"));

        historyTransaction.setReturnApproved(true);
        return bookHistoryTransactionRepository.save(historyTransaction).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication authentication, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));

        User user = ((User) authentication.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
