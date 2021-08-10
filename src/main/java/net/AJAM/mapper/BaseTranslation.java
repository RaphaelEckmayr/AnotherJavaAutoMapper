package net.AJAM.Mapper;

import java.lang.reflect.Method;
import java.util.function.Function;

class BaseTranslation<S,T,V> extends Translation<S,T,V>
{
    private Method getter;
    private Method setter;

    public BaseTranslation(Method getter, Method setter) {
        this.getter = getter;
        this.setter = setter;
    }

    protected Method getGetter() {
        return getter;
    }

    protected void setGetter(Method getter) {
        this.getter = getter;
    }

    protected Method getSetter() {
        return setter;
    }

    protected void setSetter(Method setter) {
        this.setter = setter;
    }

    @Override
    protected boolean translate(S source, T target, MappingType mappingType) {
        Class<?> readPropertyType = getter.getReturnType();
        Class<?> writePropertyType = setter.getParameterTypes()[0];

        try {
            if(readPropertyType == writePropertyType)
            {
                setter.invoke(target, getter.invoke(source));
            }
            else if(mappingType != MappingType.STRICT) {
                Function conversion = ConversionManager.getConversionFunction(readPropertyType, writePropertyType, mappingType);
                if (conversion != null)
                    setter.invoke(target, conversion.apply(getter.invoke(source)));
            }
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
}
