package org.zcorp.java2.model;

import org.hibernate.validator.constraints.SafeHtml;
import org.zcorp.java2.ValidationGroup;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR")
    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml(groups = {ValidationGroup.Web.class})
    protected String name;

    protected AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("Entity %s (%s, '%s')", getClass().getName(), id, name);
    }

}