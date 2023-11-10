package com.example.penguinpay.compose.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.penguinpay.viewmodel.TransactionViewModel

@Composable
fun ReviewTransactionScreen(
    viewModel: TransactionViewModel,
    onSubmitClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Recipient: " + viewModel.firstName + " " + viewModel.lastName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Phone Number: " + viewModel.formattedPhoneNumber,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Country: " + viewModel.country.countryString,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Amount: " + viewModel.binaryAmount,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Amount received in local currency: " +
                    String.format("%.2f", viewModel.exchangeAmount),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onSubmitClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Submit",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}