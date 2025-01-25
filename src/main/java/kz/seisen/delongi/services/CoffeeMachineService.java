package kz.seisen.delongi.services;


import kz.seisen.delongi.dto.RecipeDto;
import kz.seisen.delongi.exceptions.CoffeeNotFoundException;
import kz.seisen.delongi.exceptions.MachineUnavailableException;
import kz.seisen.delongi.exceptions.NotEnoughIngredientsException;
import kz.seisen.delongi.models.Ingredients;
import kz.seisen.delongi.models.Recipe;
import kz.seisen.delongi.models.Statistics;
import kz.seisen.delongi.repositories.IngredientsRepository;
import kz.seisen.delongi.repositories.RecipeRepository;
import kz.seisen.delongi.repositories.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CoffeeMachineService {

    private final IngredientsRepository ingredientsRepository;
    private final RecipeRepository recipeRepository;
    private final StatisticsRepository statisticsRepository;

    private final WorkTimeService workTimeService;

    public CoffeeMachineService(IngredientsRepository ingredientsRepository, RecipeRepository recipeRepository, StatisticsRepository statisticsRepository, WorkTimeService workTimeService) {
        this.ingredientsRepository = ingredientsRepository;
        this.recipeRepository = recipeRepository;
        this.statisticsRepository = statisticsRepository;
        this.workTimeService = workTimeService;
    }


    public RecipeDto makeCoffee(String name)  {
        Recipe recipe = recipeRepository.findByName(name);
        List<LocalDate> holidays = workTimeService.getHolidays("KZ");


        if (!workTimeService.isWithinWorkingHours() || workTimeService.isHoliday(holidays)) {
            throw new MachineUnavailableException("Машина в этот промежуток времени не работает");
        }

        if (recipe == null) {
            throw new CoffeeNotFoundException("Coffee not found");
        }

        if(!checkIngredients(recipe)) {
            throw new NotEnoughIngredientsException("Недостаточно ингредиентов для приготовления: " + name);
        }

        updateIngredients(recipe);
        updateStatistics(name);

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName(recipe.getName());
        recipeDto.setWater(recipe.getWater());
        recipeDto.setCoffee(recipe.getCoffee());
        recipeDto.setMilk(recipe.getMilk());


        return recipeDto;


    }

    private boolean checkIngredients(Recipe coffee) {
        return ingredientsRepository.findByName("water").getQuantity() >= coffee.getWater() &&
                ingredientsRepository.findByName("coffee").getQuantity() >= coffee.getCoffee() &&
                ingredientsRepository.findByName("milk").getQuantity() >= coffee.getMilk();
    }

    private void updateIngredients(Recipe drink) {
        Ingredients water = ingredientsRepository.findByName("water");
        Ingredients coffee = ingredientsRepository.findByName("coffee");
        Ingredients milk = ingredientsRepository.findByName("milk");

        water.setQuantity(water.getQuantity() - drink.getWater());
        coffee.setQuantity(coffee.getQuantity() - drink.getCoffee());
        milk.setQuantity(milk.getQuantity() - drink.getMilk());

        ingredientsRepository.save(water);
        ingredientsRepository.save(coffee);
        ingredientsRepository.save(milk);
    }

    private void updateStatistics(String name) {
        Statistics stats = statisticsRepository.findByName(name);
        if (stats == null) {
            stats = new Statistics();
            stats.setName(name);
            stats.setCount(1);
        } else {
            stats.setCount(stats.getCount() + 1);
        }
        statisticsRepository.save(stats);
    }



    public void addIngredient(String name, int quantity) {
        Ingredients ingredient = ingredientsRepository.findByName(name);
        if (ingredient != null) {
            ingredient.setQuantity(ingredient.getQuantity() + quantity);
        } else {
            ingredient = new Ingredients();
            ingredient.setName(name);
            ingredient.setQuantity(quantity);
        }
        ingredientsRepository.save(ingredient);
    }


    public void addNewRecipe(RecipeDto coffee) {
        Recipe recipe = new Recipe();
        recipe.setName(coffee.getName());
        recipe.setWater(coffee.getWater());
        recipe.setCoffee(coffee.getCoffee());
        recipe.setMilk(coffee.getMilk());
        recipeRepository.save(recipe);

    }

    public List<Ingredients> getIngredients() {
        return ingredientsRepository.findAll();
    }



    public String getMostPopularDrink() {
        return statisticsRepository.findAll().stream()
                .max((a, b) -> Integer.compare(a.getCount(), b.getCount()))
                .map(Statistics::getName)
                .orElse("Нет данных");
    }

    public List<Statistics> getStatistics() {
        return statisticsRepository.findAll();
    }




}
