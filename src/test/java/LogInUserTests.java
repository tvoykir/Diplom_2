import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LogInUserTests extends BaseTest {

    @Test
    @DisplayName("Проверка входа существующего пользователя")
    @Description("Вход существующего пользователя")
    public void checkLogInExistingUser() {
        createUser(email, password, name)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        logInUser(email, password)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Проверка входа с неверным логином и паролем")
    @Description("Попытка входа пользователя с неверным логином и паролем")
    public void checkLogInWithIncorrectCredentials() {
        logInUser(email, password)
                .then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}