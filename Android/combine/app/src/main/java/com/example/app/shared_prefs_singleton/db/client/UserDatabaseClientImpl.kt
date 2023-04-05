//package com.example.app.shared_prefs_singleton.db.client
//
//import com.example.app.shared_prefs_singleton.data.UserInfo
//import com.example.app.shared_prefs_singleton.db.UserDatabase
//import com.example.app.shared_prefs_singleton.db.UserInfo
//import com.example.app.shared_prefs_singleton.utils.MyDataBase
//
//class UserDatabaseClientImpl(
//) : UserDatabaseClient {
//    override suspend fun insertUserData(query: UserInfo) {
//        db.insertUserData(query)
//    }
//
//    override suspend fun getAll(): List<UserInfo> {
//        return db.getAll()
//    }
//
//    override suspend fun exists(id: String): Boolean {
//        return db.exists(id)
//    }
//
//    override suspend fun getUserById(id: String): UserInfo? {
//        return db.getUserById(id)
//    }
//
//    override suspend fun updateProfile(id: String, newProfile: Int) {
//        val user = db.getUserById(id)
//        if (user != null) {
//            user.userProfile = newProfile
//            db.updateUser(user)
//        }
//    }
//
//}