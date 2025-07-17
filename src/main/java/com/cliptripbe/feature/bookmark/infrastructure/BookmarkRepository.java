package com.cliptripbe.feature.bookmark.infrastructure;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUser(User user);

    Optional<Bookmark> findByName(String s);

    @Query("SELECT b FROM Bookmark b WHERE b.isDefault = true AND b.name != :excludedBookmarkName")
    List<Bookmark> findDefaultBookmarksExcludingName(
        @Param("excludedBookmarkName") String excludedBookmarkName);

    @Query("""
            SELECT COUNT(bp) > 0
            FROM BookmarkPlace bp
            WHERE bp.bookmark.user.id = :userId AND bp.place.id = :placeId
        """)
    boolean isPlaceBookmarkedByUser(@Param("userId") Long userId, @Param("placeId") Long placeId);

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.bookmarkPlaces bp WHERE b.id = :bookmarkId")
    Optional<Bookmark> findByIdWithBookmarkPlaces(@Param("bookmarkId") Long bookmarkId);

    @Query("SELECT b FROM Bookmark b " +
        "JOIN FETCH b.bookmarkPlaces bp " +
        "JOIN FETCH bp.place p " +
        "LEFT JOIN FETCH p.placeTranslations pt " +
        "WHERE b.id = :bookmarkId")
    Optional<Bookmark> findByIdWithPlacesAndTranslations(
        @Param("bookmarkId") Long bookmarkId
    );
}

