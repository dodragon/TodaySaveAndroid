package com.dojagy.todaysave.ui.main

import com.dojagy.todaysave.common.android.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(

) : BaseViewModel() {



    init {
        //TODO: 로그인 관련 처리
        /**
         *  1. SharedPreference or DataStore 내 회원 정보 유무 확인 (email, snsKey)
         *  2. 없으면 로그인 화면, 있으면 로그인 Api
         */
    }

    //로그인 Api
}