package gordeev.it_dictionary.presentation.screens.suggest

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import gordeev.it_dictionary.R

@Preview
@Composable
fun SuggestScreen() {
    val onWordFieldValueChange: (String) -> Unit = {}
    val wordValue = ""
    val onSetFieldValueChange: (String) -> Unit = {}
    val setValue = ""
    val onMeaningFieldValueChange: (String) -> Unit = {}
    val meaningValue = ""
    Column {
        Text(text = stringResource(id = R.string.suggest_screen_title))
        Text(text = stringResource(id = R.string.suggest_screen_help_text))
        TextField(
            value = wordValue,
            onValueChange = onWordFieldValueChange,
            label = {
                Text(text = stringResource(id = R.string.suggest_screen_word_field_label_text))
            }
        )
        TextFieldWithDropdown()
        TextField(
            value = meaningValue,
            onValueChange = onMeaningFieldValueChange,
            label = {
                Text(text = stringResource(id = R.string.suggest_screen_meaning_field_label_text))
            }
        )
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.suggest_screen_button))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextFieldWithDropdown() {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            label = {
                Text(text = stringResource(id = R.string.suggest_screen_set_field_label_text))
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}