package com.pblintern.web.Services;

import com.pblintern.web.Payload.Responses.BaseResponse;

public interface FavouriteService {
    BaseResponse<String> addFavourite(int id);

    BaseResponse<String> deleteFavourite(int id);
}
