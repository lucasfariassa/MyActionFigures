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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import br.pucpr.appdev.lucasfariassa.myactionfigures.data.ActionFigure
import coil.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    val title = if (actionFigure == null) "Novo Action Figure" else "Editar Action Figure"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancelar"
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = work,
                onValueChange = { work = it },
                label = { Text("Obra") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            OutlinedTextField(
                value = purchasePrice,
                onValueChange = { purchasePrice = it },
                label = { Text("Valor da Compra") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            OutlinedTextField(
                value = dateFormat.format(selectedDate),
                onValueChange = {},
                label = { Text("Data da Compra") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar Data",
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Imagem do Action Figure",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Selecionar Imagem")
            }

            Button(onClick = {
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
                    id = 0, // Room irá gerar o ID
                    name = name,
                    work = work,
                    brand = brand,
                    description = description,
                    purchasePrice = purchasePrice.toFloatOrNull() ?: 0f,
                    purchaseDate = selectedDate,
                    photoUrl = persistentPhotoUrl
                )
                onSave(figureToSave)
            }) {
                Text("Salvar")
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
                        val correctedMillis = millis + TimeZone.getDefault().getOffset(millis)
                        selectedDate = Date(correctedMillis)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
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
