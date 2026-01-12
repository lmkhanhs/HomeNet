package com.lmkhanhs.home_net.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "ImageListings")
public class ListingMediaEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;   

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    ListingEntity listing;

    @Column(nullable = false)
    String url;
    String type; // image or video
    Integer position; 
}
