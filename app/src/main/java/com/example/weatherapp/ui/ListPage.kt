package com.example.weatherapp.ui

import com.example.weatherapp.model.MainViewModel
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.model.City
import com.example.weatherapp.ui.nav.BottomNavItem.Route

@SuppressLint("ContextCastToActivity")
@Composable
fun ListPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val cityList = viewModel.cities
    val activity = LocalContext.current as? Activity
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(cityList, key = { it.name }) { city ->
            LaunchedEffect(city.name) {
                if (city.weather == null) {
                    viewModel.loadWeather(city.name)
                }
            }
            CityItem(
                city = city,
                onClick = {
                    viewModel.city = city
                    viewModel.page = Route.Home
                },
                onClose = {
                    viewModel.remove(city)
                }
            )
        }
    }
}

@Composable
fun CityItem(
    city: City,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = city.weather?.imgUrl,
            modifier = Modifier.size(75.dp),
            error = painterResource(id = R.drawable.loading),
            contentDescription = "Imagem"
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = modifier.weight(1f)) {
            // Nome + ícone de monitoramento (apenas indicador, sem clique)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = city.name,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.size(6.dp))
                val icon = if (city.isMonitored) {
                    Icons.Filled.Notifications
                } else {
                    Icons.Outlined.Notifications
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "Monitorada?",
                    modifier = Modifier.size(20.dp) // menor que no HomePage
                )
            }

            Text(
                text = city.weather?.desc ?: "carregando...",
                fontSize = 16.sp
            )
        }

        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}
