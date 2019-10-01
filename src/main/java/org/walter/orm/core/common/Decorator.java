package org.walter.orm.core.common;

public interface Decorator<T, P> {

    T decorate(T object, P params);
}
