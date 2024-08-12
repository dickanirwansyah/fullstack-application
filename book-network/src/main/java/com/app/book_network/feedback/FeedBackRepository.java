package com.app.book_network.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long>,
        JpaSpecificationExecutor<FeedBack> {

    @Query(value = """
        select feedback
        from FeedBack feedback
        where feedback.book.id=:bookId
       """)
    Page<FeedBack> findAllByBookId(Long bookId, Pageable pageable);
}
