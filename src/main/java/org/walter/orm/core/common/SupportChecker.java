package org.walter.orm.core.common;

import java.lang.reflect.Method;

public interface SupportChecker {

    Boolean support(Class<?> clz, Method method);
}
