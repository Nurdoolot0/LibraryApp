package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryApp()
        }
    }
}

@Composable
fun LibraryApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "book_list",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("book_list") { BookListScreen(navController) }
            composable("book_details/{title}/{author}/{description}") { backStackEntry ->
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val author = backStackEntry.arguments?.getString("author") ?: ""
                val description = backStackEntry.arguments?.getString("description") ?: ""
                BookDetailsScreen(title, author, description)
            }
            composable("favorites") { FavoritesScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("book_list" to "Книги", "favorites" to "Избранное", "profile" to "Профиль")

    BottomNavigation {
        items.forEach { (route, title) ->
            val selected = navController.currentBackStackEntryAsState().value?.destination?.route == route
            BottomNavigationItem(
                selected = selected,
                onClick = { navController.navigate(route) },
                label = { Text(text = title) },
                icon = {},
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
fun BookListScreen(navController: NavHostController) {
    val books = remember {
        listOf(
            Book(
                "Jamila",
                "Chyngyz Aitmatov",
                "The main part of the film takes place during the Great Patriotic War in what is now Kyrgyzstan. Jamilya, a young woman, follows her parents orders by marrying Sadik, a man who she does not love. After marrying Jamilya, Sadik lives with her for only four months, then he is taken to the front, to fight in World War II.",
                R.drawable.jamila
            ),
            Book(
                "Evgeniy Onegin",
                "Pushkin",
                "Evgeniy Onegin\", \"Pushkin\", \"The story. Eugene Onegin is a young, rich Russian aristocrat. Although educated and smart, he is superficial and indifferent to everything.",
                R.drawable.evgen
            ),
            Book(
                "Rich & Poor Dad",
                "Robert Kiyosaki",
                "Rich & Poor Dad\", \"Robert Kiyosaki\", \"Rich Dad, Poor Dad by Robert T. Kiyosaki is a personal finance book that emphasizes the importance of financial education, teaches how to make money work for you, and challenges traditional beliefs about money.",
                R.drawable.rich
            )
        )
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(books) { book ->
            BookItem(book) {
                navController.navigate(
                    "book_details/${book.title}/${book.author}/${book.description}"
                )
            }
        }
    }
}

@Composable
fun BookDetailsScreen(title: String, author: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = author, fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 16.dp))
        Text(text = description, fontSize = 16.sp, modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
fun FavoritesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.serdse),
                contentDescription = "Heart Icon",
                modifier = Modifier
                    .size(350.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "Избранное",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Profile",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 100.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.spider),
            contentDescription = "Profile Picture",
            modifier = Modifier.padding(bottom = 30.dp)
                .size(170.dp)
                .background(Color.Gray, CircleShape)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Spider-Man",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 300.dp),
            fontWeight = FontWeight.Bold

        )

        Button(
            onClick = { },
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Text("Edit Profile")
        }
    }
}

@Composable
fun BookItem(book: Book, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = book.imageResId),
            contentDescription = book.title,
            modifier = Modifier
                .size(120.dp)
                .padding(end = 16.dp),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 8.dp)
        ) {
            Text(
                text = book.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = book.author,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

data class Book(
    val title: String,
    val author: String,
    val description: String,
    val imageResId: Int
)

