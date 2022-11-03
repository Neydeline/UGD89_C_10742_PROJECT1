package com.example.ugd89_c_10742_project1

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException


class CameraView(context: Context?, private var mCamera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder
    private var cameraId = -1

    init{
        mCamera.setDisplayOrientation(90)
        mHolder = holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder){
        try{
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()

        } catch (e: IOException){
            Log.d("error", "Camera error on SurfaceCreated" +e.message)
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        if (mHolder.surface == null) return
        try{
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: IOException){
            Log.d("Error", "Camera error on SurfaceChanged" + e.message)
        }
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        mCamera.stopPreview()
        mCamera.release()
    }

    fun switchCamera(){
        if (mCamera != null) {
            mCamera.stopPreview()
        }
        //NB: if you don't release the current camera before switching, you app will crash
        mCamera.release()

        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_BACK)
                cameraId = i
        }
        //swap the id of the camera to be used
        if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
        }
        else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK
        }
        mCamera = Camera.open(cameraId)

        mCamera.setDisplayOrientation(90)
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: IOException) {
            Log.d("Error", "Camera error on switchCamera" + e.message)
        }
        mCamera.startPreview()
    }
}