package com.justynagajdek.healthreservationsystem;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("test123");
        System.out.println(hash);
        String hashDoctor = encoder.encode("testdoctor");
        System.out.println(hashDoctor);
        String hashNurse = encoder.encode("testnurse");
        System.out.println(hashNurse);
        String hashReceptionist = encoder.encode("testrecep");
        System.out.println(hashReceptionist);
        String hashAdmin = encoder.encode("testadminr");
        System.out.println(hashAdmin);
    }
}
