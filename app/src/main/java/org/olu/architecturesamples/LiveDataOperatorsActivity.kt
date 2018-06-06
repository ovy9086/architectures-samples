package org.olu.architecturesamples

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel


class ViewModelWithTransformations(val userRepository: UserRepository) : ViewModel() {

    private val userLiveData: LiveData<User> = MutableLiveData<User>()

    fun getDisplayUser() = Transformations.map(userLiveData, { "${it.firstName} ${it.lastName}" })

    fun getUserGrades(): LiveData<List<Grade>> =
            Transformations.switchMap(userLiveData, { userRepository.getUserGrades(it) })
}


interface UserRepository {
    fun getUserGrades(user: User): LiveData<List<Grade>>
}

data class User(val firstName: String, val lastName: String)
data class Grade(val name: String, val value: Float)