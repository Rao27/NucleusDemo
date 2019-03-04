package com.rao27.nucleusdemo.mvp

import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import nucleus5.presenter.Factory
import nucleus5.presenter.RxPresenter

open class MVPPresenter<V>: RxPresenter<V>() {
    protected var arguments: Bundle? = null
    protected var savedState: Bundle? = null
    private val EXECUTE_ID_START = 27000
    private val RESTARTABLE_ID_START = 0
    private var executeID = EXECUTE_ID_START
    private var restartableID = RESTARTABLE_ID_START

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        this.savedState = savedState
    }

    fun create() {
        onCreate(savedState, savedState != null)
        savedState = null
    }

    private fun onCreate(savedState: Bundle?, isRestoring: Boolean) {
        if (savedState != null) {
            executeID = savedState.getInt(EXECUTE_ID, EXECUTE_ID_START)
            restartableID = savedState.getInt(RESTARTABLE_ID, RESTARTABLE_ID_START)
        }
    }

    override fun onSave(state: Bundle) {
        super.onSave(state)
        state.putInt(EXECUTE_ID, executeID)
        state.putInt(RESTARTABLE_ID, restartableID)
    }

    /**
     * Executes an action on the view using the [restartableFirst]
     */
    protected fun executeOnView(action: Consumer<V>) {
        val action2: BiConsumer<V, Object> = BiConsumer { view, value ->
            action.accept(view)
        }
        val id = getNextInternalID()
        restartableFirst(id,  Factory { Observable.just(Object()) } , action2)
        start(id)
    }

    protected fun <T> executeOnView(action: BiConsumer<V, T>, value: T) {
        val id = getNextInternalID()
        restartableFirst(id, Factory { Observable.just(value) }, action)
        start(id)
    }

    fun injectArguments(arguments: Bundle) {
        this.arguments = arguments
    }

    fun injectSavedInstanceState(savedState: Bundle) {
        this.savedState = savedState
    }

    private fun getNextInternalID(): Int {
        return executeID++
    }

    protected fun getNextID(): Int {
        return restartableID++
    }

    companion object {
        private val EXECUTE_ID = "execute_id"
        private val RESTARTABLE_ID = "restartable_id"
    }
}