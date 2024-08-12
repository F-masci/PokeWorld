package it.fale.pokeworld.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailsScreen (
    navController: NavController,
    pokemonDetailViewModel: PokemonDetailViewModel
)
{

    val pokemon = pokemonDetailViewModel.pokemon.collectAsStateWithLifecycle().value
    val isDetailsLoaded = pokemonDetailViewModel.detailsLoaded.collectAsStateWithLifecycle().value

    if(isDetailsLoaded)
        DetailsCard(pokemon)
    else
        Loader()

}

@Composable
fun Loader() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Loading...")
    }
}

@Composable
fun DetailsCard(pokemon: PokemonEntity){
    pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = it
        ) {
            val spriteUrl = pokemon.spriteDefault
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = pokemon.name, modifier = Modifier
                    .background(colorResource(pokemon.type1.textColor))
                    .fillMaxWidth()
                    .padding(20.dp),
                    textAlign = TextAlign.Center)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    TypeRow(type = pokemon.type1)
                    if(pokemon.type2 != null){
                        Spacer(modifier = Modifier.width(20.dp))
                        TypeRow(type = pokemon.type2)
                    }
                }
                AsyncImage(
                    model = spriteUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(5))
                        .width(350.dp)
                        .height(300.dp)
                )
                Row(
                    modifier = Modifier.padding(0.dp, 15.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(text = "Height: ${pokemon.height}",
                        modifier = Modifier
                            .background(
                                colorResource(id = pokemon.type1.textColor),
                                RoundedCornerShape(15)
                            )
                            .width(170.dp)
                            .padding(15.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Weight: ${pokemon.weight}",
                        modifier = Modifier
                            .background(
                                colorResource(id = pokemon.type1.textColor),
                                RoundedCornerShape(15)
                            )
                            .width(170.dp)
                            .padding(15.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp)
                }
                Spacer(Modifier.height(10.dp))
                StatsSection(pokemon = pokemon)
            }
        }
    }
}

@Composable
fun AbilitiesSection(){

}

@Composable
fun StatsSection(pokemon: PokemonEntity){

    Column(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.6f))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
       Text(text = "STATS", fontSize = 14.sp, modifier = Modifier.padding(15.dp))
        Column {
            Row {
                Text(text = "HP: ${pokemon.hp}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.hp),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Attack: ${pokemon.attack}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.attack),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Defense: ${pokemon.defense}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.defense),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Speed: ${pokemon.speed}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.speed),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Special Defense: ${pokemon.specialDefense}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.special_defense),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Special attack: ${pokemon.specialAttack}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.special_attack),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
fun TypeRow(type: PokemonType) {

    Row (modifier = Modifier
        .background(
            colorResource(type.textColor)
                .copy(alpha = 0.8f),
            RoundedCornerShape(30)
        )
        .width(135.dp)
        .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Image(
            painterResource(id = type.icon), "type_icon",
            modifier = Modifier
                .padding(2.dp)
                .height(20.dp))
        Text(stringResource(id = type.string), fontSize = 12.sp)
    }
}
