package com.coder.alterhomedome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils


class MainActivity : AppCompatActivity() {
    private var mHomeWatcherReceiver: HomeWatcherReceiver? = null
    private var isFront=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver()
    }

    override fun onResume() {
        super.onResume()
        isFront=true
    }

    override fun onPause() {
        super.onPause()
        isFront=false
    }
    private fun registerReceiver() {
        mHomeWatcherReceiver = HomeWatcherReceiver()
        val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        registerReceiver(mHomeWatcherReceiver, filter)
    }

    inner class HomeWatcherReceiver : BroadcastReceiver() {
        private val SYSTEM_DIALOG_REASON_KEY = "reason"
        private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
        override fun onReceive(context: Context, intent: Intent) {
            val intentAction = intent.action
            if (TextUtils.equals(intentAction, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                if (TextUtils.equals(SYSTEM_DIALOG_REASON_HOME_KEY, reason) && isFront) {
                    val intent=Intent(this@MainActivity,AlterService::class.java)
                    startService(intent)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mHomeWatcherReceiver != null) {
            try {
                unregisterReceiver(mHomeWatcherReceiver)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
