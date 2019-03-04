package com.rao27.nucleusdemo.mvp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import nucleus5.factory.PresenterFactory
import nucleus5.factory.ReflectionPresenterFactory
import nucleus5.presenter.Presenter
import nucleus5.view.PresenterLifecycleDelegate
import nucleus5.view.ViewWithPresenter

abstract class MVPActivity<P: Presenter<*>> : AppCompatActivity(), ViewWithPresenter<P> {
    private val presenterDelegate = PresenterLifecycleDelegate(ReflectionPresenterFactory.fromViewClass<P>(javaClass))
    override fun getPresenter(): P = presenterDelegate.presenter

    override fun getPresenterFactory(): PresenterFactory<P>? = presenterDelegate.presenterFactory

    override fun setPresenterFactory(presenterFactory: PresenterFactory<P>?) {
        presenterDelegate.presenterFactory = presenterFactory
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if(savedInstanceState != null){
            presenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(presenter_state_key))
        }
    }

    override fun onResume() {
        super.onResume()
        presenterDelegate.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenterDelegate.onDropView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenterDelegate.onDestroy(!isChangingConfigurations)
    }
    companion object {
        const val presenter_state_key = "presenter_state"
    }
}