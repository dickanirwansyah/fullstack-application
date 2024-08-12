package com.app.book_network.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> withOnerId(Long ownerId){
        return (root, query,criteraiBuilder) -> criteraiBuilder.equal(root.get("owner").get("id"), ownerId);
    }

}
