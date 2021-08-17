package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.PropertyGetter;
import net.AJAM.Mapper.Interfaces.PropertySetter;

import java.util.List;

public class OneToManyTranslation<S, T, V> extends Translation<S, T, V> {
    private List<MappingOption<T, V>> options;
    private PropertyGetter<S, V[]> propertyGetter;

    protected OneToManyTranslation() {
    }

    protected OneToManyTranslation(PropertyGetter<S, V[]> propertyGetter, List<MappingOption<T, V>> options) {
        this.options = options;
        this.propertyGetter = propertyGetter;
    }

    protected List<MappingOption<T, V>> getOptions() {
        return options;
    }

    protected void setOptions(List<MappingOption<T, V>> options) {
        this.options = options;
    }

    public PropertyGetter<S, V[]> getPropertyGetter() {
        return propertyGetter;
    }

    public void setPropertyGetter(PropertyGetter<S, V[]> propertyGetter) {
        this.propertyGetter = propertyGetter;
    }

    @Override
    protected boolean translate(S source, T target, MappingType mappingType) {
        int index = 0;
        for (V getterResult : propertyGetter.get(source)) {
            MappingOption<T,V> option = options.get(index);

            if (!option.isIgnore()) {
                PropertySetter<T, V> setter = option.getSetter();
                setter.set(target, getterResult);
            }

            index++;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OneToManyTranslation<?, ?, ?> that = (OneToManyTranslation<?, ?, ?>) o;

        if (options != null ? !options.equals(that.options) : that.options != null) return false;
        return propertyGetter != null ? propertyGetter.equals(that.propertyGetter) : that.propertyGetter == null;
    }
}
