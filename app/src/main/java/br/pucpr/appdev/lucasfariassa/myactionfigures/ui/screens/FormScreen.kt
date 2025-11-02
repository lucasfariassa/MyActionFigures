@file:Suppress("AssignedValueIsNeverRead")

package br.pucpr.appdev.lucasfariassa.myactionfigures.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import br.pucpr.appdev.lucasfariassa.myactionfigures.R
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure
import br.pucpr.appdev.lucasfariassa.myactionfigures.ui.theme.*
import coil.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    actionFigure: ActionFigure?,
    onSave: (ActionFigure) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(actionFigure?.name ?: "") }
    var work by remember { mutableStateOf(actionFigure?.work ?: "") }
    var brand by remember { mutableStateOf(actionFigure?.brand ?: "") }
    var description by remember { mutableStateOf(actionFigure?.description ?: "") }
    var purchasePrice by remember { mutableStateOf(actionFigure?.purchasePrice?.toString() ?: "") }

    var imageUri by remember { mutableStateOf(actionFigure?.photoUrl?.toUri()) }
    var newImageSelected by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            newImageSelected = true
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(actionFigure?.purchaseDate ?: Date()) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val title = if (actionFigure == null) stringResource(R.string.new_action_figure) else stringResource(
        R.string.edit_action_figure
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cancel)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val fieldShape = RoundedCornerShape(16.dp)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape
            )
            OutlinedTextField(
                value = work,
                onValueChange = { work = it },
                label = { Text(stringResource(R.string.work)) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape
            )
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text(stringResource(R.string.brand)) },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = fieldShape
            )
            OutlinedTextField(
                value = purchasePrice,
                onValueChange = { purchasePrice = it },
                label = { Text(stringResource(R.string.purchase_value)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = fieldShape
            )
            OutlinedTextField(
                value = dateFormat.format(selectedDate),
                onValueChange = {},
                label = { Text(stringResource(R.string.purchase_date)) },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.select_date),
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = fieldShape
            )

            Spacer(modifier = Modifier.height(8.dp))

        imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = stringResource(R.string.action_figure_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(BlueMAF)
                ) {
                    Text(stringResource(R.string.select_image))
                }

                Button(
                    onClick = {
                        var persistentPhotoUrl = actionFigure?.photoUrl

                        if (newImageSelected) {
                            imageUri?.let { uri ->
                                persistentPhotoUrl = saveImageToInternalStorage(context, uri)
                            }
                        }

                        val figureToSave = actionFigure?.copy(
                            name = name,
                            work = work,
                            brand = brand,
                            description = description,
                            purchasePrice = purchasePrice.toFloatOrNull() ?: 0f,
                            purchaseDate = selectedDate,
                            photoUrl = persistentPhotoUrl
                        ) ?: ActionFigure(
                            id = 0,
                            name = name,
                            work = work,
                            brand = brand,
                            description = description,
                            purchasePrice = purchasePrice.toFloatOrNull() ?: 0f,
                            purchaseDate = selectedDate,
                            photoUrl = persistentPhotoUrl
                        )

                        onSave(figureToSave)
                    },
                    colors = ButtonDefaults.buttonColors(GreenMAF)
                ) {
                    Text(stringResource(R.string.save))
                }
            }

        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate.time)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Date(millis)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }

    }
}

private fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.filesDir, "${System.currentTimeMillis()}.jpg")
        val outputStream = file.outputStream()
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
