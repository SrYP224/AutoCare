package com.example.autocare.ui.screens.mantenimientos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.autocare.data.Mantenimiento
import com.example.autocare.ui.components.AutoCareScaffold
import com.example.autocare.ui.components.AutoCareTab
import com.example.autocare.ui.components.navigateToTab
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.autocare.data.FakeData

@Composable
fun MantenimientosScreen(navController: NavHostController) {

    AutoCareScaffold(
        title = "AutoCare",
        currentTab = AutoCareTab.MANTENIMIENTOS,
        onTabSelected = { tab ->
            navigateToTab(navController, tab)
        },
        showFab = true
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Text(
                text = "Mis mantenimientos",
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp
                ),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF18317E)
            )

            Text(
                text = "Historial de mantenimiento de tu vehículo",
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 4.dp,
                    end = 20.dp,
                    bottom = 16.dp
                ),
                fontSize = 14.sp,
                color = Color.Gray
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 90.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = FakeData.mantenimientos,
                    key = { mantenimiento -> mantenimiento.id }
                ){mantenimiento ->

                    MantenimientoCard(
                        mantenimiento = mantenimiento
                    )
                }
            }
        }
    }
}
@Composable
fun MantenimientoCard(
    mantenimiento: Mantenimiento
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "${mantenimiento.emoji}  ${mantenimiento.tipo}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1C)
            )

            Text(
                text = "Fecha: ${mantenimiento.fecha}",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "Kilometraje: ${mantenimiento.kilometraje} km",
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "Próximo mantenimiento: ${mantenimiento.proximoKilometraje} km",
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "$${mantenimiento.costo}",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF18317E)
            )
        }
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MantenimientosScreenPreview() {
    MantenimientosScreen(
        navController = rememberNavController()
    )
}