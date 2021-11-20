package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parameter.
 */
@Entity
@Table(name = "parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Parameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "lib_2")
    private String lib2;

    @Column(name = "lib_3")
    private String lib3;

    @Column(name = "ref_externe")
    private String refExterne;

    @Column(name = "val_1")
    private String val1;

    @Column(name = "val_2")
    private String val2;

    @Column(name = "val_3")
    private String val3;

    @Column(name = "ordre")
    private Integer ordre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "type", "paraent" }, allowSetters = true)
    private Parameter type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "type", "paraent" }, allowSetters = true)
    private Parameter paraent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Parameter label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Parameter activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getLib2() {
        return this.lib2;
    }

    public Parameter lib2(String lib2) {
        this.setLib2(lib2);
        return this;
    }

    public void setLib2(String lib2) {
        this.lib2 = lib2;
    }

    public String getLib3() {
        return this.lib3;
    }

    public Parameter lib3(String lib3) {
        this.setLib3(lib3);
        return this;
    }

    public void setLib3(String lib3) {
        this.lib3 = lib3;
    }

    public String getRefExterne() {
        return this.refExterne;
    }

    public Parameter refExterne(String refExterne) {
        this.setRefExterne(refExterne);
        return this;
    }

    public void setRefExterne(String refExterne) {
        this.refExterne = refExterne;
    }

    public String getVal1() {
        return this.val1;
    }

    public Parameter val1(String val1) {
        this.setVal1(val1);
        return this;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

    public String getVal2() {
        return this.val2;
    }

    public Parameter val2(String val2) {
        this.setVal2(val2);
        return this;
    }

    public void setVal2(String val2) {
        this.val2 = val2;
    }

    public String getVal3() {
        return this.val3;
    }

    public Parameter val3(String val3) {
        this.setVal3(val3);
        return this;
    }

    public void setVal3(String val3) {
        this.val3 = val3;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public Parameter ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Parameter getType() {
        return this.type;
    }

    public void setType(Parameter parameter) {
        this.type = parameter;
    }

    public Parameter type(Parameter parameter) {
        this.setType(parameter);
        return this;
    }

    public Parameter getParaent() {
        return this.paraent;
    }

    public void setParaent(Parameter parameter) {
        this.paraent = parameter;
    }

    public Parameter paraent(Parameter parameter) {
        this.setParaent(parameter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parameter)) {
            return false;
        }
        return id != null && id.equals(((Parameter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parameter{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", activated='" + getActivated() + "'" +
            ", lib2='" + getLib2() + "'" +
            ", lib3='" + getLib3() + "'" +
            ", refExterne='" + getRefExterne() + "'" +
            ", val1='" + getVal1() + "'" +
            ", val2='" + getVal2() + "'" +
            ", val3='" + getVal3() + "'" +
            ", ordre=" + getOrdre() +
            "}";
    }
}
