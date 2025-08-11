package com.skillbridge.crons;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class coldStartSolver {

    @Scheduled(cron = "0 * * * * *")
    public void runEveryMinute(){

        //Running just to keep the server up

    }
}
