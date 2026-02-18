import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class BasicLoadTesting extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://127.0.0.1:8080")
            .acceptHeader("text/html, application/json")
            .contentTypeHeader("application/json");

    Map<String, String> headers_0 = Map.of("Content-Type", "application/json");

    ScenarioBuilder indexLoadTest = scenario("Create index load test")
            .exec(http("index").get("/"))
            .exec(http("sendEmail")
                    .post("/ajax/sendEmail")
                    .headers(headers_0)
                    .body(StringBody("""
                            {
                              "subject": 1,
                              "recipient": 101,
                              "message": "Gatling Post Request Example"
                            }
                            """)))
            .exec(http("list").get("/list"));
    {
        setUp(
                indexLoadTest.injectOpen(atOnceUsers(10))
        ).protocols(httpProtocol)
                .assertions(
                        global().successfulRequests().percent().gt(80.0)
                );
    }
}