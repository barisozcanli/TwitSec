package com.peace.twitsec.web.controller;

import com.peace.twitsec.data.mongo.model.BlockReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.model.UserPreferences;
import com.peace.twitsec.data.session.TwitSecSession;
import com.peace.twitsec.http.request.AuthenticationRequest;
import com.peace.twitsec.http.request.BaseRequest;
import com.peace.twitsec.http.request.CreateUserRequest;
import com.peace.twitsec.http.request.UpdateUserPreferenceRequest;
import com.peace.twitsec.http.response.LoginResponse;
import com.peace.twitsec.service.BlockReportService;
import com.peace.twitsec.service.UserService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@Api(value = "user", description = "User Service")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlockReportService blockReportService;

    @ApiOperation(value="Create User")
    @RequestMapping(value="/user/create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody User createUser(@RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    @ApiOperation(value="Login")
    @RequestMapping(value="/user/login", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody
    LoginResponse authenticate(@RequestBody AuthenticationRequest request){

        return userService.authenticate(request);

    }

    @ApiOperation(value="Logout")
    @RequestMapping(value="/user/logout", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody boolean logout(@RequestBody BaseRequest request){
        return userService.logout(request);
    }

    @ApiOperation(value="Update User Preferences")
    @RequestMapping(value="/user/updateUserPreferences", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody
    UserPreferences updateUser(@RequestBody UpdateUserPreferenceRequest request) {

        return userService.updateUserPreferences(request);
    }

    @ApiOperation(value="Get Logon User")
    @RequestMapping(value="/user/get", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody
    User getLogonUser(@RequestBody BaseRequest request) {

        User authenticatedUser = TwitSecSession.getInstance().getUser(request.getAuthToken());
        return userService.findById(authenticatedUser.getId());
    }

    @ApiOperation(value="Get Blocked Users")
    @RequestMapping(value="/user/getBlockedUsers", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody
    List<BlockReport> getBlockedUsers(@RequestBody BaseRequest request) {

        User authenticatedUser = TwitSecSession.getInstance().getUser(request.getAuthToken());
        return blockReportService.getBlockReportsOfUser(authenticatedUser);
    }
}
