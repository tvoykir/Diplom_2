import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTests extends BaseUserTest {

    @Test
    @DisplayName("Проверка изменения почты пользователя")
    @Description("Изменение почты пользователя")
    public void checkChangeEmailUser() {
        token = logInUser(email, password).then().extract().path("accessToken").toString().replace("Bearer ", "");

        String new_email = faker.internet().emailAddress();

        changeUserData(token, new_email, null)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(new_email));
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя")
    @Description("Изменение имени пользователя")
    public void checkChangePassword() {
        token = logInUser(email, password).then().extract().path("accessToken").toString().replace("Bearer ", "");

        String new_name = faker.name().username();

        changeUserData(token, email, new_name)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("user.name", equalTo(new_name));
    }

    @Test
    @DisplayName("Проверка изменения данных пользователя без авторизации")
    @Description("Попытка изменения почты и имени пользователя без авторизации")
    public void checkChangePasswordWithoutLogIn() {
        String new_email = faker.internet().emailAddress();
        String new_name = faker.name().username();

        changeUserData("", new_email, new_name)
                .then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}