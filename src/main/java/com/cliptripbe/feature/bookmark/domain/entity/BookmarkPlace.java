package com.cliptripbe.feature.bookmark.domain.entity;

import com.cliptripbe.feature.place.domain.entity.Place;
import com.cliptripbe.global.entity.BaseTimeEntity;
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
public class BookmarkPlace extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder
    public BookmarkPlace(Bookmark bookmark, Place place) {
        this.bookmark = bookmark;
        this.place = place;
    }
}
