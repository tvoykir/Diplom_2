import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrdersTests extends BaseUserTest {

    @Test
    @DisplayName("Получение заказов")
    @Description("Получение заказов авторизованного пользователя")
    public void checkGetUserOrdersWithLogIn() {
        setupToken();
        getUserOrders(token);

        getUserOrders(token)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Попытка получение заказов")
    @Description("Получение заказов не авторизованного пользователя")
    public void checkGetUserOrdersWithoutLogIn () {
        getUserOrders(null)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}