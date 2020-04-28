package com.newscrawler.entity;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class News implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NonNull
    private String url;

    private String title;

    @NotNull
    @NonNull
    private Date createDate;

    private String newsDate;

    @NotNull
    @NonNull
    private String source;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(columnDefinition="TEXT",nullable=true)
    private String content;

    @OneToMany(mappedBy = "news" , cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Keyword> keywords;

}
