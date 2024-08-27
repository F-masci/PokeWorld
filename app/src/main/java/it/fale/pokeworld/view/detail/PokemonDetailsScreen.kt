package it.fale.pokeworld.view.detail

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.viewmodel.PokemonDetailViewModel

/**
 * Composable per la creazione della pagina di dettaglio del Pokemon.
 *
 * @param pokemonDeatilViewModel Il ViewModel per la il dettaglio dei Pokemon.
 * @param pokemonId L'identificativo del pokemon di cui mostrare la pagina dettaglio.
 */
@Composable
fun PokemonDetailsScreen (
    pokemonDetailViewModel: PokemonDetailViewModel,
    pokemonId: Int
)
{

    val pokemon: PokemonEntity? = pokemonDetailViewModel.pokemon.collectAsStateWithLifecycle().value
    val isDetailsLoaded = pokemonDetailViewModel.detailsLoaded.collectAsStateWithLifecycle().value

    if(!isDetailsLoaded || pokemon?.id != pokemonId) pokemonDetailViewModel.loadPokemon(pokemonId)

    if(isDetailsLoaded)
        DetailsCard(pokemonDetailViewModel, pokemon!!)
    else
        Loader()

}
