package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Conteneur} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ConteneurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conteneurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConteneurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter statut;

    private InstantFilter dateEntree;

    private ZonedDateTimeFilter dateSortie;

    private IntegerFilter zone;

    private IntegerFilter ligne;

    private IntegerFilter colonne;

    private StringFilter commentaire;

    private Boolean distinct;

    public ConteneurCriteria() {}

    public ConteneurCriteria(ConteneurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.statut = other.statut == null ? null : other.statut.copy();
        this.dateEntree = other.dateEntree == null ? null : other.dateEntree.copy();
        this.dateSortie = other.dateSortie == null ? null : other.dateSortie.copy();
        this.zone = other.zone == null ? null : other.zone.copy();
        this.ligne = other.ligne == null ? null : other.ligne.copy();
        this.colonne = other.colonne == null ? null : other.colonne.copy();
        this.commentaire = other.commentaire == null ? null : other.commentaire.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConteneurCriteria copy() {
        return new ConteneurCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getStatut() {
        return statut;
    }

    public IntegerFilter statut() {
        if (statut == null) {
            statut = new IntegerFilter();
        }
        return statut;
    }

    public void setStatut(IntegerFilter statut) {
        this.statut = statut;
    }

    public InstantFilter getDateEntree() {
        return dateEntree;
    }

    public InstantFilter dateEntree() {
        if (dateEntree == null) {
            dateEntree = new InstantFilter();
        }
        return dateEntree;
    }

    public void setDateEntree(InstantFilter dateEntree) {
        this.dateEntree = dateEntree;
    }

    public ZonedDateTimeFilter getDateSortie() {
        return dateSortie;
    }

    public ZonedDateTimeFilter dateSortie() {
        if (dateSortie == null) {
            dateSortie = new ZonedDateTimeFilter();
        }
        return dateSortie;
    }

    public void setDateSortie(ZonedDateTimeFilter dateSortie) {
        this.dateSortie = dateSortie;
    }

    public IntegerFilter getZone() {
        return zone;
    }

    public IntegerFilter zone() {
        if (zone == null) {
            zone = new IntegerFilter();
        }
        return zone;
    }

    public void setZone(IntegerFilter zone) {
        this.zone = zone;
    }

    public IntegerFilter getLigne() {
        return ligne;
    }

    public IntegerFilter ligne() {
        if (ligne == null) {
            ligne = new IntegerFilter();
        }
        return ligne;
    }

    public void setLigne(IntegerFilter ligne) {
        this.ligne = ligne;
    }

    public IntegerFilter getColonne() {
        return colonne;
    }

    public IntegerFilter colonne() {
        if (colonne == null) {
            colonne = new IntegerFilter();
        }
        return colonne;
    }

    public void setColonne(IntegerFilter colonne) {
        this.colonne = colonne;
    }

    public StringFilter getCommentaire() {
        return commentaire;
    }

    public StringFilter commentaire() {
        if (commentaire == null) {
            commentaire = new StringFilter();
        }
        return commentaire;
    }

    public void setCommentaire(StringFilter commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConteneurCriteria that = (ConteneurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateEntree, that.dateEntree) &&
            Objects.equals(dateSortie, that.dateSortie) &&
            Objects.equals(zone, that.zone) &&
            Objects.equals(ligne, that.ligne) &&
            Objects.equals(colonne, that.colonne) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statut, dateEntree, dateSortie, zone, ligne, colonne, commentaire, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConteneurCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (statut != null ? "statut=" + statut + ", " : "") +
            (dateEntree != null ? "dateEntree=" + dateEntree + ", " : "") +
            (dateSortie != null ? "dateSortie=" + dateSortie + ", " : "") +
            (zone != null ? "zone=" + zone + ", " : "") +
            (ligne != null ? "ligne=" + ligne + ", " : "") +
            (colonne != null ? "colonne=" + colonne + ", " : "") +
            (commentaire != null ? "commentaire=" + commentaire + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
