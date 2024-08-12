package com.app.book_network.feedback;

import com.app.book_network.book.Book;
import com.app.book_network.common.BaseEntity;
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
@Table(name = "tbl_feedback")
public class FeedBack extends BaseEntity {


    @Column(name = "note")
    private Double note;
    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}
