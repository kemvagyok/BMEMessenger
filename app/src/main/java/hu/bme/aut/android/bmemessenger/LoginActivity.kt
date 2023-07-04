package hu.bme.aut.android.bmemessenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.bmemessenger.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import hu.bme.aut.android.bmemessenger.ui.theme.BmemessengerTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: LoginViewModel by viewModels()
        setContent {
            BmemessengerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    loginUI(viewModel)
                }
            }
        }
        }

    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginUI(viewModel: LoginViewModel) {

    val context = LocalContext.current
    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(bitmap = ImageBitmap.imageResource(id = R.drawable.bme_logo_kicsi_color), contentDescription = stringResource(id = R.string.BMEImageText))
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = {Text(stringResource(R.string.emailLabelString)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = stringResource(R.string.emailLabelString))},
            modifier = Modifier.fillMaxWidth()

        )
        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            label = { Text(stringResource(R.string.usernameLabel)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Face, contentDescription = stringResource(R.string.usernameLabel))},
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.password,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { viewModel.password = it },
            label = { Text(stringResource(R.string.passwordLabelString)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = stringResource(R.string.passwordLabelString))},
            modifier = Modifier.fillMaxWidth()
        )
        Row {

            Button(onClick = {viewModel.login(context = context)}, modifier = Modifier.padding(20.dp))
            {
                Text(stringResource(R.string.loginText))
            }
            Button(onClick = {viewModel.signup(context = context)}, modifier = Modifier.padding(20.dp)) {
                Text(stringResource(R.string.signUpText))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun login(viewModel: LoginViewModel = viewModel())
{
    loginUI(viewModel)
}