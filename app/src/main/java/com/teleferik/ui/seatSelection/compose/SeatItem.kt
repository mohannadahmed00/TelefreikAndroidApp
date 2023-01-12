package com.teleferik.ui.seatSelection.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.teleferik.R

@Composable
fun SeatItem(
    seat:SeatItem,
    onClick:() -> Unit
){
    var tint by remember { mutableStateOf(Color(103, 199, 96)) }
    Box(modifier = Modifier
        .fillMaxSize())
    {
        IconButton(onClick = {
            if (seat.status == Status.Available) {
                seat.status = Status.Selected
                tint = Color(255, 193, 7)
                onClick.invoke()
            }else if (seat.status == Status.Selected){
                seat.status = Status.Available
                tint = Color(103, 199, 96)
                onClick.invoke()
            }
        }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_seat),
                contentDescription = "",
                tint =  if (seat.status == Status.Reserved) Color(133, 133, 133) else tint)
            Text(text = seat.number.toString(),
                modifier = Modifier.align(Alignment.Center),
                color = Color.White, fontSize = if (seat.number < 10) 10.sp else 8.sp,
            fontWeight = FontWeight.SemiBold)
        }
    }
}

enum class Status{
    Reserved,
    Available,
    Selected
}


data class SeatItem(
    var number:Int,
    var status: Status = Status.Available
)