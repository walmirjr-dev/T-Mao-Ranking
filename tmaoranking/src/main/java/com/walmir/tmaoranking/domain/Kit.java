package com.walmir.tmaoranking.domain;

import com.walmir.tmaoranking.domain.enums.KitType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "kits")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Release year is required")
    @Min(value = 1910)
    @Column(name = "kit_year")
    private Integer releaseYear;

    @NotNull(message = "name is required")
    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imgUrl;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Kit type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "kit_type")
    private KitType kitType;
}
