package com.teleferik.ui.seatSelection.compose

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.teleferik.R

@ExperimentalFoundationApi
@Composable
fun SeatsUi(onConfirmClick:() -> Unit){
    val leftList =  listOf(1,2,5,6,9,10,13,14,17,18,21,22,25,26,29,30,33,34,37,38)
    val rightList = listOf(3,4,7,8,11,12,15,16,19,20,23,24,27,28, 31,32,35,36,39,40)
    val bottomList = listOf(41,42,43,44,45)

    val leftSeats = arrayListOf<SeatItem>()
    val rightSeats = arrayListOf<SeatItem>()
    val bottomSeats = arrayListOf<SeatItem>()

    var selectedSeat by remember { mutableStateOf(0) }


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

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6F)
            .padding(bottom = 5.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)

            ) {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2)
                ) {
                    itemsIndexed(leftSeats) { _, seat ->
                        SeatItem(
                            seat = seat,
                            onClick = {
                                selectedSeat = seat.number
                                Toast.makeText(context,selectedSeat.toString(), Toast.LENGTH_SHORT).show()
                            }
                        )

                    }
                }
            }
            Spacer(modifier = Modifier.width(100.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            )
            {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2)
                ) {
                    itemsIndexed(rightSeats) { _, seat ->
                        SeatItem(
                            seat = seat,
                            onClick = {
                                selectedSeat = seat.number
                                Toast.makeText(context,selectedSeat.toString(), Toast.LENGTH_SHORT).show()
                            }
                        )

                    }
                }
            }
        }

        Row(modifier = Modifier
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
                            selectedSeat = seat.number
                        }
                    )

                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            ){
            Button(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(R.color.base_app_color),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                onConfirmClick.invoke()
            }) {
                Text(text = context.getString(R.string.confirm), textAlign = TextAlign.Center,
                    fontSize = 16.sp ,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 5.dp))
            }
        }
    }
    /*ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        ) {
        val (aboveRow, secondRow,bottomRow) = createRefs()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .constrainAs(ref = aboveRow, constrainBlock = {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)

            ) {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2)
                ) {
                    itemsIndexed(leftSeats) { _, seat ->
                        SeatItem(
                            seat = seat,
                            onClick = {
                                selectedSeat = seat.number
                                Toast.makeText(context,selectedSeat.toString(), Toast.LENGTH_SHORT).show()
                            }
                        )

                    }
                }
            }
            Spacer(modifier = Modifier.width(100.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            )
            {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2)
                ) {
                    itemsIndexed(rightSeats) { _, seat ->
                        SeatItem(
                            seat = seat,
                            onClick = {
                                selectedSeat = seat.number
                                Toast.makeText(context,selectedSeat.toString(), Toast.LENGTH_SHORT).show()
                            }
                        )

                    }
                }
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            .constrainAs(secondRow) {
                top.linkTo(aboveRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            LazyVerticalGrid(
                cells = GridCells.Fixed(5)
            ) {
                itemsIndexed(bottomSeats) { _, seat ->
                    SeatItem(
                        seat = seat,
                        onClick = {
                            selectedSeat = seat.number
                        }
                    )

                }
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            .constrainAs(bottomRow)
            {
                top.linkTo(secondRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }){
            Button(onClick = {
                onConfirmClick.invoke()
            }) {
                Text(text = "تأكيد حجز المقاعد", textAlign = TextAlign.Center,
                fontSize = 14.sp , color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .background(color = Color.Blue, shape = CutCornerShape(5.dp))
                    )
            }
        }
    }*/

}