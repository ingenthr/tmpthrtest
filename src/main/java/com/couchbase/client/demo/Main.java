package com.couchbase.client.demo;

import com.couchbase.client.CouchbaseClient;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    private static int numBuckets = 1;
    private static CouchbaseClient clients[];
    private static List<URI> baseList;


    public static void main(String args[]) throws InterruptedException {

        baseList = Arrays.asList(
                //URI.create("http://192.168.1.200:8091/pools"),
                URI.create("http://localhost:8091/pools"));


        clients = new CouchbaseClient[numBuckets];

        Thread.sleep(100);

        System.out.println("Before starting test...");
        printThreads();
        createClients();
        System.out.println("Sleeping after client create...");
        Thread.sleep(1500);
        System.out.println("Post creating clients...");
        printThreads();
        System.out.println("Sleeping for more checking extenrally.");
        Thread.sleep(60000);
        System.out.println("Shutting down...");
        shutdownClients();
        printThreads();
        System.exit(0);


    }

    private static void printThreads() {
        int currThreads =  Thread.getAllStackTraces().keySet().size();

        int runningThreads = 0;
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState()==Thread.State.RUNNABLE) runningThreads++;
        }

        System.out.println("Threads: " + runningThreads + "/" + currThreads + " (run/total)");
    }


    public static void createClients() {
        for (int i=0; i<numBuckets; i++) {
            String bucketName = "bucket" + i;
            System.out.println("creating " + bucketName);
            try {
                clients[i] = new CouchbaseClient(baseList, bucketName, bucketName);
            } catch (IOException e) {
                System.err.println("Failed to create client: " + e.getCause());
            }
        }
    }

    public static void shutdownClients() {
        for (int i=0; i<numBuckets; i++) {
            String bucketName = "bucket" + i;
            clients[i].shutdown(1, TimeUnit.SECONDS);
        }

    }

}
