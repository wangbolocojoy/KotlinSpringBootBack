package com.btm.back.enum

/**
 * 联系方式类型枚举
 * @author Trae AI
 * @date 2023-06-01
 */
enum class ContactType {
    /**
     * 手机号
     */
    PHONE,
    
    /**
     * 微信
     */
    WECHAT, 
    
    /**
     * 电子邮件
     */
    EMAIL, 
    
    /**
     * QQ
     */
    QQ
}

/**
 * 状态类型枚举
 * @author Trae AI
 * @date 2023-06-01
 */
enum class StateType {
    /**
     * 开启状态
     */
    OPEN, 
    
    /**
     * 关闭状态
     */
    CLOSED
}

/**
 * 上传类型枚举
 * @author Trae AI
 * @date 2023-06-01
 */
enum class UploadType{
    /**
     * 视频
     */
    VIDEO,
    
    /**
     * 图片（拼写修正：PICTURE）
     */
    PICTURE
}