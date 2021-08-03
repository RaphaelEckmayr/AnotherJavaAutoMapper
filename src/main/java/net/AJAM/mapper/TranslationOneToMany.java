package net.AJAM.mapper;

import net.AJAM.mapper.interfaces.PropertyGetter;
import net.AJAM.mapper.interfaces.PropertySetter;

import java.util.List;

public class TranslationOneToMany<S,T,V> extends Translation<S,T,V>
{
    private PropertyGetter<S, V[]> getter;
    private List<MappingOption<T,V>>  options;

    protected TranslationOneToMany()
    {
    }

    protected TranslationOneToMany(PropertyGetter<S, V[]> getter, List<MappingOption<T,V>> options)
    {
        this.getter = getter;
        this.options = options;
    }

    protected PropertyGetter<S, V[]> getGetter()
    {
        return getter;
    }

    protected void setSource(PropertyGetter<S, V[]> getter)
    {
        this.getter = getter;
    }

    protected List<MappingOption<T,V>> getOptions()
    {
        return options;
    }

    protected void setOptions(List<MappingOption<T,V>>  target)
    {
        this.options = options;
    }

    @Override
    protected void translate(S source, T target)
    {
        int index = 0;
        for(V getterResult : getter.get(source))
        {
            MappingOption option = options.get(index);

            if(!option.isIgnore()) {
                PropertySetter setter = option.getSetter();
                setter.set(target, getterResult);
            }

            index++;
        }
    }
}
