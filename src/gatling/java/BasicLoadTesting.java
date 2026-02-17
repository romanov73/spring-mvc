import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class BasicLoadTesting extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://127.0.0.1:8080")
            .acceptHeader("text/html");

    ScenarioBuilder scn = scenario("Create index load test")
            .exec(http("index").get("/"));

    {
        setUp(
                scn.injectOpen(
                        rampUsersPerSec(1).to(10000).during(Duration.ofSeconds(10))
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().successfulRequests().percent().gt(95.0)
                );
    }
}