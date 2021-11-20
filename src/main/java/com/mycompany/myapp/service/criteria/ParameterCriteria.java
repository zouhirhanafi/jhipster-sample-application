package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Parameter} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ParameterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parameters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParameterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter label;

    private BooleanFilter activated;

    private StringFilter lib2;

    private StringFilter lib3;

    private StringFilter refExterne;

    private StringFilter val1;

    private StringFilter val2;

    private StringFilter val3;

    private IntegerFilter ordre;

    private LongFilter typeId;

    private LongFilter paraentId;

    private Boolean distinct;

    public ParameterCriteria() {}

    public ParameterCriteria(ParameterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.lib2 = other.lib2 == null ? null : other.lib2.copy();
        this.lib3 = other.lib3 == null ? null : other.lib3.copy();
        this.refExterne = other.refExterne == null ? null : other.refExterne.copy();
        this.val1 = other.val1 == null ? null : other.val1.copy();
        this.val2 = other.val2 == null ? null : other.val2.copy();
        this.val3 = other.val3 == null ? null : other.val3.copy();
        this.ordre = other.ordre == null ? null : other.ordre.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.paraentId = other.paraentId == null ? null : other.paraentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ParameterCriteria copy() {
        return new ParameterCriteria(this);
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

    public StringFilter getLabel() {
        return label;
    }

    public StringFilter label() {
        if (label == null) {
            label = new StringFilter();
        }
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public StringFilter getLib2() {
        return lib2;
    }

    public StringFilter lib2() {
        if (lib2 == null) {
            lib2 = new StringFilter();
        }
        return lib2;
    }

    public void setLib2(StringFilter lib2) {
        this.lib2 = lib2;
    }

    public StringFilter getLib3() {
        return lib3;
    }

    public StringFilter lib3() {
        if (lib3 == null) {
            lib3 = new StringFilter();
        }
        return lib3;
    }

    public void setLib3(StringFilter lib3) {
        this.lib3 = lib3;
    }

    public StringFilter getRefExterne() {
        return refExterne;
    }

    public StringFilter refExterne() {
        if (refExterne == null) {
            refExterne = new StringFilter();
        }
        return refExterne;
    }

    public void setRefExterne(StringFilter refExterne) {
        this.refExterne = refExterne;
    }

    public StringFilter getVal1() {
        return val1;
    }

    public StringFilter val1() {
        if (val1 == null) {
            val1 = new StringFilter();
        }
        return val1;
    }

    public void setVal1(StringFilter val1) {
        this.val1 = val1;
    }

    public StringFilter getVal2() {
        return val2;
    }

    public StringFilter val2() {
        if (val2 == null) {
            val2 = new StringFilter();
        }
        return val2;
    }

    public void setVal2(StringFilter val2) {
        this.val2 = val2;
    }

    public StringFilter getVal3() {
        return val3;
    }

    public StringFilter val3() {
        if (val3 == null) {
            val3 = new StringFilter();
        }
        return val3;
    }

    public void setVal3(StringFilter val3) {
        this.val3 = val3;
    }

    public IntegerFilter getOrdre() {
        return ordre;
    }

    public IntegerFilter ordre() {
        if (ordre == null) {
            ordre = new IntegerFilter();
        }
        return ordre;
    }

    public void setOrdre(IntegerFilter ordre) {
        this.ordre = ordre;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getParaentId() {
        return paraentId;
    }

    public LongFilter paraentId() {
        if (paraentId == null) {
            paraentId = new LongFilter();
        }
        return paraentId;
    }

    public void setParaentId(LongFilter paraentId) {
        this.paraentId = paraentId;
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
        final ParameterCriteria that = (ParameterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(lib2, that.lib2) &&
            Objects.equals(lib3, that.lib3) &&
            Objects.equals(refExterne, that.refExterne) &&
            Objects.equals(val1, that.val1) &&
            Objects.equals(val2, that.val2) &&
            Objects.equals(val3, that.val3) &&
            Objects.equals(ordre, that.ordre) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(paraentId, that.paraentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, activated, lib2, lib3, refExterne, val1, val2, val3, ordre, typeId, paraentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParameterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (label != null ? "label=" + label + ", " : "") +
            (activated != null ? "activated=" + activated + ", " : "") +
            (lib2 != null ? "lib2=" + lib2 + ", " : "") +
            (lib3 != null ? "lib3=" + lib3 + ", " : "") +
            (refExterne != null ? "refExterne=" + refExterne + ", " : "") +
            (val1 != null ? "val1=" + val1 + ", " : "") +
            (val2 != null ? "val2=" + val2 + ", " : "") +
            (val3 != null ? "val3=" + val3 + ", " : "") +
            (ordre != null ? "ordre=" + ordre + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (paraentId != null ? "paraentId=" + paraentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
