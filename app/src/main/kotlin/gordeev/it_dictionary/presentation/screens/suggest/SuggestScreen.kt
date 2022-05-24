package gordeev.it_dictionary.presentation.screens.suggest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gordeev.it_dictionary.R
import gordeev.it_dictionary.presentation.screens.suggest.ErrorType.EMPTY_INPUT
import gordeev.it_dictionary.presentation.screens.suggest.ErrorType.NETWORK
import gordeev.it_dictionary.presentation.theme.Bg20
import gordeev.it_dictionary.presentation.theme.Text20
import gordeev.it_dictionary.presentation.ui.TextButton
import gordeev.it_dictionary.presentation.ui.TextButtonColors.Primary
import gordeev.it_dictionary.presentation.utils.rememberStateWithLifecycle
import gordeev.it_dictionary.presentation.utils.screenEdgeOffsetHorizontal

@Preview
@Composable
fun SuggestScreen() {
    SuggestScreen(hiltViewModel())
}

@Composable
private fun SuggestScreen(
    viewModel: SuggestViewModel
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)
    SuggestScreen(
        viewState = viewState,
        onWordFieldValueChange = { viewModel.onTermNameChanged(it) },
        termSetOnValueChange = { viewModel.onTermSetChanged(it) },
        onTermSetOptionClicked = { viewModel.onTermSetChanged(it) },
        onMeaningFieldValueChange = { viewModel.onTermMeaningChanged(it) },
        onBottomButtonClick = { viewModel.onBottomButtonClick() }
    )
}

@Composable
private fun SuggestScreen(
    viewState: SuggestViewState,
    onWordFieldValueChange: (String) -> Unit,
    termSetOnValueChange: (String) -> Unit,
    onTermSetOptionClicked: (String) -> Unit,
    onMeaningFieldValueChange: (String) -> Unit,
    onBottomButtonClick: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val networkErrorMessage = stringResource(R.string.suggest_screen_network_error_message)
    val emptyInputErrorMessage = stringResource(R.string.suggest_screen_empty_input_error_message)
    val successMessage = stringResource(R.string.suggest_screen_success_message)
    val isAnyInputEmpty = viewState.errorType == EMPTY_INPUT
    LaunchedEffect(viewState.errorType, viewState.isSuccess) {
        if (viewState.errorType == NETWORK) {
            snackBarHostState.showSnackbar(networkErrorMessage)
        }
        if (isAnyInputEmpty) {
            snackBarHostState.showSnackbar(emptyInputErrorMessage)
        }
        if (viewState.isSuccess) {
            snackBarHostState.showSnackbar(successMessage)
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenEdgeOffsetHorizontal),
        scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState)
    ) {
        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.suggest_screen_title),
                style = MaterialTheme.typography.h5
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(id = R.string.suggest_screen_help_text),
                color = Text20
            )
            TextField(
                value = viewState.termName,
                onValueChange = onWordFieldValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.suggest_screen_word_field_label_text))
                },
                shape = MaterialTheme.shapes.small,
                colors = textFieldColors,
            )
            TextFieldWithDropdown(
                viewState.termSetName,
                termSetOnValueChange,
                viewState.termSetsOptions,
                onTermSetOptionClicked
            )
            TextField(
                value = viewState.termMeaning,
                onValueChange = onMeaningFieldValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = {
                    Text(text = stringResource(id = R.string.suggest_screen_meaning_field_label_text))
                },
                shape = MaterialTheme.shapes.small,
                colors = textFieldColors,
            )
            TextButton(
                onClick = { if (viewState.isLoading.not()) onBottomButtonClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = Primary,
                isLoading = viewState.isLoading,
                textRes = R.string.suggest_screen_button
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TextFieldWithDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    onOptionClicked: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        }
    ) {
        TextField(
            value = value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange,
            singleLine = true,
            label = {
                Text(text = stringResource(id = R.string.suggest_screen_set_field_label_text))
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = MaterialTheme.shapes.small,
            colors = textFieldColors
        )
        ExposedDropdownMenu(
            expanded = expanded && options.isNotEmpty(),
            modifier = Modifier.exposedDropdownSize(),
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onOptionClicked(selectionOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

private val textFieldColors: TextFieldColors
    @Composable
    get() = TextFieldDefaults.textFieldColors(
        backgroundColor = Bg20,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedLabelColor = Text20.copy(alpha = ContentAlpha.high),
        unfocusedLabelColor = Text20.copy(alpha = ContentAlpha.medium)
    )