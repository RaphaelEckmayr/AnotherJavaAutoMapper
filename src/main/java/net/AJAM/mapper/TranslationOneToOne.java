package net.AJAM.mapper;

import net.AJAM.mapper.interfaces.PropertyGetter;
import net.AJAM.mapper.interfaces.PropertySetter;

public class TranslationOneToOne<S,T,V> extends Translation<S,T,V>
{
    private PropertyGetter<S, V> getter;
    private MappingOption<T,V> option;

    protected TranslationOneToOne()
    {
    }

    protected TranslationOneToOne(PropertyGetter<S, V> getter, MappingOption<T,V> option)
    {
        this.getter = getter;
        this.option = option;
    }

    protected PropertyGetter<S, V> getGetter()
    {
        return getter;
    }

    protected void setGetter(PropertyGetter<S, V> getter)
    {
        this.getter = getter;
    }

    protected MappingOption<T, V> getOption()
    {
        return option;
    }

    protected void setOption(MappingOption<T,V> option
    )
    {
        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslationOneToOne<?, ?, ?> that = (TranslationOneToOne<?, ?, ?>) o;

        if (getter != null ? !getter.equals(that.getter) : that.getter != null) return false;
        return option != null ? option.equals(that.option
        ) : that.option == null;
    }


    @Override
    protected void translate(S source, T target) {
        PropertySetter setter = option.getSetter();
        if (setter != null && !option.isIgnore())
            setter.set(target, getter.get(source));
    }
}
