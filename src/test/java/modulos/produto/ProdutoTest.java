package modulos.produto;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de API REST do modulo do Produto")
public class ProdutoTest {
    private String token;

    @BeforeEach
    public void beforeEach() {

        baseURI = "http://165.227.93.41";
        basePath = "/lojinha";

        this.token = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.criarUsuarioAdmin())
                //quando
                .when()
                    .post("/v2/login")
                //então
                .then()
                    .extract()
                        .path("data.token");

    }
    @Test
    @DisplayName("Validar que o valor do produto igual a 0.00 não é permitido")
    public void testValidarLimitesProibidosValorProduto () {

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComValorIgualA(0.00))
                .when()
                    .post("/v2/produtos")
                .then()
                    .assertThat()
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);

    }

    @Test
    @DisplayName("Validar que o valor do produto igual a 7000,01 não é permitido")
    public void testValidarLimitesMaiorSeteMilProibidoValorProduto () {

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComValorIgualA(7000.01))
                .when()
                .post("/v2/produtos")
                .then()
                .assertThat()
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);

    }

    @Test
    @DisplayName("Validar que o valor do produto igual a 7000,00")
    public void testValidarLimitesValidosValorProduto () {

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComValorIgualA(7000.00))
                .when()
                .post("/v2/produtos")
                .then()
                .assertThat()
                .statusCode(201);

    }
}
