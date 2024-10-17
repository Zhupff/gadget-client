package zhupff.gadget.basic.qrcode

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Message
import zhupff.gadgets.logger.logW
import java.lang.IllegalArgumentException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

class ScanHandler @JvmOverloads constructor(
    callback: Callback? = null
) : Handler(
    Looper.getMainLooper(),
    callback,
) {

    companion object {
        private val SCAN: Int = 1
        private val DECODE: Int = 2
        private val QUIT: Int = 0
    }

    private val state = AtomicInteger(SCAN)

    private val decodeThread = DecodeThread().also { it.start() }

    fun getDecodeHandler(): Handler = decodeThread.getHandler()

    fun isScanning(): Boolean = state.get() == SCAN

    fun scan() {
        if (state.get() != QUIT) {
            state.set(SCAN)
        }
    }

    fun decode(bitmap: Bitmap) {
        if (state.get() != QUIT && state.get() != DECODE) {
            state.set(DECODE)
            getDecodeHandler().obtainMessage(R.id.qrcode_decode, bitmap.width, bitmap.height, bitmap).sendToTarget()
        }
    }
    fun decode(width: Int, height: Int, data: ByteArray) {
        // image width&height are not correct, use bitmap instead.
//        if (state.get() != QUIT && state.get() != DECODE) {
//            state.set(DECODE)
//            getDecodeHandler().obtainMessage(R.id.qrcode_decode, width, height, data).sendToTarget()
//        }
        decode(BitmapFactory.decodeByteArray(data, 0, data.size))
    }

    fun quit() {
        if (state.getAndSet(QUIT) != QUIT) {
            decodeThread.getHandler().obtainMessage(R.id.qrcode_quit).sendToTarget()
            try {
                decodeThread.join()
            } catch (e: Exception) {
                logW("quit() exception").logW(e)
            }
        }
    }


    private inner class DecodeThread : Thread() {
        private lateinit var handler: DecodeHandler
        private val initLatch = CountDownLatch(1)

        fun getHandler(): Handler {
            try {
                initLatch.await()
            } catch (e: Exception) {
                logW("getHandler() exception").logW(e)
            }
            return handler
        }

        override fun run() {
            Looper.prepare()
            handler = DecodeHandler(Looper.myLooper()!!)
            initLatch.countDown()
            Looper.loop()
        }
    }

    private inner class DecodeHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                R.id.qrcode_quit -> {
                    looper.quit()
                }
                R.id.qrcode_decode -> {
                    try {
                        val width = msg.arg1
                        val height = msg.arg2
                        val data = msg.obj
                        val results = when (data) {
                            is Bitmap -> QRCodeUtil.decodeMultiple(data)
                            is ByteArray -> QRCodeUtil.decodeMultiple(data, width, height)
                            else -> throw IllegalArgumentException()
                        }
                        if (results.isNullOrEmpty()) {
                            this@ScanHandler.obtainMessage(R.id.qrcode_decode_fail).sendToTarget()
                        } else {
                            this@ScanHandler.obtainMessage(R.id.qrcode_decode_success, arrayOf(data, results)).sendToTarget()
                        }
                    } catch (e: Exception) {
                        logW("decode exception").logW(e)
                        this@ScanHandler.obtainMessage(R.id.qrcode_decode_fail).sendToTarget()
                    }
                }
            }
        }
    }

}