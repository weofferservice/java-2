package org.zcorp.java2.model;

import java.util.Objects;

public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    protected String name;

    public AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        Objects.requireNonNull(name, "name must not be null");
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