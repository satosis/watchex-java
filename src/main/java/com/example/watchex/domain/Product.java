package com.example.watchex.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false)
    private String name;

    private String slug;

    private Integer amount;
    private Integer view;
    private Integer price;
    private Integer sale;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;
    private Integer avatar;
    private Integer favourite;
    private Integer pay;
    private Integer description;
    private Integer content;
    private String keywordseo;
    private Integer review_total;
    private Integer review_star;

    @ManyToMany
    Set<Keyword> keyword;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


}
