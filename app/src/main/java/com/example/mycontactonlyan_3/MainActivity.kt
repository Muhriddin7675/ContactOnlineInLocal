package com.example.mycontactonlyan_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mycontactonlyan_3.domain.ContactRepository
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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