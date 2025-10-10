package com.example.offlinecatchsync

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.offlinecatchsync.localDataBase.DataEntity

@Composable
fun DataScreen(viewModel: DataViewModel, modifier: Modifier = Modifier) {
    val dataList by viewModel.allData.collectAsState()
    var text by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<DataEntity?>(null) }
    var editText by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Enter text") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (text.isNotBlank()) {
                                viewModel.addData(text)
                                text = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = text.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Data", fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = dataList.isEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No data yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = dataList,
                    key = { it.id }
                ) { item ->
                    DataItemCard(
                        item = item,
                        onEdit = {
                            editingItem = item
                            editText = item.text
                            showEditDialog = true
                        },
                        onDelete = {
                            // viewModel.deleteData(item)
                        }
                    )
                }
            }
        }

        // Edit Dialog
        if (showEditDialog && editingItem != null) {
            EditDialog(
                text = editText,
                onTextChange = { editText = it },
                onDismiss = {
                    showEditDialog = false
                    // editingItem = null
                    editText = ""
                },
                onConfirm = {
                    if (editText.isNotBlank()) {
                        //   viewModel.updateData(editingItem!!.copy(text = editText))
                        showEditDialog = false
                        //   editingItem = null
                        editText = ""
                    }
                }
            )
        }
    }
}

@Composable
fun DataItemCard(
    item: DataEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                showDeleteConfirm = true
                false
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                    else -> Color.Transparent
                },
                label = "background_color"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (item.isSynced)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sync status indicator
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color = if (item.isSynced) Color.Green else Color.Gray,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))

                // Text content
                Text(
                    text = item.text,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Item") },
            text = { Text("Are you sure you want to delete this item?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EditDialog(
    text: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f)
    ) {
        Dialog(onDismissRequest = {
            isVisible = false
            onDismiss()
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Edit Item",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = text,
                        onValueChange = onTextChange,
                        label = { Text("Edit text") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                isVisible = false
                                onDismiss()
                            }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onConfirm,
                            enabled = text.isNotBlank(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}