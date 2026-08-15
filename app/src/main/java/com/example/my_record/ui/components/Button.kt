package com.example.my_record.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp? = null,//120.dp,
    height: Dp = 40.dp
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1E3A5F)
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .then(
                if (width != null) {
                    Modifier.width(width)
                } else {
                    Modifier
                }
            )
//            .width(width)
            .height(height)
            .shadow(
                elevation = 8.dp,
                shape = RectangleShape,
                clip = false
            )
            .border(
                width = 4.dp,
                color = Color(0xFFB0B0B0),
                shape = RectangleShape
            )
            .padding(1.dp)
            .border(
                width = 2.dp,
                color = Color(0xFFD8D8D8),
                shape = RectangleShape
            )
            .padding(1.dp)
//            .border(
//                width = 1.dp,
//                color = Color.White.copy(alpha = 0.85f),
//                shape = RectangleShape
//            )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color(0xFFFFFFFF),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}