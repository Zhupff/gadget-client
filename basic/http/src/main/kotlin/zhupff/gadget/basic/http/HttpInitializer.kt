package zhupff.gadget.basic.http

import android.content.Context
import androidx.startup.Initializer

class HttpInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        HttpClient.initClient()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}