package com.example.worksphereconnect.ui.theme.models

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

data class Job(
    val jobTitle: String = "",
    val jobDescription: String = "",
    val experienceLevel: String = "",
    val requiredDegree: String = "",
    val applicationDeadline: String = "",
    val companyBudget: String = "",
    val extraInfo: String = ""
)

class JobsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun postJob(job: Job, context: Context, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            db.collection("jobs")
                .add(job)
                .addOnSuccessListener {
                    Toast.makeText(context, "Job posted successfully!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to post job: ${e.message}", Toast.LENGTH_SHORT).show()
                    onFailure(e)
                }
        }
    }
}
