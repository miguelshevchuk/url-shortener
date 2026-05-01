package ar.com.urlshortener.infrastructure.persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "url_click")
public class UrlClickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String shortCode;
    private Instant timestamp;
    private String userAgent;
    private String ipAddress;
    private String referer;

}
