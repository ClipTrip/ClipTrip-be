package com.cliptripbe.feature.bookmark.infrastructure;

import com.cliptripbe.feature.bookmark.domain.entity.Bookmark;
import com.cliptripbe.feature.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUser(User user);

    Optional<Bookmark> findByName(String s);

    List<Bookmark> findAllByUserIsNull();
}
