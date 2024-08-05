import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.Ingredients;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTests extends BaseUserTest {


    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа авторизированного пользователя")
    public void checkCreateOrderWithLogIn() {
        setupToken();
        Ingredients ingredients = new Ingredients(generateListIngredients());

        createOrder(token, ingredients)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("name", notNullValue());
    }

    @Test //Сервер позволяет создавать заказы без авторизации
    @DisplayName("Попытка создания заказа без авторизации")
    @Description("Создание заказа не авторизированного пользователя")
    public void checkCreateOrderWithoutLogIn() {
        Ingredients ingredients = new Ingredients(generateListIngredients());

        createOrder(null, ingredients)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Попытка создания заказа без ингредиентов")
    @Description("Создание заказа авторизированного пользователя, в теле запроса отсутствуют ингредиенты")
    public void checkCreateOrderWithoutIngredients() {
        setupToken();
        Ingredients ingredients = new Ingredients();

        createOrder(token, ingredients)
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Попытка создания заказа с некорректным хешем")
    @Description("Создание заказа авторизированного пользователя, в теле запроса хеш ингредиента не корректен")
    public void checkCreateOrderWrongHash() {
        setupToken();

        List<String> wrongHashList = new ArrayList<>();
        wrongHashList.add("sdfdsf");

        Ingredients ingredients = new Ingredients(wrongHashList);

        createOrder(token, ingredients)
                .then()
                .assertThat()
                .statusCode(500);
    }
}