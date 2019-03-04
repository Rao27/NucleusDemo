package com.rao27.nucleusdemo

import android.os.Bundle
import com.rao27.nucleusdemo.mvp.MVPActivity
import kotlinx.android.synthetic.main.activity_main.*
import nucleus5.factory.RequiresPresenter

@RequiresPresenter(MainActivityPresenter::class)
class MainActivity : MVPActivity<MainActivityPresenter<MainActivityView>>(), MainActivityView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun setDemoText(text: String) {
        demoText.text = text
    }
}
