package com.cliptripbe.feature.bookmark.domain.entity;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.feature.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
    Long id;

    String name;

    String description;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookmarkPlace> bookmarkPlaces = new ArrayList<>();

    @Builder
    public Bookmark(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public void addBookmarkPlace(BookmarkPlace bookmarkPlace) {
        this.bookmarkPlaces.add(bookmarkPlace);
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
