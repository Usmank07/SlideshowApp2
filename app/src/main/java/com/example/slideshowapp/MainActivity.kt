package com.example.slideshowapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class Slide(val imageRes: Int, val caption: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SlideshowApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideshowApp() {
    val context = LocalContext.current

    // Change these to match YOUR drawable names (must be lowercase)
    val slides = remember {
        listOf(
            Slide(R.drawable.arsenal_logo, "Arsenal"),
            Slide(R.drawable.barca_logo, "Barcelona"),
            Slide(R.drawable.bayern_logo, "Bayern Munich"),
            Slide(R.drawable.liverpool_logo, "Liverpool"),
            Slide(R.drawable.madrid_logo, "Real Madrid")
        )
    }

    var index by remember { mutableIntStateOf(0) } // 0-based index
    var jumpText by remember { mutableStateOf("") } // user types 1..N

    fun goNext() {
        index = (index + 1) % slides.size
    }

    fun goBack() {
        index = if (index == 0) slides.size - 1 else index - 1
    }

    fun goTo(n1Based: Int) {
        if (n1Based in 1..slides.size) {
            index = n1Based - 1
        } else {
            Toast.makeText(context, "Enter a number from 1 to ${slides.size}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Slideshow (${index + 1}/${slides.size})") }) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // IMAGE (same size every time)
                Image(
                    painter = painterResource(slides[index].imageRes),
                    contentDescription = slides[index].caption,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(Modifier.height(12.dp))

                // CAPTION
                Text(
                    text = slides[index].caption,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(20.dp))

                // BACK / NEXT (wrap-around)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { goBack() }) { Text("Back") }
                    Button(onClick = { goNext() }) { Text("Next") }
                }

                Spacer(Modifier.height(20.dp))

                // JUMP TO IMAGE NUMBER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = jumpText,
                        onValueChange = { jumpText = it },
                        label = { Text("Picture # (1..${slides.size})") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(12.dp))

                    Button(onClick = {
                        val n = jumpText.trim().toIntOrNull()
                        if (n == null) {
                            Toast.makeText(context, "Type a valid number", Toast.LENGTH_SHORT).show()
                        } else {
                            goTo(n)
                        }
                    }) {
                        Text("Go")
                    }
                }
            }
        }
    }
}