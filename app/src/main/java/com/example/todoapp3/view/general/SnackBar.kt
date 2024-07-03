package com.example.todoapp3.view.general

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todoapp3.R

@Composable
fun SnackBar(text: String, snackbarVisible: State<Boolean>, onClick: () -> Unit) {
    if (snackbarVisible.value) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor  = MaterialTheme.colorScheme.tertiary
                    ),
                    onClick = { onClick() }) {
                    Text(text = stringResource(id = R.string.okey))
                }
            }
        ) {
            Text(text = text)
        }
    }
}