package com.dojagy.todaysave.data.source.datastore.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.dojagy.todaysave.common.extension.DEFAULT
import com.dojagy.todaysave.data.domain.repository.UserDatastoreRepository
import com.dojagy.todaysave.data.model.user.Gender
import com.dojagy.todaysave.data.model.user.SnsType
import com.dojagy.todaysave.data.model.user.UserGrade
import com.dojagy.todaysave.data.model.user.UserModel
import com.dojagy.todaysave.data.source.datastore.preferences.UserPreferences
import com.dojagy.todaysave.data.source.datastore.qulifier.UserDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class UserDatastoreRepositoryImpl @Inject constructor(
    @UserDatastore
    private val datastore: DataStore<Preferences>
) : UserDatastoreRepository {

    override suspend fun setUser(
        data: UserModel
    ) {
        datastore.edit { user ->
            user[UserPreferences.USER_ID] = data.id
            user[UserPreferences.USER_EMAIL] = data.email
            user[UserPreferences.USER_SNS_KEY] = data.snsKey
            user[UserPreferences.USER_SNS_TYPE] = data.sns.strValue
            user[UserPreferences.USER_NICKNAME] = data.nickname
            user[UserPreferences.USER_BIRTHDAY] = data.birthday.toString()
            user[UserPreferences.USER_GENDER] = data.gender?.strValue ?: String.DEFAULT
            user[UserPreferences.USER_GRADE] = data.grade.strValue
        }
    }

    override suspend fun clearUser() {
        datastore.edit { it.clear() }
    }

    override val user: Flow<UserModel> = datastore.data
        .map { preferences ->
            UserModel(
                id = preferences[UserPreferences.USER_ID] ?: Long.DEFAULT,
                nickname = preferences[UserPreferences.USER_NICKNAME] ?: String.DEFAULT,
                email = preferences[UserPreferences.USER_EMAIL] ?: String.DEFAULT,
                sns = try {
                    SnsType.valueOf(preferences[UserPreferences.USER_SNS_TYPE] ?: String.DEFAULT)
                }catch (e: Exception) {
                    e.printStackTrace()
                    SnsType.DEFAULT
                },
                snsKey = preferences[UserPreferences.USER_SNS_KEY] ?: String.DEFAULT,
                gender = try {
                    Gender.valueOf(preferences[UserPreferences.USER_GENDER] ?: String.DEFAULT)
                }catch (e: Exception) {
                    e.printStackTrace()
                    null
                },
                birthday = try {
                    LocalDate.parse(preferences[UserPreferences.USER_BIRTHDAY] ?: String.DEFAULT)
                }catch (e: Exception) {
                    e.printStackTrace()
                    null
                },
                grade = try {
                    UserGrade.valueOf(preferences[UserPreferences.USER_GENDER] ?: String.DEFAULT)
                }catch (e: Exception) {
                    e.printStackTrace()
                    UserGrade.DEFAULT
                }
            )
        }
}