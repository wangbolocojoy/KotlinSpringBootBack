package com.btm.back.enmu

enum class ContactType {
    /*
          NORMAL : 正常
          NO_DATA : 数据为空
          NO_INTERNET : 网络未连接
          ERROR : 错误
          OTHER : 其他
      */
    PHONE,
    WECHAT, EMAIL, QQ
}

enum class StateType {
    OPEN, CLOSED
}
