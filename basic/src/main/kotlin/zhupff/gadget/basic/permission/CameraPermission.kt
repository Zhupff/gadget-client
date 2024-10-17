package zhupff.gadget.basic.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class CameraPermission(
    val host: Any,
) {

    val granted: Boolean
        get() {
            val context: Context = when (host) {
                is Context -> host
                is Fragment -> host.requireActivity()
                else -> throw IllegalArgumentException("host should be a Context or Fragment!")
            }
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }

    fun request(callback: (Boolean) -> Unit): Boolean {
        if (!granted) {
            when (host) {
                is ComponentActivity -> {
                    host.registerForActivityResult(
                        ActivityResultContracts.RequestPermission(),
                        callback
                    ).launch(Manifest.permission.CAMERA)
                }
                is Fragment -> {
                    host.registerForActivityResult(
                        ActivityResultContracts.RequestPermission(),
                        callback
                    ).launch(Manifest.permission.CAMERA)
                }
                else -> throw IllegalStateException("host should be ComponentActivity or Fragment!")
            }
            return false
        }
        return true
    }
}