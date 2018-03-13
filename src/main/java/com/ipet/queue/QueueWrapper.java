package com.ipet.queue;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 14:17
 */
public interface QueueWrapper{

    <T> T getInstance(Class<T> clz) throws Exception;
}
