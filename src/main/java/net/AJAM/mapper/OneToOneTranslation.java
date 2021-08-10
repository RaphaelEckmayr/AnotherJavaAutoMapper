package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.PropertyGetter;
import net.AJAM.Mapper.Interfaces.PropertySetter;

import java.lang.reflect.Method;

public class OneToOneTranslation<S,T,V> extends Translation<S,T,V>{
    private PropertyGetter<S,V> propertyGetter;
    private MappingOption<T,V> option;

    public OneToOneTranslation(PropertyGetter<S,V> propertyGetter, MappingOption<T,V> option) {
        this.option = option;
        this.propertyGetter = propertyGetter;
    }

    public OneToOneTranslation() {
    }

    public MappingOption<T,V> getOption() {
        return option;
    }

    public void setOption(MappingOption<T,V> option) {
        this.option = option;
    }

    public PropertyGetter<S, V> getPropertyGetter() {
        return propertyGetter;
    }

    public void setPropertyGetter(PropertyGetter<S, V> propertyGetter) {
        this.propertyGetter = propertyGetter;
    }

    @Override
    protected boolean translate(S source, T target, MappingType mappingType) {
        PropertySetter<T,V> setter = option.getSetter();
        if (!option.isIgnore() && setter != null)
            setter.set(target, propertyGetter.get(source));

        return true;
    }
}
