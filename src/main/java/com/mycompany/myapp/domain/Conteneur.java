package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conteneur.
 */
@Entity
@Table(name = "conteneur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conteneur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "statut")
    private Integer statut;

    @Column(name = "date_entree")
    private Instant dateEntree;

    @Column(name = "date_sortie")
    private ZonedDateTime dateSortie;

    @Column(name = "zone")
    private Integer zone;

    @Column(name = "ligne")
    private Integer ligne;

    @Column(name = "colonne")
    private Integer colonne;

    @Column(name = "commentaire")
    private String commentaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conteneur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatut() {
        return this.statut;
    }

    public Conteneur statut(Integer statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public Instant getDateEntree() {
        return this.dateEntree;
    }

    public Conteneur dateEntree(Instant dateEntree) {
        this.setDateEntree(dateEntree);
        return this;
    }

    public void setDateEntree(Instant dateEntree) {
        this.dateEntree = dateEntree;
    }

    public ZonedDateTime getDateSortie() {
        return this.dateSortie;
    }

    public Conteneur dateSortie(ZonedDateTime dateSortie) {
        this.setDateSortie(dateSortie);
        return this;
    }

    public void setDateSortie(ZonedDateTime dateSortie) {
        this.dateSortie = dateSortie;
    }

    public Integer getZone() {
        return this.zone;
    }

    public Conteneur zone(Integer zone) {
        this.setZone(zone);
        return this;
    }

    public void setZone(Integer zone) {
        this.zone = zone;
    }

    public Integer getLigne() {
        return this.ligne;
    }

    public Conteneur ligne(Integer ligne) {
        this.setLigne(ligne);
        return this;
    }

    public void setLigne(Integer ligne) {
        this.ligne = ligne;
    }

    public Integer getColonne() {
        return this.colonne;
    }

    public Conteneur colonne(Integer colonne) {
        this.setColonne(colonne);
        return this;
    }

    public void setColonne(Integer colonne) {
        this.colonne = colonne;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Conteneur commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conteneur)) {
            return false;
        }
        return id != null && id.equals(((Conteneur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conteneur{" +
            "id=" + getId() +
            ", statut=" + getStatut() +
            ", dateEntree='" + getDateEntree() + "'" +
            ", dateSortie='" + getDateSortie() + "'" +
            ", zone=" + getZone() +
            ", ligne=" + getLigne() +
            ", colonne=" + getColonne() +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
