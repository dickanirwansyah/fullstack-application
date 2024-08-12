package com.app.book_network.book;

import com.app.book_network.common.BaseEntity;
import com.app.book_network.feedback.FeedBack;
import com.app.book_network.history.BookHistoryTransaction;
import com.app.book_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_book")
public class Book extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;
    @Column(name = "author_name", nullable = false, length = 150)
    private String authorName;
    @Column(name = "isbn", nullable = false, length = 100)
    private String isbn;
    @Column(name = "synopsis", nullable = false, length = 300)
    private String synopsis;
    @Column(name = "book_cover")
    private String bookCover;
    @Column(name = "archived")
    private boolean archived;
    @Column(name = "shareable")
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<FeedBack> feedBacks;

    @OneToMany(mappedBy = "book")
    private List<BookHistoryTransaction> bookHistoryTransactions;

    @Transient
    public double getRate(){
        if (feedBacks == null || feedBacks.isEmpty()){
            return 0.0;
        }
        var rate = this.feedBacks.stream()
                .mapToDouble(FeedBack::getNote)
                .average()
                .orElse(0.0);
        //3.23 --> 3.0 || 3.65 --> 4.0
        double roundRate = Math.round(rate * 10.0) / 10.0;
        return roundRate;
    }
}
