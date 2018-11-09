package org.zcorp.java2.to;

import org.zcorp.java2.HasId;

public abstract class BaseTo implements HasId {
    protected Integer id;

    protected BaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
//        throw new UnsupportedOperationException();
        // Нельзя кидать UnsupportedOperationException, т.к. когда есть сеттер,
        // он всегда вызывается при Spring Binding-е. Поэтому:
        this.id = id;
    }
}
