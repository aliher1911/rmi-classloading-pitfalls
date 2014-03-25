package org.aliher.classloader.objects;

import java.io.Serializable;

public class TopLevel implements Serializable {

    public Nested nested;

    public TopLevel() {
    }

    public TopLevel(Nested nested) {
        this.nested = nested;
    }

    public Nested getNested() {
        return nested==null?new NestedDefault():nested;
    }

    public Value getValue() {
        return null;
    }
}
