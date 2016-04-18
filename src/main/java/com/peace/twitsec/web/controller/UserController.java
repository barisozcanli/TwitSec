package com.peace.twitsec.web.controller;

import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.http.request.CreateUserRequest;
import com.peace.twitsec.service.UserService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Api(value = "user", description = "User Service")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="Create User")
    @RequestMapping(value="/user/create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody User createUser(@RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

}