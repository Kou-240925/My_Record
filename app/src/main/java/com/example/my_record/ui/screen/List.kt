package com.example.my_record.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.my_record.ui.components.AppButton

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
//        Text("ここはList画面")
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
                    "◎" -> Color.White//(0xFFCCFFCC) // 黄緑（やや薄め）
                    "〇" -> Color.White
                    "△" -> Color.White//(0xFFFFFFCC) // 薄黄色
                    "評価待ち" -> Color(0xFFFFFFCC)
                    else -> Color.White
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = false
                        )
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            navController.navigate("detail/${record.id}")
                        }
                        .height(120.dp),   // ← 高さ固定

                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // 左側：タイトル + 情報
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = record.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("カテゴリ：${record.category}", fontSize = 16.sp)
                            Text("成功確率：${record.successRate}%", fontSize = 16.sp)
                        }

                        // 右側：ボタン縦並び
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            AppButton(
                                text = "評価",
                                onClick = {
                                    navController.navigate("assessment/${record.id}")
                                },
                                width = 120.dp,
                                modifier = Modifier.height(36.dp)
                            )

                            AppButton(
                                text = "削除",
                                onClick = {
                                    deleteTargetId = record.id
                                    showDialog = true
                                },
                                width = 120.dp,
                                modifier = Modifier.height(36.dp)
                            )
                        }
                    }
                }

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppButton(
                text = "ホーム画面へ",
                onClick = {
                    navController.navigate("home")
                },
                modifier = Modifier.navigationBarsPadding()
            )
            AppButton(
                text = "記録へ",
                onClick = {
                    navController.navigate("create")
                }
            )
        }

        if (showDialog && deleteTargetId != null) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text("確認") },
                text = { Text("本当に削除しますか？") },
                confirmButton = {
                    AppButton(
                        text = "削除する",
                        onClick = {
                            viewModel.deleteRecord(deleteTargetId!!)
                            showDialog = false
                            deleteTargetId = null
                        },
                        modifier = Modifier.navigationBarsPadding()
                    )
                },
                dismissButton = {
                    AppButton(
                        text = "キャンセル",
                        onClick = {
                            showDialog = false
                            deleteTargetId = null
                        },
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            )
        }
    }
}