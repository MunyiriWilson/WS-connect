package com.example.worksphereconnect.ui.theme.screens.homepage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.worksphereconnect.R
import com.example.worksphereconnect.ui.theme.screens.jobs.JobList
import com.example.worksphereconnect.ui.theme.screens.jobs.JobsFormActivity
import com.example.worksphereconnect.ui.theme.screens.jobs.JobsPageActivity
import com.example.worksphereconnect.ui.theme.screens.login.LoginActivity
import com.example.worksphereconnect.ui.theme.screens.registration.RegisterActivity


class HomePageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomePageScreen()
        }
    }
}

@Composable
fun HomePageScreen() {
    val backgroundColor = Color(0xFFf2f3f4) // Background color
    val primaryColor = Color(0xFF1E3A8A)
    val secondaryColor = Color(0xFF3370a9)
    val accentColor = Color(0xFF06B6D4)
    val textColor = Color(0xFF3370a9)

    Scaffold(
        topBar = { TopBar(textColor) },
        bottomBar = { BottomNavigation() }
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
        val context = LocalContext.current

        IconButton(onClick = { context.startActivity(Intent(context, JobsPageActivity::class.java))/* Profile Action */ }) {
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
fun BottomNavigation() {
    val backgroundColor = Color(0xFFf2f3f4)
    val iconColor = Color(0xFF3370a9)
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.promotion),
                contentDescription = "Tracking",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }

        IconButton(onClick = {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.candidate),
                contentDescription = "Affiliations",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }

        IconButton(onClick = {
            context.startActivity(Intent(context, HomePageActivity::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.home),
                contentDescription = "Jobs",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }

        IconButton(onClick = {
            context.startActivity(Intent(context, JobsPageActivity::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.appointment),
                contentDescription = "Learning",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }

        IconButton(onClick = {
            context.startActivity(Intent(context, JobsFormActivity::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Reviews",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePageScreen() {
    HomePageScreen()
}