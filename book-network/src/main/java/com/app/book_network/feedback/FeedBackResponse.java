package com.app.book_network.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackResponse {

    private Double note;
    private String comment;
    private boolean ownFeedBack;

}
