package com.example.my_record.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.my_record.RecordViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    navController: NavController,
    viewModel: RecordViewModel,
    id: Int
) {
    val record = viewModel.getRecordById(id)

    if (record == null) {
        Text("データが見つかりません")
        return
    }

    // ★ TextField の状態
    var titleState by remember { mutableStateOf(record.title) }
    var contentState by remember { mutableStateOf(record.content) }

    // ★ プルダウン①：カテゴリ
    val categories = listOf("投資", "学習", "生活")
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(record.category) }

    // ★ プルダウン②：種類（評価）
    val types = listOf("◎", "〇", "△")
    var expandedType by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(record.rating) }

    Column(modifier = Modifier.padding(16.dp).statusBarsPadding()) {

        Text("編集画面")

        // ▼ タイトル
        TextField(
            value = titleState,
            onValueChange = { titleState = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("タイトル") }
        )

        // ▼ 内容
        TextField(
            value = contentState,
            onValueChange = { contentState = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            placeholder = { Text("内容") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ▼ プルダウン①：カテゴリ
        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = { expandedCategory = !expandedCategory }
        ) {
            TextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("カテゴリ") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ▼ プルダウン②：評価
        ExposedDropdownMenuBox(
            expanded = expandedType,
            onExpandedChange = { expandedType = !expandedType }
        ) {
            TextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("評価") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            expandedType = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ▼ 更新ボタン
        Button(
            onClick = {
                viewModel.updateRecord(
                    record.copy(
                        title = titleState,
                        content = contentState,
                        category = selectedCategory,
                        rating = selectedType
                    )
                )
                navController.popBackStack()
            }
        ) {
            Text("更新")
        }
    }
}