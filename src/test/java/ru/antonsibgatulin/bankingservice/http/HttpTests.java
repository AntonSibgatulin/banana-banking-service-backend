package ru.antonsibgatulin.bankingservice.http;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import ru.antonsibgatulin.bankingservice.dto.transaction.request.TransactionDto;
import ru.antonsibgatulin.bankingservice.dto.user.request.UserAuthenticationDto;
import ru.antonsibgatulin.bankingservice.dto.user.request.UserRegistrationDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpTests {


    @Test
    public void testThroughtHttp() {

        var stringRequest = new StringRequest("http://localhost:8080/api");


        String usernameSender = "TestUser1";
        String phoneSender = "79879999999";

        String usernameReceived = "TestUser2";
        String phoneReceived = "79879999998";

        var senderRegDto = new UserRegistrationDto(usernameSender, "TestPassword", "test1@mail.ru", phoneSender, new BigDecimal(2000));

        var receivedRegDto = new UserRegistrationDto(usernameReceived, "TestPassword", "test2@mail.ru", phoneReceived, new BigDecimal(2000));
        String userFirst = null;
        String userSecond = null;
        try {
            userFirst = new JSONObject(stringRequest.post("/auth/singup", senderRegDto, null)).getString("token");

        } catch (Exception e) {
            try {
                String http = stringRequest.post("/auth/signin", new UserAuthenticationDto(usernameSender, "TestPassword"), null);

                userFirst = new JSONObject(http).getString("token");
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        System.out.println(userFirst);

        try {
            userSecond =
                    new JSONObject(stringRequest.post("/auth/singup", receivedRegDto, null)).getString("token");

        } catch (Exception e) {
            System.out.println("Ignore");
        }

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + userFirst);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.submit(()->{
                try {

                    var result = stringRequest.post("/transactions/", new TransactionDto(null, phoneReceived, null, new BigDecimal(100.0)),
                            header);
                    System.out.println(finalI+" "+result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        for (int i = 0; i < 0; i++) {
          //  executorService.submit(()->{
                try {
                    var result = stringRequest.post("/transactions/", new TransactionDto(null, phoneReceived, null, new BigDecimal(100.0)),
                            header);
                    System.out.println(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();

    }
}
