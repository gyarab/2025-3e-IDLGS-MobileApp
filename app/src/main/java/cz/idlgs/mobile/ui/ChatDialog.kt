package cz.idlgs.mobile.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownTable
import com.mikepenz.markdown.m3.Markdown
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import cz.idlgs.mobile.BuildConfig
import cz.idlgs.mobile.data.remote.dto.Role
import cz.idlgs.mobile.nav.ChatNavGraph
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.viewmodel.ChatViewModel

@Destination<ChatNavGraph>(start = true)
@Composable
fun ChatDialog(
	navigator: DestinationsNavigator,
	viewModel: ChatViewModel = hiltViewModel()
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
		onDismissRequest = { navigator.navigateUp() },
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
						.padding(16.dp, 8.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = if (BuildConfig.DEBUG) "Chat" else "AI Assistant",
						style = MaterialTheme.typography.titleLarge
					)
					IconButton(onClick = { navigator.navigateUp() }) {
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
								color = if (isUser) MaterialTheme.colorScheme.primaryContainer
								else MaterialTheme.colorScheme.surfaceVariant,
								shape = RoundedCornerShape(
									topStart = 16.dp,
									topEnd = 16.dp,
									bottomStart = if (isUser) 16.dp else 0.dp,
									bottomEnd = if (isUser) 0.dp else 16.dp
								),
								modifier = Modifier
									.widthIn(max = if (isUser) message.content.length.dp else Dp.Unspecified)
									.wrapContentWidth()
							) {
								Box(modifier = Modifier.padding(12.dp)) {
									Markdown(
										content = message.content,
										components = markdownComponents(
											table = { tableData ->
												Box(
													modifier = Modifier
														.horizontalScroll(rememberScrollState())
														.padding(vertical = 8.dp)
												) {
													MarkdownTable(
														content = tableData.content,
														node = tableData.node,
														style = MaterialTheme.typography.bodyLarge
													)
												}
											}
										)
									)
								}
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
							.onKeyEvent {
								if (it.key == Key.Enter) {
									if (text.isNotBlank()) {
										viewModel.sendMessage(text.trim())
										text = ""
									} else return@onKeyEvent false
									true
								} else false
							}
							.focusRequester(focusRequester),
						shape = MaterialTheme.shapes.medium,
						placeholder = { Text("Ask something...") },
						maxLines = 4,
					)
					Spacer(modifier = Modifier.width(8.dp))
					IconButton(
						onClick = {
							viewModel.sendMessage(text.trim())
							keyboardController!!.hide()
							text = ""
						},
						enabled = text.isNotBlank() && !isLoading,
					) {
						Icon(
							imageVector = Icons.AutoMirrored.Default.Send,
							contentDescription = "Send",
//							tint = MaterialTheme.colorScheme.primary
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
		ChatDialog(EmptyDestinationsNavigator)
	}
}
