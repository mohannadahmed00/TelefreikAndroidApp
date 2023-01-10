package com.teleferik.ui.seatSelection.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Box(modifier = Modifier
        .fillMaxSize())
    {
        IconButton(onClick = {
            if (seat.status == Status.Available) {
                onClick.invoke()
            }
        }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_seat),
                contentDescription = "", tint =  if (seat.status == Status.Available) Color.Red.copy(alpha = 0.5F) else Color.Gray)
            Text(text = seat.number.toString(),
                modifier = Modifier.align(Alignment.Center),
                color = Color.White, fontSize = if (seat.number < 10) 10.sp else 8.sp,
            fontWeight = FontWeight.SemiBold)
        }
    }
}

enum class Status{
    Reserved,
    Available
}


data class SeatItem(
    var number:Int,
    var status: Status = Status.Available
)