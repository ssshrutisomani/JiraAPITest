import static io.restassured.RestAssured.*;

import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

public class JiraAPI {

    @Test
    public void loginJira(){
        baseURI="http://localhost:8080";

        SessionFilter session=new SessionFilter();
        //Jira Login
        String response=given().header("Content-Type","application/json").body("{ \"username\": \"ssshrutisomani\", \"password\": \"jira1234\" }")
                .filter(session).when().post("/rest/auth/1/session").then().assertThat().statusCode(200).extract().response().asString();

        System.out.println(response);
        /*JsonPath js=new JsonPath(response);
        String value=js.getString("session.value");
        System.out.println(value);*/

        //Add a comment
       String commentRes= given().pathParams("key","RSA-5").header("Content-Type","application/json")
                .body("{\n" +
                        "    \"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eget venenatis elit. Duis eu justo eget augue iaculis fermentum. Sed semper quam laoreet nisi egestas at posuere augue semper.\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}").filter(session).when().post("/rest/api/2/issue/{key}/comment").then().assertThat().statusCode(201)
                .extract().response().asString();

       System.out.println(commentRes);

        //Add an attachment

        given().pathParams("key","RSA-5").header("Content-Type","multipart/form-data").header("X-Atlassian-Token","no-check").filter(session).multiPart("file",new File("jira.txt"))
        .when()
                .post("/rest/api/2/issue/{key}/attachments").then().assertThat().statusCode(200);

        //Get an issue detail

        String issueDetail=given().filter(session).pathParams("key","RSA-5").when().get("/rest/api/2/issue/{key}").then().assertThat().statusCode(200).extract().response().asString();

        System.out.println(issueDetail);

    }


}
