package net.AJAM.mapper.interfaces;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface PropertyGetter<T, V>
{
    V get(T bean);
}
