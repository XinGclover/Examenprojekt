package com.newscrawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class KeywordNLP implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;

        @ManyToOne
        @JsonIgnore
        private News news;

        @NotNull
        @NonNull
        private String lemma;

        private int tf;
        private double tfIdf;


    }
