package kz.seisen.delongi.controllers;

import kz.seisen.delongi.dto.RecipeDto;
import kz.seisen.delongi.models.Ingredients;
import kz.seisen.delongi.models.Recipe;
import kz.seisen.delongi.models.Statistics;
import kz.seisen.delongi.services.CoffeeMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coffee")
public class CoffeeMachineController {
    private final CoffeeMachineService coffeeMachineService;

    public CoffeeMachineController(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }


    @PostMapping("/make-coffee")
    public ResponseEntity<RecipeDto> makeCoffee(@RequestParam String name) {
        try {
            RecipeDto recipe = coffeeMachineService.makeCoffee(name);
            return ResponseEntity.ok(recipe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredients>> getIngredients() {
        try {
            List<Ingredients> ingredients = coffeeMachineService.getIngredients();
            return ResponseEntity.ok(ingredients);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @PostMapping("/ingredients/add")
    public ResponseEntity<String> addIngredients(@RequestParam String name, @RequestParam int quantity) {
        try {
            coffeeMachineService.addIngredient(name, quantity);
            return ResponseEntity.ok("Ингредиент добавлен успешно.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @PostMapping("/recipes/add")
    public ResponseEntity<String> addNewRecipe(@RequestBody RecipeDto coffee) {
        try {
            coffeeMachineService.addNewRecipe(coffee);
            return ResponseEntity.ok("Рецепт добавлен успешно.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/statistics/most-popular")
    public ResponseEntity<String> getMostPopularDrink() {
       try {
           String mostPopular = coffeeMachineService.getMostPopularDrink();
           return ResponseEntity.ok("Самый популярный напиток: " + mostPopular);
       } catch (Exception e) {
            throw new RuntimeException(e);
       }
    }



    @GetMapping("/statistics")
    public ResponseEntity<List<Statistics>> getStatistics() {
        try {
            List<Statistics> statistics = coffeeMachineService.getStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






}
