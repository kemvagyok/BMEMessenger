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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.android.bmemessenger.ui.theme.BmemessengerTheme
import hu.bme.aut.android.bmemessenger.viewmodel.SettingProfessorViewModel

class SettingProfessorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SettingProfessorViewModel by viewModels()
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
fun DefaultPreview3() {
    BmemessengerTheme {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsUI(viewModel: SettingProfessorViewModel) {

    val context = LocalContext.current

    viewModel.checkActualUser(context)

    Scaffold(
        bottomBar = {
            TopAppBar(

                title = { Text("${stringResource(id = R.string.studentCommunicationConstraintText)}") },
                navigationIcon = {
                    Button(onClick = { viewModel.toUserMainActivity(context) })
                    {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.sendbackTextLabel))
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
            viewModel.listeningStudents()
            StudentList(viewModel)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentList(viewModel: SettingProfessorViewModel) {
    val context = LocalContext.current
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        items(  viewModel.students ) {
                student ->
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Button(onClick = { viewModel.disableStudentWriting(student, context) }) {
                    Row {
                        Text(text = "${student.name}")
                        Icon(imageVector =
                        if(viewModel.canWrittenIconList[viewModel.students.indexOf(student)]) { Icons.Default.Edit }
                        else { Icons.Default.Close },
                            contentDescription ="Írhatóség")
                    }

                }
            }
        }
    }
}
