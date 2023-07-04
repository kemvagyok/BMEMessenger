package hu.bme.aut.android.bmemessenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.bmemessenger.model.Message
import hu.bme.aut.android.bmemessenger.ui.theme.BmemessengerTheme
import hu.bme.aut.android.bmemessenger.viewmodel.ConversationViewModel

class ConversationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: ConversationViewModel by viewModels()
        setContent {
            BmemessengerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.getActualUserDataFromActivity(intent)
                    conversationUI(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun conversationUI(viewModel: ConversationViewModel)
{

    var context = LocalContext.current

    viewModel.checkActualUser(context)

    Scaffold(
    bottomBar = {
        TopAppBar(

            title = { Text("") },
            navigationIcon = {
                Row {
                    OutlinedTextField(
                        value =  viewModel.yourMessage,
                        onValueChange = { viewModel.yourMessage = it },
                        label = { "${viewModel.otherUser.component1().name} számára" }
                    )
                    Spacer(Modifier.padding(5.dp))
                    Button(onClick = { viewModel.sendMessage() }, enabled = viewModel.actualUser.component1().getIsCanWritten ?: true) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = stringResource(R.string.sendTextLabel))
                    }
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
        viewModel.listeningMessage()
        MessageList(viewModel)
    }
}

}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageList(viewModel: ConversationViewModel) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        items(  viewModel.messages ) {
                message ->
                MessageRow(message,viewModel)
        }
    }
}


@Composable
fun MessageRow(message: Message, viewModel: ConversationViewModel)
{
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .padding(10.dp))
    {
        Image(bitmap = ImageBitmap.imageResource(id = R.drawable.negan_profile_picture), contentDescription = "Negan profil") //EZ CSAK DÍSZ
        if(viewModel.actualUser.component1().userId == message.writer)
            Text(text = "Ön: ${message.text}")
        else
            Text(text = "${viewModel.otherUser.component1().name}: ${message.text}")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    BmemessengerTheme {

    }
}