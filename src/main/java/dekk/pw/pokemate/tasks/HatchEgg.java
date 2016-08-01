package dekk.pw.pokemate.tasks;

import com.pokegoapi.api.pokemon.HatchedEgg;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import dekk.pw.pokemate.Context;
import dekk.pw.pokemate.PokeMateUI;
import dekk.pw.pokemate.util.Time;
import javafx.scene.image.Image;

import java.util.List;

class HatchEgg extends Task implements Runnable{
    HatchEgg(final Context context) {
        super(context);
    }

    @Override
    public void run() {
        try {
            List<HatchedEgg> eggs = context.getApi().getInventories().getHatchery().queryHatchedEggs();
            Time.sleepRate();
            eggs.forEach(egg -> {
                Pokemon hatchedPokemon = null;
                try {
                    hatchedPokemon = context.getApi().getInventories().getPokebank().getPokemonById(egg.getId());
                    String details = String.format("candy: %s  exp: %s  stardust: %s", egg.getCandy(), egg.getExperience(), egg.getStardust());
                    if (hatchedPokemon == null) {
                        PokeMateUI.toast("Hatched egg " + egg.getId() + " " + details, "Hatched egg!", "icons/items/egg.png");
                    } else {
                        PokeMateUI.toast("Hatched " + hatchedPokemon.getPokemonId() + " with " + hatchedPokemon.getCp() + " CP " + " - " + details,
                            "Hatched egg!",
                            "icons/items/egg.png");
                    }
                }catch (LoginFailedException | RemoteServerException e) {
                    e.printStackTrace();
                }
            });
        } catch (LoginFailedException | RemoteServerException e) {
            e.printStackTrace();
        }
        context.addTask(new HatchEgg(context));
    }
}
