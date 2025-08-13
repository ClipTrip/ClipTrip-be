package com.cliptripbe.global.util;

import static com.cliptripbe.global.response.type.ErrorType.ACCESS_DENIED_EXCEPTION;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.global.response.exception.CustomException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessUtils {

    public static void checkBookmarkAccess(Bookmark bookmark, User user) {
        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException(ACCESS_DENIED_EXCEPTION);
        }
    }
}
