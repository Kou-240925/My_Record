package com.example.my_record.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.my_record.RecordViewModel


@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: RecordViewModel,
    id: Int
) {
    val record = viewModel.getRecordById(id)
    if (record == null) {
        Text(
            "データが見つかりません",
            modifier = Modifier
                .padding(16.dp)
                .statusBarsPadding()
        )
        return
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .statusBarsPadding()
    ) {

        LaunchedEffect(id) {
            viewModel.loadRecords()
        }
//        Text("詳細画面")
        Text("タイトル：${record.title}", fontSize = 22.sp)//タイトル
        Divider(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            thickness = 1.dp
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("カテゴリ：${record.category}", fontSize = 20.sp,modifier = Modifier.weight(1f))//カテゴリ
            Text("評価：${record.rating}", fontSize = 20.sp,modifier = Modifier.weight(1f))//評価

        }
        Text("最終保存日：${formatDate(record.date)}")//日付
        Divider(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            thickness = 1.dp
        )
        Text("内容：${record.content}", fontSize = 20.sp)//内容


        Row {
            Button(
                onClick = {
                    navController.navigate("edit/${record.id}")
                }
            ) {
                Text("編集")
            }
            Button(onClick = { navController.popBackStack() }) {
                Text("戻る")
            }

        }

    }
}