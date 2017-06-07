package nstv.rxbinding

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import nstv.rxbinding.listenersFragment.RxBindingFragment
import nstv.rxbinding.listenersFragment.NormalWayFragment
import nstv.rxbinding.model.SuperShape

class MainActivity : AppCompatActivity() {

    val USE_RX_BINDING = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Add listeners Fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, getFragment())
                    .commit()
        }
    }

    private fun getFragment(): Fragment {
        if (USE_RX_BINDING) {
            return RxBindingFragment.getInstance(this)
        }
        return NormalWayFragment.getInstance(this)
    }
}
