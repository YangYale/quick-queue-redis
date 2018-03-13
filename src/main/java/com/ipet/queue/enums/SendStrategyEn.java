package com.ipet.queue.enums;

/**
 * 消息发送策略
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 14:05
 */
public enum SendStrategyEn {
    SYNC("同步发送(不管是否成功消费)"),
    ASYNC("异步发送(不管是否成功消费)");

    private String desc;
    SendStrategyEn(String _desc){
        this.desc = _desc;
    }
}
