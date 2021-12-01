package com.cloud;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Scanner;

public class Application {
    static Ec2Client ec2;

    private static void init() {
        ec2 = Ec2Client.builder()
                .region(Region.US_EAST_2)
                .build();
    }

    public static void main(String[] args) {

        init();

        Scanner menu = new Scanner(System.in);
        int number = 0;

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
                    case 1:
                        listInstances();
                        break;
                    case 2:

                        break;
                    case 3:
                        startInstance(getInstanceId());
                        break;
                    case 4:

                        break;
                    case 5:
                        stopInstance(getInstanceId());
                        break;
                    case 6:
                        createEC2Instance(getName(), getAmiId());
                        break;
                    case 7:
                        rebootEC2Instance(getInstanceId());
                        break;
                    case 8:

                        break;
                    case 99:
                        return;
                    default:
                        break;
                }
        }
    }

    private static String getName() {
        System.out.printf("Enter the instance name : ");

        return new Scanner(System.in).nextLine();
    }

    private static String getAmiId() {
        System.out.printf("Enter an image id : ");

        return new Scanner(System.in).nextLine();
    }

    private static String getInstanceId() {
        Scanner id_string = new Scanner(System.in);

        System.out.printf("Enter an instance id : ");

        return id_string.nextLine();
    }


    public static String createEC2Instance(String name, String amiId ) {

        RunInstancesRequest runRequest = RunInstancesRequest.builder()
                .imageId(amiId)
                .instanceType(InstanceType.T1_MICRO)
                .maxCount(1)
                .minCount(1)
                .build();

        RunInstancesResponse response = ec2.runInstances(runRequest);
        String instanceId = response.instances().get(0).instanceId();

        Tag tag = Tag.builder()
                .key("Name")
                .value(name)
                .build();

        CreateTagsRequest tagRequest = CreateTagsRequest.builder()
                .resources(instanceId)
                .tags(tag)
                .build();

        try {
            ec2.createTags(tagRequest);
            System.out.printf(
                    "Successfully started EC2 Instance %s based on AMI %s",
                    instanceId, amiId);

            return instanceId;

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return "";
    }

    public static void startInstance(String instanceId) {

        StartInstancesRequest request = StartInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.startInstances(request);
        System.out.printf("Successfully started instance %s", instanceId);
    }

    public static void stopInstance(String instanceId) {

        StopInstancesRequest request = StopInstancesRequest.builder()
                .instanceIds(instanceId)
                .build();

        ec2.stopInstances(request);
        System.out.printf("Successfully stopped instance %s", instanceId);
    }

    public static void rebootEC2Instance(String instanceId) {

        try {
            RebootInstancesRequest request = RebootInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            ec2.rebootInstances(request);
            System.out.printf(
                    "Successfully rebooted instance %s", instanceId);
        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void listInstances() {
        System.out.println("Listing instances....");

        boolean done = false;
        String nextToken = null;

        try {

            do {
                DescribeInstancesRequest request = DescribeInstancesRequest.builder().maxResults(6).nextToken(nextToken).build();
                DescribeInstancesResponse response = ec2.describeInstances(request);

                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
                        System.out.printf(
                                "[id] %s, " +
                                        "[AMI] %s, " +
                                        "[type] %s, " +
                                        "[state] %10s, " +
                                        "[monitoring state] %s",
                                instance.instanceId(),
                                instance.imageId(),
                                instance.instanceType(),
                                instance.state().name(),
                                instance.monitoring().state());
                        System.out.println();

                    }
                }
                nextToken = response.nextToken();
            } while (nextToken != null);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public static void monitorInstance(String instanceId) {

        MonitorInstancesRequest request = MonitorInstancesRequest.builder()
                .instanceIds(instanceId).build();

        ec2.monitorInstances(request);
        System.out.printf(
                "Successfully enabled monitoring for instance %s",
                instanceId);
    }

    public static void unmonitorInstance(String instanceId) {
        UnmonitorInstancesRequest request = UnmonitorInstancesRequest.builder()
                .instanceIds(instanceId).build();

        ec2.unmonitorInstances(request);

        System.out.printf(
                "Successfully disabled monitoring for instance %s",
                instanceId);
    }
}