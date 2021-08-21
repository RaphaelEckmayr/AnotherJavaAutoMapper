package net.AJAM.Mapper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

public class MapperEngine {
    private static final MappingType DEFAULT_MAPPING_TYPE = MappingType.MEDIUM;
    private static final int CACHE_SIZE = 200;

    private static final Map<Class<?>, List<PropertyDescriptor>> cache = Collections.synchronizedMap(new LinkedHashMap<Class<?>, List<PropertyDescriptor>>() {
        @Override
        protected boolean removeEldestEntry(final Map.Entry eldest) {
            return size() > CACHE_SIZE;
        }
    });

    protected static <S,T> T internalMap(T target, S source, MappingType mappingType, List<Mapping<?,?>> mappings)
    {
        Mapping<S, T> mapping = searchForMapping(source, target, mappings);

        MappingType usedMappingType = DEFAULT_MAPPING_TYPE;
        List<Method> ignoredProperties = mapping.getIgnoredProperties();

        if (mappingType != null)
            usedMappingType = mappingType;
        else if (mapping.getMappingType() != null)
            usedMappingType = mapping.getMappingType();

        List<PropertyDescriptor> readProps;
        List<PropertyDescriptor> writeProps;
        try {
            readProps = handleProperties(source);
            writeProps = handleProperties(target);
        } catch (IntrospectionException e) {
            return null;
        }

        List<Translation<S, T, ?>> translations = createBaseTranslations(readProps, writeProps, ignoredProperties);

        translations.addAll(mapping.getTranslations());

        for (Translation<S, T, ?> trans : translations) {
            trans.translate(source, target, usedMappingType);
        }

        return target;
    }

    private static <T> List<PropertyDescriptor> handleProperties(T obj) throws IntrospectionException {
        List<PropertyDescriptor> res = cache.get(obj.getClass());
        if (res == null) {
            res = getProperties(obj);

            cache.put(obj.getClass(), res);
        }

        return res;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <S, T> Mapping<S, T> searchForMapping(S source, T target, List<Mapping<?, ?>> mappings) {
        List<Mapping<S, T>> correctMappings = new ArrayList<>();

        for (Mapping mapping : mappings) {
            if (mapping.getSource() == source.getClass() && mapping.getTarget() == target.getClass()) {
                correctMappings.add(mapping);
            }
        }

        Mapping<S,T> result = new Mapping(source.getClass(), target.getClass());
        for (Mapping<S, T> mapping : correctMappings) {
            result.getTranslations().addAll(mapping.getTranslations());
            result.getIgnoredProperties().addAll(mapping.getIgnoredProperties());

            if (result.getMappingType() == null || result.getMappingType().getValue() < mapping.getMappingType().getValue()) {
                result.mappingType(mapping.getMappingType());
            }
        }

        return result;
    }

    private static <T> List<PropertyDescriptor> getProperties(T obj) throws IntrospectionException {
        List<PropertyDescriptor> props = new ArrayList<>();

        for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
            if (pd.getReadMethod() != null && pd.getWriteMethod() != null) {
                props.add(pd);
            }
        }

        return props;
    }

    private static <S, T> List<Translation<S, T, ?>> createBaseTranslations(List<PropertyDescriptor> readProps, List<PropertyDescriptor> writeProps, List<Method> ignoredProperties) {
        List<Translation<S, T, ?>> translations = new ArrayList<>();

        for (PropertyDescriptor read : readProps) {
            if (ignoredProperties.contains(read.getReadMethod()))
                continue;

            for (PropertyDescriptor write : writeProps) {
                if (read.getName().equals(write.getName()))
                    translations.add(new BaseTranslation<>(read, write));
            }
        }

        return translations;
    }
}
