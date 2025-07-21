package com.qualcomm.fiointerface

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class CreateJobFileActivity : AppCompatActivity() {
    private lateinit var ioEngineSpinner: Spinner
    private lateinit var ioTypeSpinner: Spinner
    private lateinit var jobSizeSuffixSpinner: Spinner
    private lateinit var blockSizeSuffixSpinner: Spinner
    private lateinit var jobName: EditText
    private lateinit var numberOfJobs: EditText
    private lateinit var blockSize: EditText
    private lateinit var ioDepth: EditText
    private lateinit var jobSize: EditText
    private lateinit var bypassCache: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_job_file)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_job)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // action bar set up
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create Job File"

        ioEngineSpinner = findViewById(R.id.ioEngineSpinner)
        ioTypeSpinner = findViewById(R.id.ioTypeSpinner)
        jobSizeSuffixSpinner = findViewById(R.id.jobSizeSuffixSpinner)
        blockSizeSuffixSpinner = findViewById(R.id.blockSizeSuffixSpinner)
        jobName = findViewById(R.id.jobNameEntry)
        numberOfJobs = findViewById(R.id.jobNumberEntry)
        blockSize = findViewById(R.id.blockSizeEntry)
        ioDepth = findViewById(R.id.ioDepthEntry)
        jobSize = findViewById(R.id.jobSizeEntry)
        bypassCache = findViewById(R.id.bypassCacheCheckbox)

        // set up drop down menus
        setUpSpinners()

        // when submit button is pressed a job file gets created
        findViewById<Button>(R.id.submitJobFile).setOnClickListener {
            if (!validateInputs()) {
                return@setOnClickListener
            }

            val fioString = StringBuilder()

            // processing user input into string
            val jobNameString: String =
                if (jobName.text.toString() == "") "test" else jobName.text.toString()
            fioString.appendLine("[$jobNameString]")

            val bypassCacheString: String =
                if (bypassCache.isChecked) "1" else "0"
            fioString.appendLine("direct=$bypassCacheString")

            fioString.appendLine("ioengine=${ioEngineSpinner.selectedItem}")

            fioString.appendLine("rw=${ioTypeSpinner.selectedItem}")

            if (numberOfJobs.text.toString().isNotBlank())
                fioString.appendLine("numjobs=${numberOfJobs.text}")

            if (ioDepth.text.toString().isNotBlank())
                fioString.appendLine("iodepth=${ioDepth.text}")

            if (jobSize.text.toString().isNotBlank())
                fioString.appendLine("size=${jobSize.text}${jobSizeSuffixSpinner.selectedItem}")

            if (blockSize.text.toString().isNotBlank())
                fioString.appendLine("bs=${blockSize.text}${blockSizeSuffixSpinner.selectedItem}")

            // writing the data out to an fio job file
            val file = File(this.getExternalFilesDir(null), "$jobNameString.fio")
            file.writeText(fioString.toString())
            Toast.makeText(this, "Successfully created job file", Toast.LENGTH_SHORT).show()
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

    private fun setUpSpinners() {
        val ioEngineAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.io_engine_options,
            android.R.layout.simple_spinner_item
        )
        ioEngineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val ioTypeAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.io_type_options,
            android.R.layout.simple_spinner_item
        )
        ioTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val suffixAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.integer_suffix_options,
            android.R.layout.simple_spinner_item
        )
        suffixAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Adding the adapters to the spinner objects
        ioEngineSpinner.adapter = ioEngineAdapter
        ioTypeSpinner.adapter = ioTypeAdapter
        jobSizeSuffixSpinner.adapter = suffixAdapter
        blockSizeSuffixSpinner.adapter = suffixAdapter
    }

    private fun validateInputs(): Boolean {
        val validationChecker = JobFileInputValidation()
        var validation = true

        if (!validationChecker.validateSizeGreaterThanBlockSize(
                blockSize = blockSize.text.toString(),
                blockSizeSuffix = blockSizeSuffixSpinner.selectedItem.toString(),
                size = jobSize.text.toString(),
                sizeSuffix = jobSizeSuffixSpinner.selectedItem.toString()
            )
        ) {
            Toast.makeText(this, "Size cannot be smaller than block size", Toast.LENGTH_SHORT)
                .show()
            validation = false
        }

        if (!validationChecker.validateSizeNotEmptyWhenBlockSizeNotEmpty(
                blockSize = blockSize.text.toString(),
                size = jobSize.text.toString()
            )
        ) {
            Toast.makeText(this, "Size cannot be empty if block size is filled out", Toast.LENGTH_SHORT)
                .show()
            validation = false
        }

        return validation
    }
}
