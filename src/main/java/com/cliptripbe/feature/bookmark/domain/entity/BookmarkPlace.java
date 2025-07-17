package com.cliptripbe.feature.bookmark.domain.entity;

import com.cliptripbe.feature.place.domain.entity.Place;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BookmarkPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookmark_id")
    Bookmark bookmark;

    @ManyToOne
    @JoinColumn(name = "place_id")
    Place place;

    @Builder
    public BookmarkPlace(Bookmark bookmark, Place place) {
        this.bookmark = bookmark;
        this.place = place;
    }
}
