package com.newscrawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Keyword implements Comparable<Keyword>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonIgnore
    private News news;

    @NotNull
    @NonNull
    private String stem;

    private int frequency;

    @ElementCollection(targetClass=String.class)
    private Set<String> terms = new HashSet<>();

    public void add(String term){
        this.terms.add(term);
        this.frequency++;
    }

    @Override
    public int compareTo(Keyword o) {
        return Integer.valueOf(o.frequency).compareTo(this.frequency);
    }
    /**
     * Get stem's hashcode
     *
     * @return int, which contains stem's hashcode
     */
    @Override
    public int hashCode() {
        return this.getStem().hashCode();
    }

    /**
     * Check if two stems are equal
     *
     * @param o
     * @return boolean, true if two stems are equal
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Keyword)) return false;

        Keyword that = (Keyword) o;

        return this.getStem().equals(that.getStem());
    }



}
