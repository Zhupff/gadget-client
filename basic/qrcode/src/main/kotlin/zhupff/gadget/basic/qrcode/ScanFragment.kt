package zhupff.gadget.basic.qrcode

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.Bundle
import android.os.Message
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import com.google.zxing.Result
import zhupff.gadget.basic.fragment.GadgetFragment
import zhupff.gadget.basic.permission.CameraPermission
import zhupff.gadget.basic.widget.FrameLayout
import zhupff.gadget.basic.widget.TextureView
import zhupff.gadget.basic.widget.frameLayoutParams
import zhupff.gadgets.logger.logD
import zhupff.gadgets.logger.logI
import zhupff.gadgets.logger.logW
import zhupff.gadgets.toast.toastS
import zhupff.gadgets.widget.dsl.MATCH_PARENT
import java.util.concurrent.atomic.AtomicBoolean

class ScanFragment : GadgetFragment() {

    private lateinit var previewer: TextureView
    private val previewerListener = SurfaceTextureListener { availabe ->
        if (availabe) {
            startPreviewing()
        } else {
            stopPreviewing()
        }
    }

    private val previewing = AtomicBoolean(false)

    private lateinit var previewSize: Size

    private val cameraPermission = CameraPermission(this)

    private val cameraStateCallback = CameraStateCallback()

    private var imageReader: ImageReader? = null

    private val handler = ScanHandler { msg ->
        when (msg.what) {
            R.id.qrcode_decode_success,
            R.id.qrcode_decode_fail -> {
                onDecoded(msg)
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (cameraPermission.request { granted ->
                if (!granted) {
                    quit()
                } else {
                    startPreviewing()
                }
            }
        ) {
            startPreviewing()
        }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FrameLayout(
            context = requireContext(),
            size = MATCH_PARENT to MATCH_PARENT,
        ) {
            previewer = TextureView(
                size = MATCH_PARENT to MATCH_PARENT
            ) {
                frameLayoutParams {
                    gravity = Gravity.CENTER
                }
                surfaceTextureListener = previewerListener
            }
        }

    override fun onPause() {
        super.onPause()
        quit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopPreviewing()
        handler.quit()
    }


    @SuppressLint("MissingPermission")
    private fun startPreviewing() {
        ID_TAG.logI("startPreviewing")
        if (!cameraPermission.granted) {
            ID_TAG.logI("Cancel previewing because camera permission is not granted yet.")
            return
        }
        if (!previewerListener.get()) {
            ID_TAG.logI("Cancel previewing because texture is not available.")
            return
        }

        if (previewing.compareAndSet(false, true)) {
            val cameraManager = requireContext().getSystemService(CameraManager::class.java)
            if (cameraManager == null) {
                onError("找不到摄像头管理器")
                return
            }
            var cameraCharacteristics: CameraCharacteristics? = null
            var cameraId: String = ""
            val cameraIdList = cameraManager.cameraIdList
            if (cameraIdList.isNotEmpty()) {
                for (i in cameraIdList.lastIndex downTo 0) {
                    cameraId = cameraIdList[i]
                    cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIdList[i])
                    if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                        break
                    }
                }
            }
            if (cameraCharacteristics == null) {
                onError("找不到摄像头")
                return
            }
            previewSize = decidePreviewSize(cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!)
            adjustPreviewerSize(previewSize)
            imageReader?.close()
            imageReader = ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.JPEG, 2)
            imageReader?.setOnImageAvailableListener(object : OnImageAvailableListener {
                override fun onImageAvailable(reader: ImageReader?) {
                    val image = reader?.acquireNextImage() ?: return
                    if (handler.isScanning()) {
                        val buffer = image.planes[0].buffer
                        val data = ByteArray(buffer.remaining())
                        buffer.get(data)
                        handler.decode(image.width, image.height, data)
                    }
                    image.close()
                }
            }, handler.getDecodeHandler())
            cameraManager.openCamera(cameraId, cameraStateCallback, handler)
        }
    }

    private fun stopPreviewing() {
        if (previewing.compareAndSet(true, false)) {
            ID_TAG.logI("stopPreviewing")
//            handler.quit()
            imageReader?.close()
            imageReader = null
            cameraStateCallback.quit()
        }
    }

    private fun decidePreviewSize(map: StreamConfigurationMap): Size {
        val targetRange = 1280 * 720 .. 1920 * 1080
        val outputSizes = map.getOutputSizes(ImageFormat.JPEG)
            .filter { it.width * 9 == it.height * 16 }
            .sortedWith(Comparator<Size> { a, b ->
                if (a.height != b.height) {
                    return@Comparator a.height - b.height
                }
                return@Comparator b.width - a.width
            })
        val targetSize = outputSizes.filter { it.width * it.height in targetRange }.lastOrNull()
        return targetSize ?: outputSizes.first()
    }

    private fun adjustPreviewerSize(size: Size) {
        val container = requireView()
        val parentRatio = container.width.toFloat() / container.height.toFloat()
        val targetRatio = size.shorter.toFloat() / size.longer.toFloat()

        if (parentRatio < targetRatio) {
            previewer.frameLayoutParams {
                height = container.height
                width = container.height * size.shorter / size.longer
            }
        } else if (parentRatio > targetRatio) {
            previewer.frameLayoutParams {
                width = container.width
                height = container.width * size.longer / size.shorter
            }
        } else {
            previewer.frameLayoutParams {
                width = container.width
                height = container.height
            }
        }
    }

    private fun quit() {
        ID_TAG.logI("quit")
        stopPreviewing()
        removeSelf()
    }

    private fun onError(message: String) {
        ID_TAG.logW("onError($message)")
        stopPreviewing()
    }

    private fun onDecoded(msg: Message) {
        if (msg.what == R.id.qrcode_decode_fail) {
            handler.scan()
            return
        }
        if (msg.what == R.id.qrcode_decode_success) {
            val obj = msg.obj as Array<Any>
            val data = obj[0]
            val results = obj[1] as Array<Result>
            stopPreviewing()

            results.forEach { r ->
                r.text.toastS()
            }
        }
    }





    private inner class SurfaceTextureListener(
        val available: (Boolean) -> Unit,
    ) : TextureView.SurfaceTextureListener, AtomicBoolean(false) {

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            ID_TAG.logD("onSurfaceTextureAvailable")
            if (compareAndSet(false, true)) {
                available(true)
            }
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            ID_TAG.logD("onSurfaceTextureSizeChanged")
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            ID_TAG.logD("onSurfaceTextureDestroyed")
            if (compareAndSet(true, false)) {
                available(false)
            }
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            ID_TAG.logD("onSurfaceTextureUpdated")
        }
    }


    private inner class CameraStateCallback : CameraDevice.StateCallback() {
        var camera: CameraDevice? = null; private set
        private var requestBuilder: CaptureRequest.Builder? = null
        private var captureSession: CameraCaptureSession? = null

        override fun onOpened(camera: CameraDevice) {
            ID_TAG.logI("camera onOpened()")
            this.camera = camera
            createSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            ID_TAG.logI("camera onDisconnected()")
            camera.close()
            this.camera = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            ID_TAG.logI("camera onError($error)")
            camera.close()
            this.camera = null
        }

        private fun createSession() {
            ID_TAG.logI("camera createSession()")
            val texture = previewer.surfaceTexture!!
            texture.setDefaultBufferSize(previewSize.width, previewSize.height)
            val surface = Surface(texture)
            requestBuilder?.removeTarget(surface)
            requestBuilder = this.camera!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).also { builder ->
                builder.addTarget(surface)
                imageReader?.surface?.let { builder.addTarget(it) }
                builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                builder.set(CaptureRequest.JPEG_ORIENTATION, 90)
            }
            this.camera!!.createCaptureSession(
                listOfNotNull(surface, imageReader?.surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession?.close()
                        captureSession = session
                        try {
                            captureSession?.setRepeatingRequest(requestBuilder!!.build(), null, null)
                        } catch (e: Exception) {
                            ID_TAG.logW("camera createCaptureSession exception").logW(e)
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        ID_TAG.logW("camera createCaptureSession onConfigureFailed")
                    }
                }, null
            )
        }

        fun quit() {
            ID_TAG.logI("camera quit")
            this.camera?.close()
            this.camera = null
            this.requestBuilder = null
            this.captureSession?.close()
            this.captureSession = null
        }
    }

    private val Size.longer: Int; get() = if (height > width) height else width
    private val Size.shorter: Int; get() = if (height > width) width else height
}