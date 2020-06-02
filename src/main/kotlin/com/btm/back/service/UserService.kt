package com.btm.back.service
import com.btm.back.bean.ReqBody
import com.btm.back.dto.User
import com.btm.back.utils.BaseResult

interface UserService {
    fun register(body: ReqBody): BaseResult
    fun deleteUser(id: Long) :BaseResult
    fun login(body: ReqBody):BaseResult
    fun findUserById(id: String):User?
    fun test(): BaseResult
//    fun deleteAllUsers()
//    fun getUserById(id: Long): User?
//    fun getUsers(): MutableIterable<User>?
//    fun getUserByFirstName(firstname: String): List<User>
//    fun updateUser(id: Long, user: User): User
}
