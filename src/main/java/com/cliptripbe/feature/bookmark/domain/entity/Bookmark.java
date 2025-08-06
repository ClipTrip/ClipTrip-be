package com.cliptripbe.feature.bookmark.domain.entity;

import static com.cliptripbe.global.response.type.ErrorType.EXISTS_PLACE;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.global.response.exception.CustomException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private Set<BookmarkPlace> bookmarkPlaces = new LinkedHashSet<>();

    @Column
    private boolean isDefault;

    @Builder
    public Bookmark(
        String name,
        String description,
        User user,
        boolean isDefault
    ) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.isDefault = isDefault;
    }

    public static Bookmark createByUser(
        String name, String description, User user
    ) {
        return Bookmark.builder()
            .name(name)
            .description(description)
            .user(user)
            .isDefault(false)
            .build();
    }

    public static Bookmark createDefault(
        String name, String description
    ) {
        return Bookmark.builder()
            .name(name)
            .description(description)
            .isDefault(true)
            .build();
    }


    public void addBookmarkPlace(BookmarkPlace newBookmarkPlace) {
        Long newPlaceId = newBookmarkPlace.getPlace().getId();
        boolean exists = bookmarkPlaces.stream()
            .map(bp -> bp.getPlace().getId())
            .anyMatch(newPlaceId::equals);
        if (exists) {
            throw new CustomException(EXISTS_PLACE);
        }

        bookmarkPlaces.add(newBookmarkPlace);
    }

    public List<Place> getPlaces() {
        return bookmarkPlaces.stream()
            .map(BookmarkPlace::getPlace)
            .collect(Collectors.toList());
    }

    public void cleanBookmarkPlace() {
        this.bookmarkPlaces.clear();
    }

    public void modifyInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
