package com.mycompany.myapp.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A MyData.
 */
@Entity
@Table(name = "my_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MyData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "first_date", nullable = false)
    private LocalDate firstDate;

    @NotNull
    @Column(name = "laste_date", nullable = false)
    private LocalDate lasteDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public MyData firstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
        return this;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public LocalDate getLasteDate() {
        return lasteDate;
    }

    public MyData lasteDate(LocalDate lasteDate) {
        this.lasteDate = lasteDate;
        return this;
    }

    public void setLasteDate(LocalDate lasteDate) {
        this.lasteDate = lasteDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyData)) {
            return false;
        }
        return id != null && id.equals(((MyData) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MyData{" +
            "id=" + getId() +
            ", firstDate='" + getFirstDate() + "'" +
            ", lasteDate='" + getLasteDate() + "'" +
            "}";
    }
}
