package net.AJAM.mapper;

import net.AJAM.mapper.interfaces.PropertySetter;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;

public class MapperEngine
{
    private static final int CACHE_SIZE = 200;
    private static final MappingType DEFAULT_MAPPING_TYPE = MappingType.MEDIUM;

    private static MapperEngine instance;

    private List<Conversion<?, ?>> conversions = new ArrayList<>();
    private final Map<Class<?>, List<PropertyDescriptor>> cache = Collections.synchronizedMap(new LinkedHashMap<Class<?>, List<PropertyDescriptor>>()
    {
        @Override
        protected boolean removeEldestEntry(final Map.Entry eldest)
        {
            return size() > CACHE_SIZE;
        }
    });

    public static MapperEngine getInstance()
    {
        if(instance == null)
            instance = new MapperEngine();

        return instance;
    }

    public MapperEngine()
    {
        init();
    }


    protected  <T, S> T internalMap(T target, S source, List<Mapping<?,?>> mappings, MappingType mappingType)
    {
        Mapping<S, T> mapping = searchForMapping(target, source, mappings);

        MappingType usedMappingType = getUsedMappingType(mappingType, mapping);

        List<PropertyDescriptor> propsSource;
        List<PropertyDescriptor> propsTarget;
        try
        {
            propsSource = handleProperties(source);
            propsTarget = handleProperties(target);
        }
        catch (IntrospectionException e)
        {
            return null;
        }

        for (PropertyDescriptor read : propsSource)
        {
            if((mapping != null && mapping.getSkippedProperties().contains(read.getReadMethod())))
                continue;

            for (PropertyDescriptor write : propsTarget)
            {
                if (write.getName().equals(read.getName()))
                    doMap(write, read, source, target, usedMappingType);
            }
        }

        if(mapping != null)
        {
            handleTranslations(mapping, source, target);
        }

        return target;
    }

    private <S,T> void handleTranslations(Mapping<S,T> mapping, S source, T target)
    {
        for (Translation<S,T,?> translation : mapping.getTranslations())
        {
            translation.translate(source, target);
        }
    }

    private <T> List<PropertyDescriptor> handleProperties(T obj) throws IntrospectionException
    {
        List<PropertyDescriptor> res = cache.get(obj.getClass());
        if (res == null)
        {
            res = getProperties(obj);

            cache.put(obj.getClass(), res);
        }

        return res;
    }

    private Function<?, ?> getConversion(Class<?> from, Class<?> to, MappingType mappingType)
    {
        for (Conversion<?, ?> conv : conversions)
        {
            if (from == conv.getFrom() && to == conv.getTo() &&
                    (mappingType == conv.getMappingType() || mappingType == MappingType.LOOSE))
            {
                return conv.getConversionFunction();
            }
        }

        return null;
    }


    private <S,T> Mapping<S, T> searchForMapping(T target, S source, List<Mapping<?,?>> mappings)
    {
        for (Mapping mapping : mappings)
        {
            if (mapping.getSource() == source.getClass() && mapping.getTarget() == target.getClass())
            {
                return mapping;
            }
        }
        return null;
    }


    private <T> List<PropertyDescriptor> getProperties(T obj) throws IntrospectionException
    {
        List<PropertyDescriptor> props = new ArrayList<>();

        for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors())
        {
            if (pd.getReadMethod() != null && pd.getWriteMethod() != null)
            {
                props.add(pd);
            }
        }

        return props;
    }

    private MappingType getUsedMappingType(MappingType callMappingType, Mapping<?,?> mapping)
    {
        if (callMappingType != null)
            return callMappingType;
        else if (mapping != null && mapping.getMappingType() != null)
            return mapping.getMappingType();

        return DEFAULT_MAPPING_TYPE;
    }

    private <S,T> void doMap(PropertyDescriptor write, PropertyDescriptor read, S source, T target, MappingType usedMappingType)
    {
        try
        {
            Class<?> writePropertyType = write.getPropertyType();
            Class<?> readPropertyType = read.getPropertyType();

            if (writePropertyType == readPropertyType)
                write.getWriteMethod().invoke(target, read.getReadMethod().invoke(source));
            else if (usedMappingType != MappingType.STRICT)
            {
                try
                {
                    Function conversion = getConversion(readPropertyType, writePropertyType, usedMappingType);
                    if (conversion != null)
                        write.getWriteMethod().invoke(target, conversion.apply(read.getReadMethod().invoke(source)));
                    else if (usedMappingType == MappingType.LOOSE)
                    {
                        write.getWriteMethod().invoke(target,
                                write.getPropertyType().cast(read.getReadMethod().invoke(source)));
                    }
                }
                catch (ClassCastException e)
                {
                    System.out.println("AHHHHHHHHHH");
                }
            }
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {

        }
    }

    private void init()
    {
        conversions = Conversion.initLooseConversions();
    }
}
