package hu.bme.aut.android.bmemessenger



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.bmemessenger.enum.Types
import hu.bme.aut.android.bmemessenger.ui.theme.BmemessengerTheme
import hu.bme.aut.android.bmemessenger.viewmodel.UserMainViewModel

class UserMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: UserMainViewModel by viewModels()
        viewModel.getActualUserDataFromActivity(intent)
        setContent {
            BmemessengerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    userMainUI(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun userMainUI(viewModel: UserMainViewModel) {

    val context = LocalContext.current

    viewModel.checkActualUser(context)

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            TopAppBar(

                title = { Text("${stringResource(R.string.patnersText)} (TE: ${viewModel.actualUser.component1().name})") },
                navigationIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false })
                    {
                        DropdownMenuItem(text = { Text(text = stringResource(R.string.detailsText)) }, onClick = { viewModel.toUserDetailsActivity(context);  expanded = !expanded})
                        if (viewModel.actualUser.component1().type == Types.professor)
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.settingText)) },
                                onClick = {viewModel.toSettingProfessorActivity(context);expanded = !expanded})
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.logoutText)) },
                            onClick = { viewModel.signOut(context);  expanded = !expanded })
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
            viewModel.listeningUsers()
            UserList(viewModel)
            }
        }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserList(viewModel: UserMainViewModel) {
    val context  = LocalContext.current
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            items(  viewModel.profiles ) {
                        profile ->
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Button(onClick = { viewModel.toConversationActivity(profile,context)}) {
                            Text(text = "${profile.name}")
                        }
                    }
            }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BmemessengerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        )
        {
            val viewModel: UserMainViewModel by viewModel()
            userMainUI(viewModel)
        }
    }

    }