package com.dblab.webservice.domain.user.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReissueResponse {
    private final boolean isReissue;
    private final String oldRefreshToken;
    private final String newRefreshToken;
}
