package com.example.seller.dto.response;

import com.example.seller.domain.Watchlist;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class AllWatchlistResponse
{
    private ResponseStatus status;
    private List<Watchlist> watchlists;
}
