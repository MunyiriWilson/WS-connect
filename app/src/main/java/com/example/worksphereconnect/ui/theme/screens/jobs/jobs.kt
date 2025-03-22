package com.example.worksphereconnect.ui.theme.screens.jobs


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worksphereconnect.R


class JobsPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobsPageScreen()
        }
    }
}

@Composable
fun JobsPageScreen() {
    val backgroundColor = Color(0xFFf2f3f4) // Background color
    val primaryColor = Color(0xFF1E3A8A)
    val secondaryColor = Color(0xFF3370a9)
    val accentColor = Color(0xFF06B6D4)
    val textColor = Color(0xFF3370a9)

    Scaffold(
        topBar = { TopBar(textColor) },
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            JobList(textColor, secondaryColor)
        }
    }
}

@Composable
fun TopBar(textColor: Color) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFf2f3f4))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Profile Action */ }) {
            Icon(Icons.Default.Person, contentDescription = "Profile", tint = textColor)
        }

        // Replaced text with an image spanning the space
        Image(
            painter = painterResource(id = R.drawable.applogo), // Change to your actual image resource
            contentDescription = "W.S CONNECT Logo",
            modifier = Modifier
                .weight(1f) // Ensures it spans the space
                .height(30.dp) // Adjust as needed
        )

        IconButton(onClick = { menuExpanded = true }) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = textColor)
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.background(Color(0xFFf2f3f4))
        ) {
            listOf("Freelancing", "Help", "Settings", "Resume Help", "Notifications", "About Us").forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, color = textColor) },
                    onClick = { /* Handle action */ }
                )
            }
        }
    }
}

@Composable
fun JobList(textColor: Color, cardColor: Color) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // You can populate this list with job data
    }
}

@Composable
fun JobCard(job: Job, textColor: Color, cardColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Open job details */ },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(job.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
            Text(job.description, fontSize = 14.sp, color = textColor.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Budget: ${job.budget}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)

            Row(modifier = Modifier.padding(top = 8.dp)) {
                job.tags.forEach { tag ->
                    Text(
                        text = tag,
                        fontSize = 12.sp,
                        color = textColor,
                        modifier = Modifier
                            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                            .padding(6.dp)
                            .padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
    }
}

data class Job(val title: String, val description: String, val budget: String, val tags: List<String>)

@Composable
fun BottomNavigationBar() {
    val backgroundColor = Color(0xFFf2f3f4)
    val iconColor = Color(0xFF3370a9)

    var selectedIndex by remember { mutableStateOf(2) } // Default selection: Jobs
    val navItems = listOf(
        R.drawable.promotion to "Tracking",
        R.drawable.candidate to "Affiliations",
        R.drawable.home to "Jobs",
        R.drawable.appointment to "Learning",
        R.drawable.star to "Reviews"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        navItems.forEachIndexed { index, (imageRes, description) ->
            val isSelected = index == selectedIndex
            var isClicked by remember { mutableStateOf(false) }

            // Animation for slight enlargement and shrink back
            val scale by animateFloatAsState(
                targetValue = if (isClicked) 1.1f else 1.0f,
                animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
                finishedListener = { isClicked = false }, // Reset after animation completes
                label = "scale"
            )

            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(5.dp, backgroundColor, RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .padding(1.dp)
                    .clickable {
                        isClicked = true
                        selectedIndex = index
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = description,
                    modifier = Modifier
                        .size((32 * scale).dp) // Briefly enlarges then shrinks
                        .graphicsLayer(scaleX = scale, scaleY = scale),
                    colorFilter = ColorFilter.tint(iconColor)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePageScreen() {
    JobsPageScreen()
}
