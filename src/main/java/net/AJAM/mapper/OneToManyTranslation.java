package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.PropertyGetter;
import net.AJAM.Mapper.Interfaces.PropertySetter;

import java.lang.reflect.Method;
import java.util.List;

public class OneToManyTranslation<S,T,V> extends Translation<S,T,V>
{
    private List<MappingOption<T,V>> options;
    private PropertyGetter<S,V[]> propertyGetter;

    protected OneToManyTranslation()
    {
    }

    protected OneToManyTranslation(PropertyGetter<S,V[]> propertyGetter, List<MappingOption<T,V>> options)
    {
        this.options = options;
        this.propertyGetter = propertyGetter;
    }

    protected List<MappingOption<T,V>> getOptions()
    {
        return options;
    }

    protected void setOptions(List<MappingOption<T,V>>  target)
    {
        this.options = options;
    }

    public PropertyGetter<S, V[]> getPropertyGetter() {
        return propertyGetter;
    }

    public void setPropertyGetter(PropertyGetter<S, V[]> propertyGetter) {
        this.propertyGetter = propertyGetter;
    }

    @Override
    protected boolean translate(S source, T target, MappingType mappingType)
    {
        int index = 0;
        for(V getterResult : propertyGetter.get(source))
        {
            MappingOption option = options.get(index);

            if(!option.isIgnore()) {
                PropertySetter setter = option.getSetter();
                setter.set(target, getterResult);
            }

            index++;
        }

        return true;
    }
}
