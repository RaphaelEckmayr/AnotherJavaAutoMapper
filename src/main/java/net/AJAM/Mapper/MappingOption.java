package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.PropertySetter;

public class MappingOption<T,V>
{
    private PropertySetter<T, V> setter;
    private boolean isIgnored;

    public MappingOption(PropertySetter<T, V> setter, boolean isIgnored) {
        this.setter = setter;
        this.isIgnored = isIgnored;
    }

    public MappingOption() {
    }

    public MappingOption<T, V> ignore() {
        isIgnored = true;
        return this;
    }

    public MappingOption<T, V> mapTo(PropertySetter<T, V> setter) {
        isIgnored = false;
        this.setter = setter;

        return this;
    }

    protected PropertySetter<T,V> getSetter() {
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
