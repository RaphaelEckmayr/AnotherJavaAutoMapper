package net.AJAM.Mapper;

import net.AJAM.Mapper.Interfaces.ConversionFunction;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

class BaseTranslation<S, T, V> extends Translation<S, T, V> {
    private PropertyDescriptor getter;
    private PropertyDescriptor setter;

    public BaseTranslation(PropertyDescriptor getter, PropertyDescriptor setter) {
        this.getter = getter;
        this.setter = setter;
    }

    protected PropertyDescriptor getGetter() {
        return getter;
    }

    protected void setGetter(PropertyDescriptor getter) {
        this.getter = getter;
    }

    protected PropertyDescriptor getSetter() {
        return setter;
    }

    protected void setSetter(PropertyDescriptor setter) {
        this.setter = setter;
    }

    @Override
    @SuppressWarnings("unchecked")
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
        Class<?> writePropertyType = setter.getPropertyType();

        if(mappingType == MappingType.STRICT)
            return false;

        try {
            if (List.class.isAssignableFrom(readPropertyType)) {
                ConversionFunction conversion = getConversionFunctionForSpecialCases(source, target, mappingType);

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
            else if(Set.class.isAssignableFrom(readPropertyType))
            {
                ConversionFunction conversion = getConversionFunctionForSpecialCases(source, target, mappingType);

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
            else if(Map.class.isAssignableFrom(readPropertyType))
            {
                ConversionFunction conversion = getConversionFunctionForSpecialCases(source, target, mappingType);

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

                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }


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


    private static <T1> List<T1> createListOfType(Class<T1> type){
        return new ArrayList<T1>();
    }
}
