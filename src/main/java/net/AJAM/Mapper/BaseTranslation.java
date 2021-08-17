package net.AJAM.Mapper;

import net.AJAM.Mapper.Exceptions.TypeParameterException;
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
                if(!handleSpecialCases(source, target, mappingType))
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

    private boolean handleSpecialCases(S source, T target, MappingType mappingType) {
        Class<?> readPropertyType = getter.getPropertyType();

        if(mappingType == MappingType.STRICT)
            return false;

        try {
            if (List.class.isAssignableFrom(readPropertyType)) {
                return handleList(source, target, mappingType);
            }
            else if(Set.class.isAssignableFrom(readPropertyType))
            {
                return handleSet(source, target, mappingType);
            }
            else if(Map.class.isAssignableFrom(readPropertyType))
            {
                return handleMap(source, target, mappingType);
            }
        }
        catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | TypeParameterException e)
        {
            return false;
        }

        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean handleList(S source, T target, MappingType mappingType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, TypeParameterException {
        ConversionFunction conversion = getConversionByObject(source, target, mappingType);
        if(conversion == null) return false;

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

        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean handleSet(S source, T target, MappingType mappingType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, TypeParameterException {
        ConversionFunction conversion = getConversionByObject(source, target, mappingType);
        if(conversion == null) return false;

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

        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean handleMap(S source, T target, MappingType mappingType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, TypeParameterException {
        Type[] sourceTypes = getTypeParameters(source, getter.getName());
        Type[] targetTypes = getTypeParameters(target, setter.getName());

        if (sourceTypes == null || targetTypes == null)
            return false;
        if (Arrays.equals(sourceTypes, targetTypes))
            return false;

        Class<?> writePropertyType = setter.getPropertyType();

        Map result;
        if(writePropertyType.isInterface())
            result = new HashMap();
        else
            result = (Map) writePropertyType.getDeclaredConstructor().newInstance();

        Map<?,?> getterRes = (Map<?,?>) getter.getReadMethod().invoke(source);

        if(sourceTypes[0] != targetTypes[0])
        {
            ConversionFunction conversion = getConversionByTypeParameter(sourceTypes[0], targetTypes[0], mappingType);
            if(conversion == null) return false;
            for (Object key : getterRes.keySet()) {
                result.put(conversion.convert(key), getterRes.get(key));
            }
        } else if(sourceTypes[1] != targetTypes[1]) {
            ConversionFunction conversion = getConversionByTypeParameter(sourceTypes[1], targetTypes[1], mappingType);
            if(conversion == null) return false;
            for (Object key : getterRes.keySet()) {
                result.put(key, conversion.convert(getterRes.get(key)));
            }
        } else {
            ConversionFunction conversion = getConversionByTypeParameter(sourceTypes[0], targetTypes[0], mappingType);
            ConversionFunction conversion1 = getConversionByTypeParameter(sourceTypes[1], targetTypes[1], mappingType);
            if(conversion == null || conversion1 == null) return false;
            for (Object key : getterRes.keySet()) {
                result.put(conversion.convert(key), conversion1.convert(getterRes.get(key)));
            }
        }

        setter.getWriteMethod().invoke(target, result);

        return true;
    }

    @SuppressWarnings("rawtypes")
    private ConversionFunction getConversionByObject(S source, T target, MappingType mappingType) throws TypeParameterException {
        Type[] sourceTypes = getTypeParameters(source, getter.getName());
        Type[] targetTypes = getTypeParameters(target, setter.getName());

        if (sourceTypes == null || targetTypes == null)
            throw new TypeParameterException("SourceTypes or targetTypes is null");
        if (Arrays.equals(sourceTypes, targetTypes))
            throw new TypeParameterException("SourceTypes != targetTypes");

        return getConversionByTypeParameter(sourceTypes[0], targetTypes[0], mappingType);
    }

    @SuppressWarnings("rawtypes")
    private ConversionFunction getConversionByTypeParameter(Type sourceType, Type targetType, MappingType mappingType)
    {
        Class<?> sourceClass = (Class<?>) sourceType;
        Class<?> targetClass = (Class<?>) targetType;

        return ConversionManager.getConversionFunction(sourceClass, targetClass, mappingType);
    }

    private <T1> Type[] getTypeParameters(T1 target, String fieldName) throws TypeParameterException {
        try {
            ParameterizedType getterParameterizedType = (ParameterizedType) target.getClass().getDeclaredField(fieldName).getGenericType();
            return getterParameterizedType.getActualTypeArguments();
        }catch (NoSuchFieldException | ClassCastException e)
        {
            throw new TypeParameterException("Failed to get ParameterizedType from Field");
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
