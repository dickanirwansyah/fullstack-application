package com.app.book_network.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookHistoryTransactionRepository extends JpaRepository<BookHistoryTransaction, Long>,
        JpaSpecificationExecutor<BookHistoryTransaction> {

    @Query(value = "select history from BookHistoryTransaction history where history.user.id=:userId")
    Page<BookHistoryTransaction> findAllBorrowedBooks(Pageable pageable, Long userId);

    @Query(value = "select history from BookHistoryTransaction  history where history.book.owner.id=:userId")
    Page<BookHistoryTransaction> findAllReturnedBooks(Pageable pageable, Long userId);

    @Query(value = "select (count(*) > 0) as isBorrowed from BookHistoryTransaction history where history.user.id=:userId and history.book.id=:bookId and history.returnApproved=false")
    boolean isAlreadyBorrowedByUser(Long bookId, Long userId);

    @Query("""
    select transaction from 
    BookHistoryTransaction transaction
    where transaction.user.id=:userId and 
    transaction.book.id=:bookId and 
    transaction.returned = false and
    transaction.returned = false
    """)
    Optional<BookHistoryTransaction> findByBookIdAndUserId(Long bookId, Long userId);

    @Query("""
        select transaction from BookHistoryTransaction transaction
        where transaction.book.owner.id=:userId 
        and transaction.book.id=:bookId
        and transaction.returned=true
        and transaction.returnApproved=false
    """)
    Optional<BookHistoryTransaction> findByBookIdAndOwnerId(Long bookId, Long userId);
}
