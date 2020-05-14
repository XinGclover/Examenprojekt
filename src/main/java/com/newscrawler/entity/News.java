package com.newscrawler.entity;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import lombok.*;


/**
 * News entity
 */
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

    @OneToMany(mappedBy = "news" , cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Keyword> keywords;

    @OneToMany(mappedBy = "news" , cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<KeywordNLP> keywordsNLP;

}
