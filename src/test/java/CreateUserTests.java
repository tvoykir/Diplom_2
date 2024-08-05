import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTests extends BaseTest {

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Создание пользователя с уникальным именем и почтой")
    public void checkCreateUniqueUser() {
        createUser(email, password, name)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    @Description("Повторная попытка создания пользователя с теми же данными")
    public void checkCreateDuplicateUser() {
        createUser(email, password, name)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));

        createUser(email, password, name)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без обязательного поля email")
    @Description("Попытка создания пользователя без обязательного поля email")
    public void checkCreateUserWithoutEmail() {
        createUser(null, password, name)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без обязательного поля password")
    @Description("Попытка создания пользователя без обязательного поля password")
    public void checkCreateUserWithoutPassword() {
        createUser(email, null, name)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без обязательного поля name")
    @Description("Попытка создания пользователя без обязательного поля name")
    public void checkCreateUserWithoutName() {
        createUser(email, password, null)
                .then()
                .assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
