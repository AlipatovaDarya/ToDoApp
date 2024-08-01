package com.example.todoapp3.presentation.common

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todoapp3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SnackBar(text: String, snackbarVisible: State<Boolean>, onClick: () -> Unit) {
    if (snackbarVisible.value) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
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

/**
 * Custom composable function for show SnackBar with Error
 */
fun showErrorSnackbar(
    context: Context,
    errorCode: Int?,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    syncRemote: () -> Unit
) {
    val message = when (errorCode) {
        1 -> context.getString(R.string.error_adding_item)
        2 -> context.getString(R.string.error_deleting_item)
        3 -> context.getString(R.string.error_editing_is_competed_item_status)
        4 -> context.getString(R.string.error_synchronization_with_server)
        5 -> context.getString(R.string.error_deleting_from_server)
        6 -> context.getString(R.string.error_connection)
        7 -> context.getString(R.string.error_receiving_data_from_server)
        else -> "${context.getString(R.string.error_receiving_data_from_server)} $errorCode"
    }
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = message,
            actionLabel = context.getString(R.string.retry)
        )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                syncRemote()
            }

            SnackbarResult.Dismissed -> {}
        }
    }
}