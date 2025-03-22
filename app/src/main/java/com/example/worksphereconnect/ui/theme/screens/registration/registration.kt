package com.example.worksphereconnect.ui.theme.screens.registration

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext


import androidx.activity.viewModels




import com.google.firebase.auth.FirebaseAuthUserCollisionException


import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.worksphereconnect.R
import com.example.worksphereconnect.ui.theme.ElectricBlue
import com.example.worksphereconnect.ui.theme.models.AuthViewModel
import com.example.worksphereconnect.ui.theme.screens.homepage.HomePageActivity


import com.example.worksphereconnect.ui.theme.screens.login.LoginActivity
import com.google.firebase.auth.AuthCredential
import com.google.i18n.phonenumbers.PhoneNumberUtil

class RegisterActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            RegisterScreen(authViewModel)
        }
    }
}

@Composable
fun RegisterScreen(authViewModel: AuthViewModel) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    val phoneUtil = PhoneNumberUtil.getInstance()
    val countryCodes = phoneUtil.supportedRegions.map { "+${phoneUtil.getCountryCodeForRegion(it)}" }
        .distinct()
        .sortedBy { it.replace("+", "").toInt() }
    var selectedCountryCode by remember { mutableStateOf(countryCodes.first()) }
    var expanded by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F2F2))
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.newlogo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .width(565.dp) // Wider
                    .height(screenHeight * 0.1f) // Shorter
            )
            Spacer(modifier = Modifier.height(16.dp))

            RegistrationTextField("Company Name", name) { name = it }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = "$selectedCountryCode $phone",
                    onValueChange = {
                        val parts = it.split(" ", limit = 2)
                        if (parts.size == 2) {
                            selectedCountryCode = parts[0]
                            phone = parts[1]
                        } else {
                            phone = parts[0]
                        }
                    },
                    label = { Text("Phone Number", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = Color.Gray,
                    ),
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countryCodes.forEach { code ->
                        DropdownMenuItem(
                            text = { Text(code) },
                            onClick = {
                                selectedCountryCode = code
                                expanded = false
                            }
                        )
                    }
                }
            }

            RegistrationTextField("Email", email) { email = it }
            PasswordTextField("Password", password) { password = it }
            PasswordTextField("Confirm Password", confirmPassword) { confirmPassword = it }

            Spacer(modifier = Modifier.height(10.dp))
            val context = LocalContext.current

            Text(
                text = "By continuing, you agree to our ",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Terms and Conditions",
                fontSize = 12.sp,
                color = ElectricBlue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yourwebsite.com/terms.pdf"))
                    context.startActivity(intent)
                })

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        errorMessage = "All fields are required!"
                        return@Button
                    }
                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match!"
                        return@Button
                    }

                    authViewModel.signUp(name, email, password,
                        onSuccess = {
                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, HomePageActivity::class.java))
                        },
                        onFailure = { error ->
                            errorMessage = when {
                                error.contains("User with this email already exists") -> "This email is already registered. Try logging in."
                                error.contains("All fields are required") -> "Please fill in all fields."
                                else -> error
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign Up", fontSize = 18.sp)
            }


            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Have an account? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Login",
                    color = ElectricBlue,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            SocialLoginButtons()
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElectricBlue,
            unfocusedBorderColor = Color.Gray,
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    var visibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visibility = !visibility }) {
                Icon(
                    painter = painterResource(if (visibility) R.drawable.visibility else R.drawable.visibility_off),
                    contentDescription = "Toggle Password Visibility"
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElectricBlue,
            unfocusedBorderColor = Color.Gray,
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun SocialLoginButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val icons = listOf(
            R.drawable.googleicon,
            R.drawable.appleicon,
            R.drawable.msicon,
        )

        icons.forEach { icon ->
            IconButton(
                onClick = { /* Handle social login */ },
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFF9FAFB), CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    val context = LocalContext.current.applicationContext
    val fakeAuthViewModel = AuthViewModel(context as Application)

    RegisterScreen(fakeAuthViewModel)
}
