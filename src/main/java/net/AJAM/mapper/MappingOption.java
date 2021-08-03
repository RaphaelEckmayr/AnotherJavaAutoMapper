package net.AJAM.mapper;

import net.AJAM.mapper.interfaces.PropertySetter;

import java.util.Arrays;

public class MappingOption<T,V> {
    private PropertySetter<T, V> setter;
    private boolean isIgnored;


    public MappingOption<T, V> ignore() {
        isIgnored = true;
        return this;
    }

    public MappingOption<T, V> mapTo(PropertySetter<T, V> setter) {
        isIgnored = false;
        this.setter = setter;

        return this;
    }

    protected PropertySetter<T, ?> getSetter() {
        return setter;
    }

    protected boolean isIgnore() {
        return isIgnored;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingOption<?, ?> that = (MappingOption<?, ?>) o;

        if (isIgnored != that.isIgnored) return false;
        return setter != null ? setter.equals(that.setter) : that.setter == null;
    }
}
