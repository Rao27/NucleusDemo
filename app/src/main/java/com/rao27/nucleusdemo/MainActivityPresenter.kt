package com.rao27.nucleusdemo

import android.os.Bundle
import android.os.Handler
import com.rao27.nucleusdemo.mvp.MVPPresenter
import io.reactivex.functions.Consumer

class MainActivityPresenter<V: MainActivityView> : MVPPresenter<V>() {
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        startHandler(0)
    }

    private fun startHandler(counter: Int) {
        if(counter == 3) return
        Handler().postDelayed({
            executeOnView(Consumer {
                it.setDemoText(counter.toString())
                startHandler(counter+1)
            })
        }, 2000)
    }
}

interface MainActivityView {
    fun setDemoText(text: String)
}