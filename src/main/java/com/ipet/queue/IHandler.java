package com.ipet.queue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 14:21
 */
public interface IHandler extends InvocationHandler {
    Object run(Method method,Object[] args) throws Exception;
}
