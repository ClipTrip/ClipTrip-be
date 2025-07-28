package com.cliptripbe.feature.user.application;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.domain.entity.BookmarkPlace;
import com.cliptripbe.feature.bookmark.domain.serrvice.BookmarkFinder;
import com.cliptripbe.feature.bookmark.infrastructure.BookmarkRepository;
import com.cliptripbe.feature.user.api.dto.request.UserSignInRequestDto;
import com.cliptripbe.feature.user.api.dto.request.UserSignUpRequestDto;
import com.cliptripbe.feature.user.api.dto.response.UserInfoResponse;
import com.cliptripbe.feature.user.api.dto.response.UserLoginResponse;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.user.infrastructure.UserRepository;
import com.cliptripbe.global.auth.AuthService;
import com.cliptripbe.global.auth.jwt.entity.JwtToken;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserChecker userChecker;
    private final UserRepository userRepository;
    private final UserLoader userLoader;
    private final BookmarkFinder bookmarkFinder;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public UserInfoResponse signUp(UserSignUpRequestDto signUpDto) {
        userChecker.checkExistUserEmail(signUpDto.email());
        String encodePassword = passwordEncoder.encode(signUpDto.password());
        User user = signUpDto.toEntity(encodePassword);
        userRepository.save(user);

        List<Bookmark> defaultBookmarks = bookmarkFinder.getDefaultBookmarksExcludingStorageSeoul();

        List<Bookmark> newUserBookmarks = new ArrayList<>();

        for (Bookmark bm : defaultBookmarks) {
            Bookmark copiedBookmark = Bookmark
                .builder()
                .name(bm.getName())
                .user(user)
                .description(bm.getDescription())
                .build();
            if (bm.getBookmarkPlaces() != null) {
                for (BookmarkPlace originalBp : bm.getBookmarkPlaces()) {
                    BookmarkPlace newBookmarkPlace = BookmarkPlace.builder()
                        .bookmark(copiedBookmark)
                        .place(originalBp.getPlace())
                        .build();
                    copiedBookmark.addBookmarkPlace(newBookmarkPlace);
                }
            }
            newUserBookmarks.add(copiedBookmark); // Add the fully constructed copied bookmark
        }
        bookmarkRepository.saveAll(newUserBookmarks);
        return UserInfoResponse.from(user);
    }

    public UserLoginResponse userSignIn(UserSignInRequestDto userSignInRequestDto) {
        JwtToken tokens = authService.createAuthenticationToken(
            userSignInRequestDto.email(),
            userSignInRequestDto.password());
        User user = userLoader.findByEmail(userSignInRequestDto.email());

        return UserLoginResponse.of(tokens, user.getLanguage());
    }

    public List<UserInfoResponse> getAllUserInfo() {
        return userLoader.getAllUser()
            .stream()
            .map(UserInfoResponse::from)
            .collect(Collectors.toList());
    }
}
