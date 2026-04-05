package com.example.my_record.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_record.RecordViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}
@Composable
fun ListScreen(
    navController: NavController,
    viewModel: RecordViewModel
) {
    Text("ここはList画面")
    // 画面表示時にデータを読み込む
    LaunchedEffect(Unit) {
        viewModel.loadRecords()
    }

    val records = viewModel.recordList

    LazyColumn {
        items(records) { record ->
            ListItem(
                headlineContent = {
                    Text(record.title)
                },
                supportingContent = {
                    Column {
                        Text(record.content)
                        Text("カテゴリ：${record.category}")
                        Text("評価：${record.rating}")
//                        Text("日付：${record.date}")
                        Text("日付：${formatDate(record.date)}")

                        Button(
                            onClick = {
                                viewModel.deleteRecord(record.id)
                            }
                        ) {
                            Text("削除")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),              // 画面全体を使う
        verticalArrangement = Arrangement.Bottom, // 下に寄せる
        horizontalAlignment = Alignment.CenterHorizontally // 中央揃え

    ) {
        Button(
            onClick = {
                navController.navigate("create")
            }
        ) {
            Text("記録へ")
        }
    }
}