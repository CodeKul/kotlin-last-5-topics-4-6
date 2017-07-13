package com.codekul.camera

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import android.content.Intent
import android.net.Uri
import android.os.Environment
import java.io.File
import io.reactivex.schedulers.Schedulers
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCapture.setOnClickListener {
            //val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = File(Environment.getExternalStorageDirectory(), "Pic.jpg")
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile))
            imageUri = Uri.fromFile(photoFile)
            startActivityForResult(intent, 1234)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK) {

                Compressor(this)
                        .compressToFileAsFlowable(photoFile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            val inputStream = FileInputStream(it)
                            val newFile = File(
                                    Environment.getExternalStoragePublicDirectory(""),
                                    "compressed.png"
                            )
                            val outputStream = FileOutputStream(newFile)
                            inputStream.channel.transferTo(0, it.length(), outputStream.channel)

                            inputStream.close()
                            outputStream.close()

                            imageView.setImageURI(Uri.fromFile(newFile))
                        }
                        .subscribe()
            }
        }
    }
}
