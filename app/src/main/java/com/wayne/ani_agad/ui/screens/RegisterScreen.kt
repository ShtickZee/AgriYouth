package com.wayne.ani_agad.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wayne.ani_agad.ui.theme.AppGradient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }
    var agreeToTerms by remember { mutableStateOf(false) }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var verifyPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var syncMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val passwordsMatch = password.isNotEmpty() && password == verifyPassword
    val isFormValid = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && passwordsMatch && agreeToTerms && !isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppGradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("🌱", fontSize = 50.sp)
                
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                // Basic Info Fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        errorMessage = null 
                    },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        errorMessage = null 
                    },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { 
                        phone = it
                        errorMessage = null 
                    },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    enabled = !isLoading
                )

                // Password Fields
                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        errorMessage = null 
                    },
                    label = { Text("Create Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !isLoading,
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = null)
                        }
                    }
                )

                OutlinedTextField(
                    value = verifyPassword,
                    onValueChange = { 
                        verifyPassword = it
                        errorMessage = null 
                    },
                    label = { Text("Verify Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = verifyPassword.isNotEmpty() && !passwordsMatch,
                    visualTransformation = if (verifyPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !isLoading,
                    trailingIcon = {
                        val image = if (verifyPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { verifyPasswordVisible = !verifyPasswordVisible }) {
                            Icon(image, contentDescription = null)
                        }
                    }
                )

                if (verifyPassword.isNotEmpty() && !passwordsMatch) {
                    Text(
                        text = "Passwords do not match",
                        color = Color.Red,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = agreeToTerms,
                        onCheckedChange = { 
                            agreeToTerms = it
                            errorMessage = null 
                        },
                        enabled = !isLoading
                    )
                    Text(
                        text = "I agree to the terms and conditions",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }

                errorMessage?.let {
                    Text(
                        text = it, 
                        color = Color.Red, 
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                if (isLoading) {
                    Text(
                        text = syncMessage ?: "Processing...",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        syncMessage = "Creating Auth Account..."
                        auth.createUserWithEmailAndPassword(email.trim(), password)
                            .addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    val userId = authTask.result?.user?.uid
                                    if (userId != null) {
                                        syncMessage = "Syncing Profile to Cloud..."
                                        val userProfile = hashMapOf(
                                            "fullName" to name,
                                            "email" to email.trim(),
                                            "phoneNumber" to phone,
                                            "createdAt" to System.currentTimeMillis()
                                        )
                                        
                                        db.collection("users").document(userId)
                                            .set(userProfile)
                                            .addOnSuccessListener {
                                                isLoading = false
                                                onRegisterSuccess()
                                            }
                                            .addOnFailureListener { e ->
                                                isLoading = false
                                                errorMessage = "Profile sync failed"
                                            }
                                    }
                                } else {
                                    isLoading = false
                                    val exception = authTask.exception
                                    errorMessage = when (exception) {
                                        is FirebaseAuthInvalidCredentialsException -> "Invalid Email"
                                        is FirebaseAuthUserCollisionException -> "Account already exists"
                                        else -> "Registration failed"
                                    }
                                }
                            }
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Create Account", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
