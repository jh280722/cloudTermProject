package com.cloud;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.io.IOException;
import java.util.Scanner;

public class Application {
    static Ec2Client ec2;

    private static void init() {
        ec2 = Ec2Client.builder()
                .region(Region.US_EAST_2)
                .build();
    }

    public static void main(String[] args) throws IOException {

        init();

        Scanner menu = new Scanner(System.in);
        int number = 0;
        String instance_id = "";

        while(true)
        {
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("------------------------------------------------------------");
            System.out.println(" Amazon AWS Control Panel using SDK ");
            System.out.println(" ");
            System.out.println(" Cloud Computing, Computer Science Department ");
            System.out.println(" at Chungbuk National University ");
            System.out.println("------------------------------------------------------------");
            System.out.println(" 1. list instance 2. available zones ");
            System.out.println(" 3. start instance 4. available regions ");
            System.out.println(" 5. stop instance 6. create instance ");
            System.out.println(" 7. reboot instance 8. list images ");
            System.out.println(" 99. quit ");
            System.out.println("------------------------------------------------------------");
            System.out.print("Enter an integer: ");

            number = menu.nextInt();
                switch(number) {
                    case 99:
                        return;
                    default:
                        break;
                }
        }
    }

}