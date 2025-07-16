package com.qualcomm.fiointerface

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
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

        // spinners and input data
        val ioEngineSpinner: Spinner = findViewById(R.id.ioEngineSpinner)
        val jobName: EditText = findViewById(R.id.jobNameEntry)
        val numberOfJobs: EditText = findViewById(R.id.jobNumberEntry)
        val blockSize: EditText = findViewById(R.id.blockSizeEntry)
        val ioDepth: EditText = findViewById(R.id.ioDepthEntry)
        val jobSize: EditText = findViewById(R.id.jobSizeEntry)
        val bypassCache: CheckBox = findViewById(R.id.bypassCacheCheckbox)

        // setting up drop down menus
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.io_engine_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ioEngineSpinner.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_job)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // when submit button is pressed a job file gets created
        val submitJobFileBttn: Button = findViewById(R.id.submitJobFile)
        submitJobFileBttn.setOnClickListener {
            fun writeFioFile(context: Context, fileName: String, content: String) {
                val file = File(context.getExternalFilesDir(null), "$fileName.fio")
                file.writeText(content)
            }

            fun addOptionToFIOFile(
                option: EditText,
                fioKeyword: String,
                fioContent: StringBuilder
            ) {
                if (option.text.toString() != "") {
                    fioContent.appendLine("$fioKeyword=${option.text}")
                }
            }

            val fioContent = StringBuilder()
            val jobNameString: String =
                if (jobName.text.toString() == "") "test" else jobName.text.toString()
            val bypassCacheString: String =
                if (bypassCache.isChecked) "1" else "0"

            fioContent.appendLine("[$jobNameString]")
            fioContent.appendLine("ioengine=${ioEngineSpinner.selectedItem}")
            fioContent.appendLine("direct=$bypassCacheString")
            addOptionToFIOFile(numberOfJobs, "numjobs", fioContent)
            addOptionToFIOFile(blockSize, "bs", fioContent)
            addOptionToFIOFile(ioDepth, "iodepth", fioContent)
            addOptionToFIOFile(jobSize, "size", fioContent)

            writeFioFile(this, jobNameString, fioContent.toString())
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
