import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Client;
import models.Ingredients;
import org.junit.Before;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class BaseUserTest extends BaseTest {

    private static final String INGREDIENTS_API = "/ingredients";
    private static final String ORDER_API = "/orders";

    @Before
    @DisplayName("Инициализация нового пользователя для теста")
    @Description("Инициализация логина, пароля и почтового адреса, создание пользователя")
    public void initTest() {
        super.initTest();
        createUser(email, password, name);
    }

    @Step("Изменение информации о пользователе")
    protected Response changeUserData(String token, String email, String name) {
        Client client = new Client(email, null, name);

        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(client)
                .patch(USER_API);
    }

    @Step("Получение данных об ингредиентах")
    protected Response getIngredients() {
        return given()
                .get(INGREDIENTS_API);
    }

    @Step("Создание заказа")
    protected Response createOrder(String token, Ingredients ingredients) {
        if (token == null) {
            return given()
                    .contentType(ContentType.JSON)
                    .body(ingredients)
                    .post(ORDER_API);
        } else {
            return given()
                    .contentType(ContentType.JSON)
                    .auth().oauth2(token)
                    .body(ingredients)
                    .post(ORDER_API);
        }
    }

    @Step("Создание случайного списка ингредиентов")
    protected List<String> generateListIngredients() {

        List<String> _ids = getIngredients()
                .then().extract().body().jsonPath()
                .getList("data._id");

        Random random = new Random();

        for (int i = 0; i < _ids.size(); i++) {
            if (_ids.size() >= 2) {
                if (random.nextBoolean()) {
                    _ids.remove(i);
                }
            }
        }

        return _ids;
    }

    @Step("Получение заказа пользователя")
    protected Response getUserOrders(String token) {
        if (token == null) {
            return given()
                    .contentType(ContentType.JSON)
                    .get(ORDER_API);
        } else {
            return given()
                    .contentType(ContentType.JSON)
                    .auth().oauth2(token)
                    .get(ORDER_API);
        }
    }
}