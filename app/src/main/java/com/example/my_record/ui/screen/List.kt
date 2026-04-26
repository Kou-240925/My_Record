package com.example.my_record.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: RecordViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()   // ← これが重要！
    ) {
        Text("ここはList画面")
        // 画面表示時にデータを読み込む
        LaunchedEffect(Unit) {
            viewModel.loadRecords()
        }

        var showDialog by remember { mutableStateOf(false) }
        var deleteTargetId by remember { mutableStateOf<Int?>(null) }
        val records = viewModel.recordList

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(records) { record ->
                val bgColor = when (record.rating) {
                    "◎" -> Color(0xFFCCFFCC) // 黄緑（やや薄め）
                    "〇" -> Color.White
                    "△" -> Color(0xFFFFFFCC) // 薄黄色
                    else -> Color.White
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp), // ← カード同士の間隔
                    elevation = CardDefaults.cardElevation(1.dp), // ← 影
                    colors = CardDefaults.cardColors(
                        containerColor = bgColor
                    )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(record.title, fontSize = 20.sp)
                        },
                        supportingContent = {
                            Column {
//                                Text(record.content)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "カテゴリ：${record.category}",
                                        fontSize = 18.sp,
//                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "評価：${record.rating}",
                                        fontSize = 18.sp,
//                                        modifier = Modifier.weight(1f)
                                    )
//                                Text("日付：${formatDate(record.date)}")
                                }
                            }
                        },
                        trailingContent = {
                            Button(
                                onClick = {
//                                        viewModel.deleteRecord(record.id)
                                    deleteTargetId = record.id
                                    showDialog = true
                                }
                            ) {
                                Text("削除")
                            }
                        },
                        colors = ListItemDefaults.colors( // ← これが重要
                            containerColor = bgColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                navController.navigate("detail/${record.id}")
                            }
                            .statusBarsPadding()
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            onClick = {
                navController.navigate("create")
            }
        ) {
            Text("記録へ")
        }

        if (showDialog && deleteTargetId != null) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text("確認") },
                text = { Text("本当に削除しますか？") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteRecord(deleteTargetId!!)
                            showDialog = false
                            deleteTargetId = null
                        }
                    ) {
                        Text("削除する")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            deleteTargetId = null
                        }
                    ) {
                        Text("キャンセル")
                    }
                }
            )
        }
    }
}