package gr.fragkiadakis.springmysql;

import gr.fragkiadakis.springmysql.service.SMSService;
import gr.fragkiadakis.springmysql.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableScheduling
public class SMSScheduler {

    // autowire context to exit spring boot app
    @Autowired
    private ApplicationContext context;

    @Autowired
    SMSService smsService;

    @Autowired
    WeatherService weatherService;

    private int smsSent = 0;

    // every 10 minutes so 10*60*1000=600000ms
    @Scheduled(fixedDelay = 600000)
    public void scheduleFixedDelayTask() throws IOException, InterruptedException {
        // select the city we want based on name
        String temperature = weatherService.getCityTemperature("thessaloniki");
        if (!temperature.equals("")) {
            String accessToken = smsService.getAccessToken();
            if (! accessToken.equals("")) {
                smsService.sendSMS(accessToken, temperature);
                smsSent++;
            }
            else System.out.println("Access token not available");
        } else System.out.println("Temperature not available");
    }

    // check if sent sms are 10 then exit app
    @Scheduled(fixedRate = 660000)
    public void shutdownApp() {
        System.out.println(smsSent + " sms has been sent");
        if (smsSent == 10) {
            int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);
            System.exit(exitCode);
        }

    }
}
