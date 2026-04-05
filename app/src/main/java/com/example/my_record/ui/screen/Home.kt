package com.example.my_record.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_record.RecordViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: RecordViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),              // 画面全体を使う
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // 中央揃え

    ) {
        Text("ここはHome画面です。")
        Divider(thickness = 1.dp)
        Text(
            text = "今日の判断を記録しましょう",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Divider(thickness = 1.dp)

        Button(
            onClick = {
                navController.navigate("create")
            }
        ) {
            Text("記録へ")
        }
    }
}