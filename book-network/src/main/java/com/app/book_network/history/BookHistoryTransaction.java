package com.app.book_network.history;

import com.app.book_network.book.Book;
import com.app.book_network.common.BaseEntity;
import com.app.book_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_book_his_transaction")
public class BookHistoryTransaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "returned")
    private boolean returned;
    @Column(name = "returned_approved")
    private boolean returnApproved;

}
