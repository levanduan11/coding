package com.coding.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class SearchService {
    static List<Customer> customers;

    static {
        try (Stream<String> stream = Files.lines(Paths.get("customer.csv"))) {
            customers = stream
                    .skip(1)
                    .map(s -> s.split(","))
                    .map(strings -> {
                        Customer customer = new Customer();
                        customer.setId(strings[0]);
                        customer.setFirstName(strings[1]);
                        customer.setLastName(strings[2]);
                        customer.setEmail(strings[3]);
                        customer.setGender(strings[4]);
                        customer.setBirthDate(LocalDate.parse(strings[5]));
                        return customer;
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static List<Customer> search(Customer searchRequest) {
        return customers.stream()
                .filter(customer -> resolveCustomerCondition(customer, searchRequest))
                .toList();
    }

    private static boolean resolveCustomerCondition(Customer customer, Customer searchRequest) {
        boolean searchCondition = true;
        String idReq = searchRequest.getId();
        String firstNameReq = searchRequest.getFirstName();
        String lastNameReq = searchRequest.getLastName();
        String emailReq = searchRequest.getEmail();
        String genderReq = searchRequest.getGender();
        LocalDate birthDateReq = searchRequest.getBirthDate();
        if (idReq != null && !idReq.isBlank()) {
            searchCondition = (customer.getId().equalsIgnoreCase(idReq));
        }
        if (firstNameReq != null && !firstNameReq.isBlank()) {
            searchCondition = searchCondition && (customer.getFirstName().equalsIgnoreCase(firstNameReq));
        }
        if (lastNameReq != null && !lastNameReq.isBlank()) {
            searchCondition = searchCondition && (customer.getLastName().equalsIgnoreCase(lastNameReq));
        }
        if (emailReq != null && !emailReq.isBlank()) {
            searchCondition = searchCondition && (customer.getEmail().contains(emailReq));
        }
        if (genderReq != null && !genderReq.isBlank()) {
            searchCondition = searchCondition && (customer.getGender().equalsIgnoreCase(genderReq));
        }
        if (birthDateReq != null) {
            searchCondition = searchCondition && (customer.getBirthDate().equals(birthDateReq));
        }
        return searchCondition;
    }

    static void print() {
        customers.forEach(customer -> {
            System.out.println(customer);
            System.out.println("---------------");
        });
    }

    static void test() {
        Customer customer = new Customer(null, null, null, null, "Genderfluid", null);
        List<Customer> customers1 = search(customer);
        customers1.forEach(System.out::println);
    }


    public static void main(String[] args) throws Throwable {
        print();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean stop = false;
        while (!stop) {
            System.out.println("Enter id:");
            String idReq = reader.readLine();
            System.out.println("Enter first name:");
            String firstNameReq = reader.readLine();
            System.out.println("Enter last name:");
            String lastNameReq = reader.readLine();
            System.out.println("Enter email:");
            String emailReq = reader.readLine();
            System.out.println("Enter gender:");
            String genderReq = reader.readLine();
            System.out.println("Enter birth date:");
            LocalDate birthDateReq = null;
            String b = reader.readLine();
            if (!b.trim().isBlank()) {
                birthDateReq = LocalDate.parse(b);
            }
            Customer customer = new Customer(idReq, firstNameReq, lastNameReq, emailReq, genderReq, birthDateReq);
            List<Customer> res = search(customer);
            System.out.println("************");
            res.forEach(customer1 -> {
                System.out.println(customer1);
                System.out.println(">>>>>>>>>");
            });
            System.out.println("search continue ? y/n");
            String s = reader.readLine();
            if (s.equalsIgnoreCase("n")) {
                stop = true;
            }
        }
    }
}
