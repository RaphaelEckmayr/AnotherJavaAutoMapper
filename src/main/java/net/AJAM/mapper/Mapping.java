package net.AJAM.mapper;

import de.cronn.reflection.util.PropertyUtils;
import de.cronn.reflection.util.TypedPropertyGetter;
import net.AJAM.mapper.interfaces.PropertyGetter;
import net.AJAM.mapper.interfaces.OptionsBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Mapping <S, T>
{
    private final Class<S> source;
    private final Class<T> target;

    private final List<Method> skippedProperties = new ArrayList<>();
    private final List<Translation<S,T, ?>> translations = new ArrayList<>();

    private MappingType mappingType = null;


    public Mapping(Class<S> source, Class<T> target) {
        this.source = source;
        this.target = target;
    }


    public <V> Mapping<S,T> forMember(PropertyGetter<S,V> source, OptionsBuilder<MappingOption<T,V>> option)
    {
        MappingOption<T,V> mappingOption = option.build(new MappingOption<>());

        if(mappingOption.isIgnore())
            ignore(source);

        translations.add(new Translation(source, mappingOption));
        return this;
    }

    public Mapping<S,T> ignore(PropertyGetter<S,?> getter)
    {
        TypedPropertyGetter<S,?> typedGetter = (TypedPropertyGetter<S, Object>) getter::get;

        Method getterMethod = PropertyUtils.findMethodByGetter(source, typedGetter);
        skippedProperties.add(getterMethod);

        return this;
    }

    public Mapping<S,T> mappingType(MappingType mappingType)
    {
        this.mappingType = mappingType;
        return this;
    }



    protected Class<S> getSource() {
        return source;
    }

    protected Class<T> getTarget() {
        return target;
    }

    protected List<Translation<S,T, ?>> getTranslations() {
        return translations;
    }

    protected MappingType getMappingType()
    {
        return mappingType;
    }

    protected List<Method> getSkippedProperties()
    {
        return skippedProperties;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mapping<?, ?> mapping = (Mapping<?, ?>) o;

        if (!source.equals(mapping.source)) return false;
        if (!target.equals(mapping.target)) return false;
        if (!skippedProperties.equals(mapping.skippedProperties))
            return false;
        if (!translations.equals(mapping.translations))
            return false;
        return mappingType == mapping.mappingType;
    }
}
