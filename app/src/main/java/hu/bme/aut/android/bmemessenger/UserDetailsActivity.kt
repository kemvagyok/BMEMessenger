package hu.bme.aut.android.bmemessenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.android.bmemessenger.ui.theme.BmemessengerTheme
import hu.bme.aut.android.bmemessenger.viewmodel.UserDetailsViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class UserDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: UserDetailsViewModel by viewModels()
        setContent {
            BmemessengerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.getActualUserDataFromActivity(intent)
                    UserDetailsUI(viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDefault(viewModel: UserDetailsViewModel = viewModel())
{
    UserDetailsUI(viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsUI(viewModel: UserDetailsViewModel) {

    val context = LocalContext.current

    viewModel.checkActualUser(context)

    Scaffold(
        bottomBar = {
            TopAppBar(

                title = { Text("${stringResource(id = R.string.sendbackTextLabel)}") },
                navigationIcon = {
                        Button(onClick = { viewModel.toUserMainActivity(context) })
                        {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Vissza")
                        }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding()
                )
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.username = it },
                    label = {Text(stringResource(R.string.usernameLabel)) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Face, contentDescription = stringResource(R.string.usernameLabel))},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.padding(5.dp))
                Button(onClick = { viewModel.setUserName(context = context) }) {
                    Text(text = stringResource(R.string.sendNameTextLabel))
                }
                Spacer(Modifier.padding(5.dp))
                RadioButtons(viewModel = viewModel)

            }
        }
    }
}

@Composable
fun RadioButtons(viewModel: UserDetailsViewModel)
{

    var context = LocalContext.current

    val (selectedOption, onOptionSelected) =  viewModel.selectedOption
    Column {
        viewModel.typeOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text); viewModel.setType(context)}
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}