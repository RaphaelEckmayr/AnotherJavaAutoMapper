package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.ConversionFunction;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

class BaseTranslation<S, T, V> extends Translation<S, T, V> {
    private final PropertyDescriptor getter;
    private final PropertyDescriptor setter;

    public BaseTranslation(PropertyDescriptor getter, PropertyDescriptor setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected boolean translate(S source, T target, MappingType mappingType) {
        Class<?> readPropertyType = getter.getPropertyType();
        Class<?> writePropertyType = setter.getPropertyType();

        try {
            if (readPropertyType == writePropertyType) {
                if(!treatSpecialCases(source, target, mappingType))
                    setter.getWriteMethod().invoke(target, getter.getReadMethod().invoke(source));

                return true;
            }

            if (mappingType != MappingType.STRICT) {
                ConversionFunction conversion = ConversionManager.getConversionFunction(getter.getPropertyType(), setter.getPropertyType(), mappingType);
                if (conversion != null)
                    setter.getWriteMethod().invoke(target, conversion.convert(getter.getReadMethod().invoke(source)));
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean treatSpecialCases(S source, T target, MappingType mappingType) {
        Class<?> readPropertyType = getter.getPropertyType();

        if(mappingType == MappingType.STRICT)
            return false;

        try {
            if (List.class.isAssignableFrom(readPropertyType)) {
                handleList(source, target, mappingType);

                return true;
            }
            else if(Set.class.isAssignableFrom(readPropertyType))
            {
                handleSet(source, target, mappingType);

                return true;
            }
            else if(Map.class.isAssignableFrom(readPropertyType))
            {
                handleMap(source, target, mappingType);

                return true;
            }
        }
        catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e)
        {

        }

        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleList(S source, T target, MappingType mappingType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        ConversionFunction conversion = getConversionFunctionForSpecialCases(source, target, mappingType);

        Class<?> writePropertyType = setter.getPropertyType();

        List result;
        if(writePropertyType.isInterface())
            result = new ArrayList();
        else
            result = (List) writePropertyType.getDeclaredConstructor().newInstance();

        List<?> getterRes = (List<?>) getter.getReadMethod().invoke(source);

        for (Object getterResElement : getterRes) {
            result.add(conversion.convert(getterResElement));
        }

        setter.getWriteMethod().invoke(target, result);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleSet(S source, T target, MappingType mappingType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        ConversionFunction conversion = getConversionFunctionForSpecialCases(source, target, mappingType);

        Class<?> writePropertyType = setter.getPropertyType();

        Set result;
        if(writePropertyType.isInterface())
            result = new HashSet();
        else
            result = (Set) writePropertyType.getDeclaredConstructor().newInstance();

        Set<?> getterRes = (Set<?>) getter.getReadMethod().invoke(source);

        for (Object getterResElement : getterRes) {
            result.add(conversion.convert(getterResElement));
        }

        setter.getWriteMethod().invoke(target, result);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleMap(S source, T target, MappingType mappingType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        ConversionFunction conversion = getConversionFunctionForSpecialCases(source, target, mappingType);

        Class<?> writePropertyType = setter.getPropertyType();

        Map result;
        if(writePropertyType.isInterface())
            result = new HashMap();
        else
            result = (Map) writePropertyType.getDeclaredConstructor().newInstance();

        Map<?,?> getterRes = (Map<?,?>) getter.getReadMethod().invoke(source);

        for (Object key : getterRes.keySet()) {
            result.put(key, conversion.convert(getterRes.get(key)));
        }

        setter.getWriteMethod().invoke(target, result);
    }

    @SuppressWarnings("rawtypes")
    private ConversionFunction getConversionFunctionForSpecialCases(S source, T target, MappingType mappingType) throws IllegalArgumentException
    {
        Type[] sourceTypes = getTypeParameters(source, getter.getName());
        Type[] targetTypes = getTypeParameters(target, setter.getName());

        if (sourceTypes == null || targetTypes == null)
            throw new IllegalArgumentException("sourceTypes or targetTypes is null");
        if (Arrays.equals(sourceTypes, targetTypes))
            throw new IllegalArgumentException("sourceTypes != targetTypes");

        Class<?> sourceClass = (Class<?>) sourceTypes[0];
        Class<?> targetClass = (Class<?>) targetTypes[0];

        return ConversionManager.getConversionFunction(sourceClass, targetClass, mappingType);
    }


    private <T1> Type[] getTypeParameters(T1 target, String fieldName) {
        try {
            ParameterizedType getterParameterizedType = (ParameterizedType) target.getClass().getDeclaredField(fieldName).getGenericType();
            return getterParameterizedType.getActualTypeArguments();
        }catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseTranslation<?, ?, ?> that = (BaseTranslation<?, ?, ?>) o;

        if (!Objects.equals(getter, that.getter)) return false;
        return Objects.equals(setter, that.setter);
    }
}
