package com.tg.jackysdailychallenge.controller;

import com.google.common.collect.Lists;
import com.tg.jackysdailychallenge.model.Challenger;
import com.tg.jackysdailychallenge.service.ChallengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ChallengerController {
    @Autowired
    ChallengerService challengerService;

    private final String challengerRequestBase = "/challengers/";

    @RequestMapping(value = challengerRequestBase, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Challenger>> findAll() {
        return ResponseEntity.ok(challengerService.findAll());
    }

    @RequestMapping(value = challengerRequestBase + "{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Challenger> findByUserId(@PathVariable("userId") int userId) {
        return challengerService.findByUserId(userId)
            .map(ResponseEntity::ok)
            .orElseGet(ResponseEntity.notFound()::build);
    }
}
