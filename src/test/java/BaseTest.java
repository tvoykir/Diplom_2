import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import models.Client;

import static io.restassured.RestAssured.given;

public class BaseTest {

    private static final String URI = "https://stellarburgers.nomoreparties.site/api";
    protected static final String REGISTER_API = "/auth/register";
    protected static final String LOGIN_API = "/auth/login";
    protected static final String USER_API = "/auth/user";


    protected static Faker faker = new Faker();
    protected String email;
    protected String password;
    protected String name;
    protected String token;


    @BeforeClass
    @DisplayName("Инициализация адреса шлюза")
    @Description("Инициализация адреса шлюза")
    public static void setup() {
        RestAssured.baseURI = URI;
    }

    @Before
    @DisplayName("Инициализация нового пользователя для теста")
    @Description("Инициализация логина, пароля и почтового адреса")
    public void initTest() {
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().username();
    }

    @After
    @DisplayName("Очистка тестовых данных")
    @Description("Удаление пользователя после каждого теста")
    public void after() {
        if(logInUser(email, password).then().extract().path("accessToken") != null) {
            setupToken();
            deleteUser(token)
                    .then().assertThat().statusCode(202);
        }
    }

    @Step()
    protected void setupToken() {
        token = logInUser(email, password).then().extract().path("accessToken").toString().replace("Bearer ", "");
    }

    @Step("Создание пользователя")
    protected Response createUser(String email, String password, String name) {
        Client client = new Client(email, password, name);

        return given()
                .contentType(ContentType.JSON)
                .body(client)
                .post(REGISTER_API);
    }

    @Step("Логин пользователя")
    protected Response logInUser(String email, String password) {
        Client client = new Client(email, password, null);

        return given()
                .contentType(ContentType.JSON)
                .body(client)
                .post(LOGIN_API);
    }

    @Step("Удаление пользователя")
    protected Response deleteUser(String token) {
        return given()
                .auth().oauth2(token)
                .delete(USER_API);
    }
}