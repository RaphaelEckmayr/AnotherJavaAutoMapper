package net.AJAM.Mapper;

import de.cronn.reflection.util.PropertyUtils;
import de.cronn.reflection.util.TypedPropertyGetter;
import net.AJAM.Mapper.Interfaces.OptionsBuilder;
import net.AJAM.Mapper.Interfaces.PropertyGetter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mapping <S,T>
{
    private final Class<S> source;
    private final Class<T> target;

    private final List<Translation<S,T, ?>> translations = new ArrayList<>();
    private final List<Method> ignoredProperties = new ArrayList<>();

    private MappingType mappingType = null;


    public Mapping(Class<S> source, Class<T> target) {
        this.source = source;
        this.target = target;
    }

    public <V> Mapping<S,T> forMember(PropertyGetter<S,V> getter, OptionsBuilder<MappingOption<T,V>> option)
    {
        MappingOption<T,V> mappingOption = option.build(new MappingOption<>());

        translations.add(new OneToOneTranslation<>(getter, mappingOption));
        return this;
    }

    public <V> Mapping<S,T> forMembers(PropertyGetter<S,V[]> getter, OptionsBuilder<MappingOption<T,V>>... options)
    {
        List<MappingOption<T,V>> mappingOptions = Arrays.stream(options).map(x -> x.build(new MappingOption<>())).collect(Collectors.toList());

        translations.add(new OneToManyTranslation<>(getter, mappingOptions));
        return this;
    }

    public Mapping<S,T> ignore(PropertyGetter<S,?> getter) {
        TypedPropertyGetter<S, ?> typedGetter = (TypedPropertyGetter<S, Object>) getter::get;

        Method getterMethod = PropertyUtils.findMethodByGetter(source, typedGetter);
        ignoredProperties.add(getterMethod);

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

    public List<Method> getIgnoredProperties() {
        return ignoredProperties;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mapping<?, ?> mapping = (Mapping<?, ?>) o;

        if (!source.equals(mapping.source)) return false;
        if (!target.equals(mapping.target)) return false;
        if (!ignoredProperties.equals(mapping.ignoredProperties))
            return false;
//        if (!translations.equals(mapping.translations))
//            return false;
        return mappingType == mapping.mappingType;
    }
}
