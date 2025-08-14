package com.cliptripbe.feature.bookmark.infrastructure;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.bookmark.infrastructure.projection.BookmarkMapping;
import com.cliptripbe.feature.bookmark.infrastructure.projection.PlaceBookmarkMapping;
import com.cliptripbe.feature.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface
BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUser(User user);

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.bookmarkPlaces WHERE b.isDefault = true AND b.name != :excludedBookmarkName")
    List<Bookmark> findDefaultBookmarksExcludingName(
        @Param("excludedBookmarkName") String excludedBookmarkName);


    @Query("SELECT DISTINCT b FROM Bookmark b " +
        "LEFT JOIN FETCH b.bookmarkPlaces bp " +
        "LEFT JOIN FETCH bp.place p " +
        "WHERE b.id = :bookmarkId")
    Optional<Bookmark> findByIdWithBookmarkPlaces(@Param("bookmarkId") Long bookmarkId);

    @EntityGraph(attributePaths = {
        "bookmarkPlaces",
        "bookmarkPlaces.place",
        "bookmarkPlaces.place.placeTranslations"
    })
    @Query("SELECT b FROM Bookmark b WHERE b.id = :bookmarkId")
    Optional<Bookmark> findByIdWithPlacesAndTranslations(@Param("bookmarkId") Long bookmarkId);

    @Query("""
            SELECT p.kakaoPlaceId as kakaoPlaceId, b.id as bookmarkId
            FROM BookmarkPlace bp
            JOIN bp.bookmark b
            JOIN bp.place p
            WHERE b.user.id = :userId
            AND p.kakaoPlaceId IN :kakaoPlaceIds
        """)
    List<BookmarkMapping> findBookmarkMappingsByUserAndKakaoPlaceIds(
        @Param("userId") Long userId,
        @Param("kakaoPlaceIds") List<String> kakaoPlaceIds
    );

    @Query("""
            SELECT p.id as placeId,
                   b.id as bookmarkId
            FROM BookmarkPlace bp1
            JOIN bp1.place p
            JOIN BookmarkPlace bp2 ON bp2.place.id = p.id
            JOIN bp2.bookmark b
            WHERE p.id IN :placeIdList
            AND b.user.id = :userId
            ORDER BY p.id, b.id
        """)
    List<PlaceBookmarkMapping> findPlaceBookmarkMappingsByUserAndPlaceIds(
        @Param("placeIdList") List<Long> placeIdList,
        @Param("userId") Long userId
    );

    @Query("""
        select p.id
        from BookmarkPlace bp
        join bp.place p
        where bp.bookmark.id = :bookmarkId
        order by bp.id
    """)
    List<Long> findPlaceIdsByBookmarkId(@Param("bookmarkId") Long bookmarkId);

    @Query("""
            SELECT b.id
            FROM BookmarkPlace bp
            JOIN bp.place p
            JOIN bp.bookmark b
            WHERE b.user.id = :userId
              AND p.id = :placeId
                      
        """)
    List<Long> findBookmarkIdsByUserIdAndPlaceId(
        @Param("userId") Long userId,
        @Param("placeId") Long placeId
    );
}

