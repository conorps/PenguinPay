package com.example.penguinpay.compose.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.penguinpay.R
import com.example.penguinpay.viewmodel.Country
import com.example.penguinpay.viewmodel.TransactionViewModel

/**
 * Due to time constraints I kept everything in the one activity and didn't use any
 * fragments. On larger projects I've used a fragment for each screen, with a file
 * for the compose view, and a viewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeTransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel,
    onReviewClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.new_transaction),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
        TextField(
            value = viewModel.firstName,
            label = { Text(stringResource(R.string.first_name)) },
            onValueChange = { firstName -> viewModel.updateFirstName(firstName) },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        TextField(
            value = viewModel.lastName,
            label = { Text(stringResource(R.string.last_name)) },
            onValueChange = { lastName -> viewModel.updateLastName(lastName) },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        val countries = Country.values()
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(Country.UNKNOWN.countryString) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                countries.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.countryString) },
                        onClick = {
                            selectedText = item.countryString
                            viewModel.updateCountry(item)
                            expanded = false
                        }
                    )
                }
            }
        }
        TextField(
            value = viewModel.phoneNumber,
            leadingIcon = { Text(text = viewModel.countryCode) },
            label = { Text(stringResource(R.string.phone_number)) },
            onValueChange = { phoneNumber ->
                if (phoneNumber.length <= viewModel.country.digits) {
                    viewModel.updatePhoneNumber(phoneNumber)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        TextField(
            value = viewModel.binaryAmount,
            label = { Text(stringResource(R.string.amount)) },
            onValueChange = { amount ->
                if (amount.matches("[01]+".toRegex()))
                    viewModel.updateAmount(amount)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        /**
         * TODO: Add loading icon to button
         * Ideally I think we'd have a loading icon when the button is pressed
         * so that we can gather the latest exchange rate and use that on the next screen
         */
        Button(
            onClick = onReviewClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = viewModel.isReviewEnabled()
        ) {
            Text(
                text = "Review",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

