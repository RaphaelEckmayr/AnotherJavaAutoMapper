package net.AJAM.mapper.interfaces;

public interface PropertySetter<T, V>
{
    void set(T bean, V value);
}
