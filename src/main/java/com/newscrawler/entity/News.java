package com.newscrawler.entity;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    @NotNull
    @NonNull
    private String title;

    private String image;

    @NotNull
    @NonNull
    private Date createDate;

    private Date newsDate;

    @NotNull
    @NonNull
    private String source;

    @NotNull
    @NonNull
    private String content;

}
