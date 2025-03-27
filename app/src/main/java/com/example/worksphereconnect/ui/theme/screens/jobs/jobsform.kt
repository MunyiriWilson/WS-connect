package com.example.worksphereconnect.ui.theme.screens.jobs

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.worksphereconnect.ui.theme.models.Job
import com.example.worksphereconnect.ui.theme.models.JobsViewModel

class JobsFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jobsViewModel = ViewModelProvider(this)[JobsViewModel::class.java]

        setContent {
            val navController = rememberNavController()
            JobsFormScreen(navController, jobsViewModel)
        }
    }
}

@Composable
fun JobsFormScreen(navController: NavController, jobsViewModel: JobsViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var jobTitle by remember { mutableStateOf(TextFieldValue("")) }
    var jobDescription by remember { mutableStateOf(TextFieldValue("")) }
    var experienceLevel by remember { mutableStateOf("Select Experience Level") }
    var requiredDegree by remember { mutableStateOf(TextFieldValue("")) }
    var applicationDeadline by remember { mutableStateOf(TextFieldValue("")) }
    var companyBudget by remember { mutableStateOf(TextFieldValue("")) }
    var extraInfo by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState) // Enables scrolling
        ) {
            OutlinedTextField(
                value = jobTitle,
                onValueChange = { jobTitle = it },
                label = { Text("Job Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = jobDescription,
                onValueChange = { jobDescription = it },
                label = { Text("Job Description") },
                modifier = Modifier.fillMaxWidth()
            )

            ExperienceDropdown(experienceLevel) { experienceLevel = it }

            OutlinedTextField(
                value = requiredDegree,
                onValueChange = { requiredDegree = it },
                label = { Text("Required Degree") },
                modifier = Modifier.fillMaxWidth()
            )

            // Modified to be a normal text input instead of Date Picker
            OutlinedTextField(
                value = applicationDeadline,
                onValueChange = { applicationDeadline = it },
                label = { Text("Application Deadline") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = companyBudget,
                onValueChange = { companyBudget = it },
                label = { Text("Company Budget") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = extraInfo,
                onValueChange = { extraInfo = it },
                label = { Text("Extra Information") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (jobTitle.text.isNotEmpty() && jobDescription.text.isNotEmpty() &&
                        experienceLevel != "Select Experience Level" && requiredDegree.text.isNotEmpty() &&
                        applicationDeadline.text.isNotEmpty() && companyBudget.text.isNotEmpty()
                    ) {
                        val job = Job(
                            jobTitle = jobTitle.text,
                            jobDescription = jobDescription.text,
                            experienceLevel = experienceLevel,
                            requiredDegree = requiredDegree.text,
                            applicationDeadline = applicationDeadline.text, // Manual input
                            companyBudget = companyBudget.text,
                            extraInfo = extraInfo.text
                        )

                        jobsViewModel.postJob(job, context, onSuccess = {
                            jobTitle = TextFieldValue("")
                            jobDescription = TextFieldValue("")
                            experienceLevel = "Select Experience Level"
                            requiredDegree = TextFieldValue("")
                            applicationDeadline = TextFieldValue("") // Reset field
                            companyBudget = TextFieldValue("")
                            extraInfo = TextFieldValue("")
                        }, onFailure = {
                            Toast.makeText(context, "Failed to post job!", Toast.LENGTH_SHORT).show()
                        })
                    } else {
                        Toast.makeText(context, "Please fill all required fields!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Job", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("Post a Job", color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E3A8A))
    )
}

@Composable
fun ExperienceDropdown(selectedLevel: String, onSelectionChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val experienceLevels = listOf("Junior", "Mid-Level", "Senior")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(4.dp))
            .clickable { expanded = true }
            .padding(8.dp)
    ) {
        Text(text = selectedLevel, color = Color.Black)

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            experienceLevels.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level) },
                    onClick = {
                        onSelectionChange(level)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJobsFormScreen() {
    val navController = rememberNavController()
    val jobsViewModel: JobsViewModel = viewModel() // Ensure it works in preview mode
    JobsFormScreen(navController, jobsViewModel)
}
