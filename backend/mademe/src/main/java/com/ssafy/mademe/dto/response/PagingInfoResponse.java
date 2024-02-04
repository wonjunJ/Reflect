package com.ssafy.mademe.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingInfoResponse {
    private int totalPage;
    private int pageNum;
    private boolean isFirst;
    private boolean hasNext;
}
