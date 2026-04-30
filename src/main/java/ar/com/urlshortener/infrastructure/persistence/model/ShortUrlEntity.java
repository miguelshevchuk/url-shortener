package ar.com.urlshortener.infrastructure.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "short_url")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortUrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String shortCode;
    private String originalUrl;
    private long clicks;
    private Instant createAt;
    private Instant expiresAt;

}
