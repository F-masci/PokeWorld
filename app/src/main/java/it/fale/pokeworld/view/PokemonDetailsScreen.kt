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
import it.fale.pokeworld.getBackgroundColorForType
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
    pokemon.type1?.let { getBackgroundColorForType(type = it) }?.let {
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
                    .background(getTextColorForType(type = pokemon.type1))
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
                                getTextColorForType(type = pokemon.type1),
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
                                getTextColorForType(type = pokemon.type1),
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
        Column(){
            Row(){
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
            Row(){
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
            Row(){
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
            it.fale.pokeworld
                .getTextColorForType(type = type)
                .copy(alpha = 0.8f), RoundedCornerShape(30)
        )
        .width(135.dp)
        .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Image(
            painterResource(id = it.fale.pokeworld.getIconForType(type)), "type_icon",
            modifier = Modifier
                .padding(2.dp)
                .height(20.dp))
        Text(stringResource(id = type.resource), fontSize = 12.sp)
    }
}

fun getIconForType(type: PokemonType): Int {
    return when(type){
        PokemonType.GRASS -> R.drawable.grass
        PokemonType.FIGHTING -> R.drawable.fighting
        PokemonType.FLYING -> R.drawable.flying
        PokemonType.POISON -> R.drawable.poison
        PokemonType.GROUND -> R.drawable.ground
        PokemonType.ROCK -> R.drawable.rock
        PokemonType.BUG -> R.drawable.bug
        PokemonType.GHOST -> R.drawable.ghost
        PokemonType.STEEL -> R.drawable.steel
        PokemonType.FIRE -> R.drawable.fire
        PokemonType.WATER -> R.drawable.water
        PokemonType.ELECTRIC -> R.drawable.electric
        PokemonType.PSYCHIC -> R.drawable.psychic
        PokemonType.ICE -> R.drawable.ice
        PokemonType.DRAGON -> R.drawable.dragon
        PokemonType.DARK -> R.drawable.dark
        PokemonType.FAIRY -> R.drawable.fairy
        PokemonType.STELLAR -> R.drawable.stellar
        PokemonType.NORMAL -> R.drawable.normal
        else -> R.drawable.normal
    }
}


@Composable
fun getBackgroundColorForType(type: PokemonType) : Color {
    return when(type){
        PokemonType.GRASS -> colorResource(R.color.grass_light_background)
        PokemonType.FIGHTING -> colorResource(R.color.fighting_light_background)
        PokemonType.FLYING -> colorResource(R.color.flying_light_background)
        PokemonType.POISON -> colorResource(R.color.poison_light_background)
        PokemonType.GROUND -> colorResource(R.color.ground_light_background)
        PokemonType.ROCK -> colorResource(R.color.rock_light_background)
        PokemonType.BUG -> colorResource(R.color.bug_light_background)
        PokemonType.GHOST -> colorResource(R.color.ghost_light_background)
        PokemonType.STEEL -> colorResource(R.color.steel_light_background)
        PokemonType.FIRE -> colorResource(R.color.fire_light_background)
        PokemonType.WATER -> colorResource(R.color.water_light_background)
        PokemonType.ELECTRIC -> colorResource(R.color.electric_light_background)
        PokemonType.PSYCHIC -> colorResource(R.color.psychic_light_background)
        PokemonType.ICE -> colorResource(R.color.ice_light_background)
        PokemonType.DRAGON -> colorResource(R.color.dragon_light_background)
        PokemonType.DARK -> colorResource(R.color.dark_light_background)
        PokemonType.FAIRY -> colorResource(R.color.fairy_light_background)
        PokemonType.STELLAR -> colorResource(R.color.stellar_light_background)
        PokemonType.SHADOW -> colorResource(R.color.shadow_light_background)
        else -> Color.LightGray
    }
}

@Composable
fun getTextColorForType(type: PokemonType) : Color {
    return when(type){
        PokemonType.GRASS -> colorResource(R.color.grass_light_text)
        PokemonType.FIGHTING -> colorResource(R.color.fighting_light_text)
        PokemonType.FLYING -> colorResource(R.color.flying_light_text)
        PokemonType.POISON -> colorResource(R.color.poison_light_text)
        PokemonType.GROUND -> colorResource(R.color.ground_light_text)
        PokemonType.ROCK -> colorResource(R.color.rock_light_text)
        PokemonType.BUG -> colorResource(R.color.bug_light_text)
        PokemonType.GHOST -> colorResource(R.color.ghost_light_text)
        PokemonType.STEEL -> colorResource(R.color.steel_light_text)
        PokemonType.FIRE -> colorResource(R.color.fire_light_text)
        PokemonType.WATER -> colorResource(R.color.water_light_text)
        PokemonType.ELECTRIC -> colorResource(R.color.electric_light_text)
        PokemonType.PSYCHIC -> colorResource(R.color.psychic_light_text)
        PokemonType.ICE -> colorResource(R.color.ice_light_text)
        PokemonType.DRAGON -> colorResource(R.color.dragon_light_text)
        PokemonType.DARK -> colorResource(R.color.dark_light_text)
        PokemonType.FAIRY -> colorResource(R.color.fairy_light_text)
        PokemonType.STELLAR -> colorResource(R.color.stellar_light_text)
        PokemonType.SHADOW -> colorResource(R.color.shadow_light_text)
        else -> Color.Gray
    }
}
