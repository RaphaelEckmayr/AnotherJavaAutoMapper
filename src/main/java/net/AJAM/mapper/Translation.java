package net.AJAM.mapper;

import net.AJAM.mapper.interfaces.PropertyGetter;

public class Translation <S,T,V>
{
    private PropertyGetter<S, V> source;
    private MappingOption<T,V> target;

    protected Translation()
    {
    }

    protected Translation(PropertyGetter<S, V> source, MappingOption<T,V> target)
    {
        this.source = source;
        this.target = target;
    }

    protected PropertyGetter<S, V> getSource()
    {
        return source;
    }

    protected void setSource(PropertyGetter<S, V> source)
    {
        this.source = source;
    }

    protected MappingOption<T, V> getTarget()
    {
        return target;
    }

    protected void setTarget(MappingOption<T,V> target)
    {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Translation<?, ?, ?> that = (Translation<?, ?, ?>) o;

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        return target != null ? target.equals(that.target) : that.target == null;
    }
}
