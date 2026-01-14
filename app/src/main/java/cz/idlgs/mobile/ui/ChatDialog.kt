package cz.idlgs.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.idlgs.mobile.BuildConfig
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.viewmodel.ChatViewModel
import cz.idlgs.mobile.viewmodel.Role

@Composable
fun ChatDialog(
	onDismiss: () -> Unit,
	viewModel: ChatViewModel = viewModel()
) {
	var text by remember { mutableStateOf("") }
	val messages by viewModel.messages.collectAsState()
	val isLoading by viewModel.isLoading.collectAsState()
	val focusRequester = remember { FocusRequester() }

	val lazyListState = rememberLazyListState()
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(messages) {
		if (messages.isNotEmpty()) {
			val firstVisibleIndex = lazyListState.firstVisibleItemIndex
			if (!lazyListState.isScrollInProgress && firstVisibleIndex < 1)
				lazyListState.animateScrollToItem(0)
		}
	}

	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}

	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false),
	) {
		Surface(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
				.imePadding(),
			shape = RoundedCornerShape(16.dp),
			color = MaterialTheme.colorScheme.surface,
			tonalElevation = 8.dp
		) {
			Column(modifier = Modifier.fillMaxSize()) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp,8.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = if (BuildConfig.DEBUG) "Chat" else "AI Assistant",
						style = MaterialTheme.typography.titleLarge
					)
					IconButton(onClick = onDismiss) {
						Icon(Icons.Default.Close, contentDescription = "Close")
					}
				}
				HorizontalDivider()

				LazyColumn(
					state = lazyListState,
					reverseLayout = true,
					modifier = Modifier
						.weight(1f)
						.padding(horizontal = 8.dp)
				) {
					items(messages.reversed()) { message ->
						val isUser = message.role == Role.user
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(vertical = 4.dp),
							contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
						) {
							Surface(
								shape = RoundedCornerShape(12.dp),
								color = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
								modifier = Modifier.widthIn(max = 280.dp)
							) {
								Text(
									text = message.content,
									modifier = Modifier.padding(12.dp),
									color = MaterialTheme.colorScheme.onSurface
								)
							}
						}
					}
				}

				if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
				HorizontalDivider()

				Row(
					modifier = Modifier
						.padding(8.dp)
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					OutlinedTextField(
						value = text,
						onValueChange = { text = it },
						modifier = Modifier
							.weight(1f)
							.focusRequester(focusRequester)
							.onPreviewKeyEvent {
								if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
									if (it.isCtrlPressed || it.isShiftPressed) {
										false
									} else {
										if (text.isNotBlank() && !isLoading) {
											viewModel.sendMessage(text.trim())
											text = ""
										}
										true
									}
								} else false
							},
						placeholder = { Text("Ask something...") },
						maxLines = 3
					)
					Spacer(modifier = Modifier.width(8.dp))
					IconButton(
						onClick = {
							viewModel.sendMessage(text.trim())
							keyboardController!!.hide()
							text = ""
						},
						enabled = text.isNotBlank() && !isLoading
					) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.Send,
							contentDescription = "Send",
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
			}
		}
	}
}

@PreviewScreenSizes
@Composable
fun ChatDialogPreview() {
	IDLGSTheme {
		ChatDialog(onDismiss = {})
	}
}
