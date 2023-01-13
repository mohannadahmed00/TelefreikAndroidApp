package com.teleferik.ui.seatSelection.compose

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.teleferik.models.seats.Status

@ExperimentalFoundationApi
@Composable
fun SeatsUi(){
    val leftList =  listOf(1,2,5,6,9,10,13,14,17,18,21,22,25,26,29,30,33,34,37,38)
    val rightList = listOf(3,4,7,8,11,12,15,16,19,20,23,24,27,28, 31,32,35,36,39,40)
    val bottomList = listOf(41,42,43,44,45)

    val leftSeats = arrayListOf<SeatItem>()
    val rightSeats = arrayListOf<SeatItem>()
    val bottomSeats = arrayListOf<SeatItem>()


    val selectedSeats = remember { mutableStateListOf<Int>() }

    val c = remember { mutableStateOf(true) }

    for (i in 0..44){
        if (i+1 in leftList){
            leftSeats.add(SeatItem(i+1))
        }else if (i+1 in rightList) {
            rightSeats.add(SeatItem(i+1))
        }else {
            bottomSeats.add(SeatItem(i+1))
        }
    }

    leftSeats[3].status = Status.Reserved
    leftSeats[6].status = Status.Reserved
    leftSeats[7].status = Status.Reserved
    rightSeats[5].status = Status.Reserved
    rightSeats[9].status = Status.Reserved
    rightSeats[11].status = Status.Reserved
    rightSeats[12].status = Status.Reserved
    rightSeats[14].status = Status.Reserved
    bottomSeats[3].status = Status.Reserved

    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val columWidth = screenWidth.value.times(0.3)
    val spaceWidth= screenWidth.value.times(0.4)


    Column(
        modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(columWidth.dp)

                ) {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2)
                    ) {
                        itemsIndexed(leftSeats) { _, seat ->
                            SeatItem(
                                seat = seat,
                                onClick = {
                                    if (selectedSeats.contains(seat.number)) selectedSeats.remove(
                                        seat.number
                                    ) else selectedSeats.add(seat.number)
                                    Toast.makeText(
                                        context,
                                        selectedSeats.toList().toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )

                        }
                    }
                }
                Spacer(modifier = Modifier.width(spaceWidth.dp))
                Column(
                    modifier = Modifier
                        .width(columWidth.dp)
                )
                {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2)
                    ) {
                        itemsIndexed(rightSeats) { _, seat ->
                            SeatItem(
                                seat = seat,
                                onClick = {
                                    if (selectedSeats.contains(seat.number)) selectedSeats.remove(
                                        seat.number
                                    ) else selectedSeats.add(seat.number)
                                    Toast.makeText(
                                        context,
                                        selectedSeats.toList().toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )

                        }
                    }
                }
            }
            /*Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            ) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(5)
            ) {
                itemsIndexed(bottomSeats) { _, seat ->
                    SeatItem(
                        seat = seat,
                        onClick = {
                            if (selectedSeats.contains(seat.number)) selectedSeats.remove(seat.number) else selectedSeats.add(seat.number)
                            Toast.makeText(context,selectedSeats.toList().toString(), Toast.LENGTH_SHORT).show()
                        }
                    )

                }
            }
        }*/
        }



}