/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import net.sf.cglib.proxy.*;
import net.sf.cglib.proxy.InvocationHandler;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.*;
import java.util.List;

class Tracker {


    static <T> T track(final T o, final TrackingTree trackingTree) {
        return createProxy(o, handler(o, trackingTree), trackingTree);
    }

    private static <T> InvocationHandler handler(final T o, final TrackingTree trackingTree) {
        return (proxy, method, args) -> {
            method.setAccessible(true);
            final Object returnValue = method.invoke(o, args);
            final TrackingTree nextTree = trackingTree.track(method);
            if (returnValue == null ) {
                return null;
            }
            final Class<?> returnClass = returnValue.getClass();
            if (Modifier.isFinal(returnClass.getModifiers())){
                if (returnClass.isArray()){
                    final Object[] returnArray = (Object[]) returnValue;
                    final Object[] array = (Object[]) Array.newInstance(returnClass.getComponentType(), returnArray.length);
                    final TrackingTree arrayTree = trackingTree.array(method);
                    for (int i = 0; i < returnArray.length; i++) {
                        final Object track = track(returnArray[i], arrayTree);
                        array[i] = track;
                    }
                    return array;
                }
                if (!(returnClass == String.class || isPrimitiveWrapper(returnClass))){
                    nextTree.track(null).err(new IllegalArgumentException("Can't track across final class " + returnClass.getSimpleName() + "."));
                }
                return returnValue;
            }
            return track(returnValue, nextTree);
        };
    }

    private static boolean isPrimitiveWrapper(final Class<?> type) {
        return ((type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class));
    }

    private static <T> T createProxy(final T obj, final InvocationHandler handler, final TrackingTree trackingTree) {
        try {
            final Class<?> proxyClass = enhancer(obj.getClass()).createClass();
            final Factory proxy = (Factory) new ObjenesisStd().newInstance(proxyClass);
            proxy.setCallbacks(new Callback[]{handler, NoOp.INSTANCE});
            //noinspection unchecked
            return (T) proxy;
        } catch (final Throwable e){
            trackingTree.track(null).err(e);
            return obj;
        }
    }

    private static Enhancer enhancer(final Class<?> targetClass) {
        final Enhancer enhancer = new Enhancer() {
            protected void filterConstructors(final Class sc, final List constructors) {
            }
        };
        enhancer.setUseFactory(true);
        enhancer.setSuperclass(targetClass);
        enhancer.setCallbackType(net.sf.cglib.proxy.InvocationHandler.class);
        return enhancer;
    }


}