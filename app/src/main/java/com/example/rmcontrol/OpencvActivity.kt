package com.example.rmcontrol

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rmcontrol.databinding.ActivityMainBinding

import com.example.rmcontrol.databinding.ActivityOpencvBinding
import org.opencv.android.CameraActivity
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.util.Collections

class OpencvActivity : CameraActivity() {
    private lateinit var b: ActivityOpencvBinding
    lateinit var camera :CameraBridgeViewBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opencv)

        b = ActivityOpencvBinding.inflate(layoutInflater)
        setContentView(b.root)
        camera = b.camera
        camera.setCameraIndex(0)



        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 100)

        }
        if (OpenCVLoader.initLocal())
        {
            camera.enableView()
        }

        camera.setCvCameraViewListener(object : CvCameraViewListener2{
            override fun onCameraViewStarted(width: Int, height: Int) {
            }

            override fun onCameraViewStopped() {
            }

            override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {

                val rgba = inputFrame?.rgba()
                //val gray = Mat(rgba?.size(), CvType.CV_8UC1)
                //Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_RGBA2GRAY)
                val rotatedImage = rotateImage(rgba!!, -90.0) // Поворот изображения на 90 градусов

                val blurredImage = Mat()
                Imgproc.GaussianBlur(rotatedImage, blurredImage, Size(5.0, 5.0), 0.0)

                // Определяем центр изображения
                /*val centerX = rotatedImage.width() / 2
                val centerY = rotatedImage.height() / 2

                // Определяем верхний левый и нижний правый углы прямоугольника
                val rectWidth = 100
                val rectHeight = 100
                val topLeft = Point((centerX - rectWidth / 2).toDouble(), (centerY - rectHeight / 2).toDouble())
                val bottomRight = Point((centerX + rectWidth / 2).toDouble(), (centerY + rectHeight / 2).toDouble())*/

                // Рисуем зелёный прямоугольник
                //Imgproc.rectangle(blurredImage, topLeft, bottomRight, Scalar(0.0, 255.0, 0.0), 4)

                // Преобразуем изображение в HSV (Hue, Saturation, Value)
                // Преобразуем изображение в формат BGR
                val bgrImage = Mat()
                Imgproc.cvtColor(blurredImage, bgrImage, Imgproc.COLOR_RGBA2BGR)

// Преобразуем BGR в HSV
                val hsvImage = Mat()
                Imgproc.cvtColor(bgrImage, hsvImage, Imgproc.COLOR_BGR2HSV)

                // Определяем диапазон зеленого цвета в HSV
                val lowerGreen = Scalar(35.0, 100.0, 100.0)
                val upperGreen = Scalar(85.0, 255.0, 255.0)

                // Синий цвет
                val lowerBlue = Scalar(100.0, 100.0, 100.0)
                val upperBlue = Scalar(140.0, 255.0, 255.0)


// Создаём маску для выделения зеленого цвета
                val greenMask = Mat()
                Core.inRange(hsvImage, lowerGreen, upperGreen, greenMask)

                val blueMask = Mat()
                Core.inRange(hsvImage, lowerBlue, upperBlue, blueMask)


                val combinedMask = Mat()
                Core.bitwise_or(greenMask, blueMask, combinedMask)

// Нахождение контуров зеленых областей
                val contours = mutableListOf<MatOfPoint>()
                val hierarchy = Mat()
                Imgproc.findContours(combinedMask, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

// Рисуем контуры на изображении
                for (contour in contours) {
                    val area = Imgproc.contourArea(contour)
                    if (area > 500) { // Фильтр по площади, чтобы избежать шума
                        val boundingRect = Imgproc.boundingRect(contour)
                        Imgproc.rectangle(blurredImage, boundingRect.tl(), boundingRect.br(), Scalar(0.0, 255.0, 0.0), 4)
                    }
                }

                return blurredImage
            }

        })
    }

    fun rotateImage(img: Mat, angle: Double): Mat {
        // Получаем центр изображения
        val center = Point((img.width() / 2).toDouble(), (img.height() / 2).toDouble())

        // Создаем матрицу преобразования для указанного угла
        val rotationMatrix = Imgproc.getRotationMatrix2D(center, angle, 1.0)

        // Рассчитываем размеры изображения после поворота
        val boundingSize = Size(img.width().toDouble(), img.height().toDouble())

        // Создаем выходное изображение
        val rotatedImage = Mat()

        // Применяем матрицу преобразования для поворота изображения
        Imgproc.warpAffine(img, rotatedImage, rotationMatrix, boundingSize)

        return rotatedImage
    }


    override fun getCameraViewList(): MutableList<out CameraBridgeViewBase> {
        return Collections.singletonList(camera)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                })
            }
        }
    }


}