package controllers

import Actions.Authorized
import io.swagger.annotations.{Api, ApiResponse, ApiResponses, ApiOperation}
import play.api.mvc._

@Api(value = "/", description = "Operations about application")
class Application extends Controller {

  @ApiOperation(
    nickname = "index",
    value = "Test web service is up", httpMethod = "GET")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Your new application is ready.")))
  def index = Authorized {
    Ok("Your new application is ready.")
  }

}