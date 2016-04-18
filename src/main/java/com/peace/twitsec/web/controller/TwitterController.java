package com.peace.twitsec.web.controller;

import com.peace.twitsec.service.TwitterService;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Api(value = "user", description = "User Service")
public class TwitterController {

    @Autowired
    private TwitterService twitterService;

    /*
    @ApiOperation(value = "List Followers of Given User")
    @RequestMapping(value = "myFollowers", method = RequestMethod.POST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    public @ResponseBody
    ListFollowersResponse getMyMeetings(@RequestBody BaseRequest request) {

        ListFollowersResponse followerResponse = new ListFollowersResponse();
        followerResponse.setFollowerList(twitterService.fetchFollowers(request));

        return followerResponse;

    }
    */
}
