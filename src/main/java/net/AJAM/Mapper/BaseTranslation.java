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
                if (!handleSpecialCases(source, target, mappingType))
                    setter.getWriteMethod().invoke(target, getter.getReadMethod().invoke(source));

                return true;
            }

            if (mappingType != MappingType.STRICT) {
                Conversion conversion =
                        ConversionManager.getConversion(getter.getPropertyType(), setter.getPropertyType(), mappingType);
                if (conversion != null)
                    setter.getWriteMethod().invoke(target, conversion.getConversionFunction().convert(getter.getReadMethod().invoke(source)));
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean handleSpecialCases(S source, T target, MappingType mappingType) {
        Class<?> readPropertyType = getter.getPropertyType();

        if (mappingType == MappingType.STRICT)
            return false;

        try {
            if (List.class.isAssignableFrom(readPropertyType)) {
                return handleList(source, target, mappingType);
            } else if (Set.class.isAssignableFrom(readPropertyType)) {
                return handleSet(source, target, mappingType);
            } else if (Map.class.isAssignableFrom(readPropertyType)) {
                return handleMap(source, target, mappingType);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | TypeParameterException e) {
            return false;
        }

        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean handleList(S source, T target, MappingType mappingType)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, TypeParameterException {
        ConversionFunction conversion = getConversionByObject(mappingType);
        if (conversion == null) return false;

        Class<?> writePropertyType = setter.getPropertyType();

        List result;
        if (writePropertyType.isInterface())
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
    private boolean handleSet(S source, T target, MappingType mappingType)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, TypeParameterException {
        ConversionFunction conversion = getConversionByObject(mappingType);
        if (conversion == null) return false;

        Class<?> writePropertyType = setter.getPropertyType();

        Set result;
        if (writePropertyType.isInterface())
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
    private boolean handleMap(S source, T target, MappingType mappingType)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, TypeParameterException {
        Type[] sourceTypes = ((ParameterizedType) getter.getReadMethod().getGenericReturnType()).getActualTypeArguments();
        Type[] targetTypes = ((ParameterizedType) setter.getReadMethod().getGenericReturnType()).getActualTypeArguments();

        if (sourceTypes == null || targetTypes == null)
            return false;
        if (Arrays.equals(sourceTypes, targetTypes))
            return false;

        Class<?> writePropertyType = setter.getPropertyType();

        Map result;
        if (writePropertyType.isInterface())
            result = new HashMap();
        else
            result = (Map) writePropertyType.getDeclaredConstructor().newInstance();

        Map<?, ?> getterRes = (Map<?, ?>) getter.getReadMethod().invoke(source);

        if(sourceTypes[0] != targetTypes[0] && sourceTypes[1] != targetTypes[1])
        {
            ConversionFunction conversion = getConversionFunctionByTypeParameter(sourceTypes[0], targetTypes[0], mappingType);
            ConversionFunction conversion1 = getConversionFunctionByTypeParameter(sourceTypes[1], targetTypes[1], mappingType);
            if (conversion == null || conversion1 == null) return false;
            for (Object key : getterRes.keySet()) {
                result.put(conversion.convert(key), conversion1.convert(getterRes.get(key)));
            }
        } else if (sourceTypes[0] != targetTypes[0]) {
            ConversionFunction conversion = getConversionFunctionByTypeParameter(sourceTypes[0], targetTypes[0], mappingType);
            if (conversion == null) return false;
            for (Object key : getterRes.keySet()) {
                result.put(conversion.convert(key), getterRes.get(key));
            }
        } else if (sourceTypes[1] != targetTypes[1]) {
            ConversionFunction conversion = getConversionFunctionByTypeParameter(sourceTypes[1], targetTypes[1], mappingType);
            if (conversion == null) return false;
            for (Object key : getterRes.keySet()) {
                result.put(key, conversion.convert(getterRes.get(key)));
            }
        }

        setter.getWriteMethod().invoke(target, result);

        return true;
    }

    @SuppressWarnings("rawtypes")
    private ConversionFunction getConversionByObject(MappingType mappingType) throws TypeParameterException {
        Type[] sourceTypes = ((ParameterizedType) getter.getReadMethod().getGenericReturnType()).getActualTypeArguments();
        Type[] targetTypes = ((ParameterizedType) setter.getReadMethod().getGenericReturnType()).getActualTypeArguments();

        if (sourceTypes == null || targetTypes == null)
            throw new TypeParameterException("SourceTypes or targetTypes is null");
        if (Arrays.equals(sourceTypes, targetTypes))
            throw new TypeParameterException("SourceTypes != targetTypes");

        return getConversionFunctionByTypeParameter(sourceTypes[0], targetTypes[0], mappingType);
    }

    @SuppressWarnings("rawtypes")
    private ConversionFunction getConversionFunctionByTypeParameter(Type sourceType, Type targetType, MappingType mappingType) {
        Class<?> sourceClass = (Class<?>) sourceType;
        Class<?> targetClass = (Class<?>) targetType;

        Conversion conv = ConversionManager.getConversion(sourceClass, targetClass, mappingType);
        if(conv != null)
            return conv.getConversionFunction();

        return null;
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
