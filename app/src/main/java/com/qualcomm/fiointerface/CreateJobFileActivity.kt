package com.qualcomm.fiointerface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class CreateJobFileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // action bar set up
        setContentView(R.layout.activity_create_job_file)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create Job File"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_job)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // when submit button is pressed a job file gets created
        val submitJobFileBttn = findViewById<Button>(R.id.submitJobFile)
        submitJobFileBttn.setOnClickListener {
            fun writeFioFile(context: Context, fileName: String, content: String) {
                val file = File(context.getExternalFilesDir(null), "$fileName.fio")
                file.writeText(content)
            }

            writeFioFile(this, "example", "")
        }
    }

    // sets action bar back button to return to home page
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}