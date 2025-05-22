package com.example.seller.dto.response;

import com.example.seller.domain.Watchlist;
import lombok.*;

@Getter
@Setter
@Builder
public class WatchlistResponse
{
    private ResponseStatus status;
    private Watchlist watchlist;
}
