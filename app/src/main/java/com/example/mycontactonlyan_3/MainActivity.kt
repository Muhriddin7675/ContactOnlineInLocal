package com.example.mycontactonlyan_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.mycontactonlyan_3.domain.ContactRepository
import com.example.mycontactonlyan_3.navigatiion.AppNavigationHandler
import com.example.mycontactonlyan_3.utils.MyEventBus
import com.example.mycontactonlyan_3.utils.NetworkStatusValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: ContactRepository

    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator

    @Inject
    lateinit var appNavigationHandler:AppNavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = hostFragment.navController

        appNavigationHandler.buffer
            .onEach {it.invoke(navController)}
            .launchIn(lifecycleScope)

        networkStatusValidator.init(
            availableNetworkBlock = {
                repository.syncWithServer().onEach {
                    it.onSuccess {
                        MyEventBus.reloadEvent?.invoke()
                    }
                    it.onFailure {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }.launchIn(lifecycleScope)
            },
            lostConnection = {
                Toast.makeText(
                    this@MainActivity,
                    "Not connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}/*
finishBlock = {
                            this@MainActivity.runOnUiThread {
                                MyEventBus.reloadEvent?.invoke()
                            }
                        }
                    ) {
                        this@MainActivity.runOnUiThread {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                        }
                    }

            lostConnection = { Toast.makeText(this@MainActivity, "Not connection", Toast.LENGTH_SHORT).show() }

                    */